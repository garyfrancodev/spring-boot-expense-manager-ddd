package com.garyfrancodev.ExpenseManagerSharedKernel.core;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Base class for Domain Events in Domain-Driven Design.
 *
 * <p><b>¿Qué es un Domain Event?</b></p>
 * Representa un hecho significativo que ya ocurrió en el dominio (en tiempo pasado,
 * ej: {@code AccountDebited}, {@code UserCreated}).
 *
 * <p><b>Objetivos:</b></p>
 * <ul>
 *   <li>Desacoplar agregados y bounded contexts permitiendo consistencia eventual o reacciones secundarias.</li>
 *   <li>Inmutabilidad: Los eventos reflejan el pasado y no pueden ser alterados.</li>
 *   <li>Llevar únicamente la información relevante requerida por los consumidores del evento.</li>
 * </ul>
 */
public abstract class DomainEvent implements Serializable {

    private final UUID eventId;
    private final Date occurredOn;

    public DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = new Date();
    }

    public DomainEvent(Date occurredOn) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = occurredOn != null ? occurredOn : new Date();
    }

    public UUID getEventId() {
        return eventId;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }
}
