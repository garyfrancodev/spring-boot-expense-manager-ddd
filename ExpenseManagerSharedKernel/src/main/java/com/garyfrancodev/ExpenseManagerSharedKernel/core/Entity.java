package com.garyfrancodev.ExpenseManagerSharedKernel.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all Entities in Domain-Driven Design.
 *
 * <p><b>¿Qué es una Entidad?</b></p>
 * Un objeto con identidad propia y continua a lo largo del tiempo. Su igualdad no
 * depende de sus atributos, sino de su identificador único (ID).
 *
 * <p><b>Objetivos:</b></p>
 * <ul>
 *   <li>Mantener y proteger los invariantes de negocio a través de métodos con comportamiento.</li>
 *   <li>Registrar eventos de dominio ocurridos durante las transiciones de estado.</li>
 *   <li>Identidad única inmutable.</li>
 * </ul>
 */
public abstract class Entity implements Serializable {

    protected UUID id;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Entity() {
        this.id = UUID.randomUUID();
    }

    public Entity(UUID id) {
        this.id = id != null ? id : UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void addDomainEvent(DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    protected void checkRule(BusinessRule rule) throws BusinessRuleValidationException {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        if (!rule.isValid()) {
            throw new BusinessRuleValidationException(rule);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id != null && id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
