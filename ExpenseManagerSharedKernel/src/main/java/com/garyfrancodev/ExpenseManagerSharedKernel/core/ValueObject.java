package com.garyfrancodev.ExpenseManagerSharedKernel.core;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for all Value Objects in Domain-Driven Design.
 *
 * <p><b>¿Qué es un Value Object?</b></p>
 * Es un objeto que no posee identidad conceptual propia (no tiene ID). Se define
 * exclusivamente por el conjunto de sus atributos.
 *
 * <p><b>Objetivos clave:</b></p>
 * <ul>
 *   <li><b>Inmutabilidad:</b> Una vez instanciado, su estado no puede cambiar. Cualquier
 *       operación que modifique valores retorna una nueva instancia.</li>
 *   <li><b>Igualdad por valor:</b> Dos Value Objects son iguales si todos sus atributos
 *       son iguales (sobrescribe {@code equals} y {@code hashCode}).</li>
 *   <li><b>Auto-validación e Invariantes:</b> Encapsula reglas de negocio para asegurar
 *       que nunca exista un Value Object en un estado inválido.</li>
 *   <li><b>Eliminar Obsesión Primitiva:</b> Reemplaza tipos primitivos (Double, String)
 *       por conceptos del Lenguaje Ubicuo (Money, UserEmailValue, AccountName).</li>
 * </ul>
 */
public abstract class ValueObject implements Serializable {

    protected static void checkRule(BusinessRule rule) throws BusinessRuleValidationException {
        Objects.requireNonNull(rule, "Rule cannot be null");
        if (!rule.isValid()) {
            throw new BusinessRuleValidationException(rule);
        }
    }
}
