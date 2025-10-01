package net.playwright.magitacz.affix;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.Arrays;
import java.util.Optional;

public enum ElementType {
    BLOOD("Blood", ISSDamageTypes.BLOOD_MAGIC),
    LIGHTNING("Lightning", ISSDamageTypes.LIGHTNING_MAGIC),
    ENDER("Ender", ISSDamageTypes.ENDER_MAGIC),
    EVOCATION("Evocation", ISSDamageTypes.EVOCATION_MAGIC),
    FIRE("Fire", ISSDamageTypes.FIRE_MAGIC),
    ICE("Ice", ISSDamageTypes.ICE_MAGIC),
    ELDRITCH("Eldritch", ISSDamageTypes.ELDRITCH_MAGIC),
    HOLY("Holy", ISSDamageTypes.HOLY_MAGIC),
    NATURE("Nature", ISSDamageTypes.NATURE_MAGIC);

    public final String name;
    public final ResourceKey<DamageType> damageType;

    ElementType(String name, ResourceKey<DamageType> damageType) {
        this.name = name;
        this.damageType = damageType;
    }

    public static Optional<ElementType> fromString(String str) {
        return Arrays.stream(values())
                .filter(e -> e.name.equalsIgnoreCase(str))
                .findFirst();
    }
}