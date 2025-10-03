package net.playwright.magitacz.Utils;

import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.init.ModAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class DamageTypeUtils {
    public static double spellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.SPELL_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }

    public static double bulletResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(ModAttributes.BULLET_RESISTANCE.get());
        return inst != null ? inst.getValue() : 0.0;
    }

    public static double bloodSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.BLOOD_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }

    public static double lightSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.HOLY_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }

    public static double fireSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.FIRE_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }
    public static double iceSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.ICE_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }
    public static double enderSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.ENDER_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }

    public static double evocationSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.EVOCATION_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }
    public static double lightningSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.LIGHTNING_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }
    public static double eldritchSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.ELDRITCH_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }
    public static double natureSpellResistOf(LivingEntity e) {
        AttributeInstance inst = e.getAttribute(AttributeRegistry.NATURE_MAGIC_RESIST.get());
        return inst != null ? inst.getValue() : 0.0;
    }





}
