package net.playwright.magitacz.Utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class DamageSourceUtils {
    public static DamageSource getDamageSourceFromResource(Level level, ResourceKey<DamageType> damageTypeKey, Entity directEntity, Entity causingEntity) {
        // Retrieve the Holder<DamageType> from the registry
        Holder<DamageType> damageTypeHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(damageTypeKey);
        // Create and return the DamageSource
        return new DamageSource(damageTypeHolder, directEntity, causingEntity);
    }
}
