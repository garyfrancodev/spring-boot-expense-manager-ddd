package com.garyfrancodev.ExpenseManagerSharedKernel.core;

/**
 * Contract for discrete business rules.
 *
 * <p><b>¿Qué es una Business Rule?</b></p>
 * Una especificación atómica y testeable de un invariante o restricción del dominio.
 *
 * <p><b>Objetivos:</b></p>
 * <ul>
 *   <li>Evitar código de validación duplicado o disperso en controladores o servicios.</li>
 *   <li>Permitir validaciones declarativas dentro de Entidades y Value Objects.</li>
 *   <li>Emitir mensajes de error claros y explicativos en caso de violación.</li>
 * </ul>
 */
public interface BusinessRule {
    boolean isValid();
    String getMessage();
}
