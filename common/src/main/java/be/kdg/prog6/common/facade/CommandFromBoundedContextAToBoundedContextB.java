package be.kdg.prog6.common.facade;

/**
 * Command contract to invoke functionality in Bounded Context B from Bounded Context A.
 * <p>
 * This record is placed in the Shared Kernel so both bounded contexts can reference
 * the same command structure when using the Facade pattern.
 * <p>
 * Unlike events, this represents an <b>intention</b> to perform an action,
 * not something that has already happened.
 * <p>
 * Example: Landside BC -> Warehousing BC
 */
public record CommandFromBoundedContextAToBoundedContextB() {}