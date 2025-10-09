package net.playwright.magitacz.mixins;

import com.obscuria.aquamirae.common.entities.CaptainCornelia;
import com.obscuria.aquamirae.common.items.weapon.*;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.playwright.magitacz.MagitaczMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaptainCornelia.class)
public abstract class CaptainCorneliaMixin extends Monster {
    protected CaptainCorneliaMixin(EntityType<? extends Mob> type, Level level) { super((EntityType<? extends Monster>) type, level); }

    // Shadow the private helper from CaptainCornelia:
    @Shadow(remap = false)
    private boolean doHurtTarget(LivingEntity entity, DamageSource source, float amount) { return false; }

    @Inject(method = "doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"), cancellable = true)
    private void iss$swapDamageTypes(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof LivingEntity living)) return;
        Item held = this.getMainHandItem().getItem();

        MagitaczMod.LOGGER.info("ITEM: ",held.getDescriptionId());


        // Whisper of the Abyss -> ender magic
        if (held instanceof WhisperOfTheAbyssItem) {
            cir.setReturnValue(this.doHurtTarget(living, iss$damage(ISSDamageTypes.ENDER_MAGIC), 8.0F));
            return;
        }

        // Divider -> lightning magic
        if (held instanceof DividerItem) {
            float amt = Math.max(4.0F, living.getHealth() / 2.0F);
            cir.setReturnValue(this.doHurtTarget(living, iss$damage(ISSDamageTypes.LIGHTNING_MAGIC), amt));
            return;
        }

        if (held instanceof CoralLanceItem) {
            float amt = Math.max(5.0F, living.getHealth() / 2.0F);
            cir.setReturnValue(this.doHurtTarget(living, iss$damage(ISSDamageTypes.ICE_MAGIC), amt));
            return;
        }

        // Poisoned Blade -> ice magic (still applies poison first)
        if (held instanceof PoisonedBladeItem) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 2));
            float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            cir.setReturnValue(this.doHurtTarget(living, iss$damage(ISSDamageTypes.ICE_MAGIC), base));
            return;
        }

        if (held instanceof RemnantsSaberItem) {
            this.heal(Math.min(30.0F, living.getMaxHealth() * 0.4F));
            float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            cir.setReturnValue(this.doHurtTarget(living, iss$damage(ISSDamageTypes.EVOCATION_MAGIC), base));
            return;
        }

        // For other weapons (e.g., Remnants Saber), let the original method run.
    }

    // Build a DamageSource tied to this entity for a given damage type key
    @Unique
    private DamageSource iss$damage(ResourceKey<DamageType> key) {
        Registry<DamageType> reg = this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> holder = reg.getHolderOrThrow(key);
        // In 1.20.1, this constructor sets both direct and causing entity to "this"
        return new DamageSource(holder, this);
    }
}