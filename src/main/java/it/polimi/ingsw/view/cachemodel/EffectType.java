package it.polimi.ingsw.view.cachemodel;

/**
 * Effect type of a single weapon, they can be EXCLUSIVE if you can chose only alternatevely between base or first effect,
 * CONCATENABLE if you can choose other effects but only in order, CONCATENABLE_NON_ORD for weapons whose first effect can be
 * done before base effect
 */
public enum EffectType {

    ESCLUSIVE,
    CONCATENABLE,
    CONCATENABLE_NON_ORD,
}
