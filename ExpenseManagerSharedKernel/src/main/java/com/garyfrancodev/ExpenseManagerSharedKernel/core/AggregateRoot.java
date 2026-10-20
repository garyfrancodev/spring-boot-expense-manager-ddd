package com.garyfrancodev.ExpenseManagerSharedKernel.core;

import java.util.UUID;

/**
 * Base class for all Aggregate Roots in Domain-Driven Design.
 *
 * <p><b>¿Qué es un Aggregate Root?</b></p>
 * Es la entidad raíz de un agregado que actúa como puerta de entrada para todas las
 * modificaciones del estado de dicho agregado y salvaguarda sus invariantes de negocio.
 */
public abstract class AggregateRoot extends Entity {

    public AggregateRoot() {
        super();
    }

    public AggregateRoot(UUID id) {
        super(id);
    }
}
