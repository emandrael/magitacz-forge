package net.playwright.magitacz.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import dev.shadowsoffire.apotheosis.adventure.affix.*;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.AbstractValuedAffix;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.GunAffix;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Set;

// --- Iron's Spellbooks imports ---
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.level.Level;
import net.playwright.magitacz.Utils.DamageSourceUtils;

public class ElementalBulletAffix extends AbstractValuedAffix implements GunAffix {

    public static final String ELEMENT_DAMAGE_TRACKER_TAG = "magitacz.elemental_damage";

    // How much elemental damage before effect triggers
    public static final float DAMAGE_THRESHOLD = 20f;

    // Duration and amplifier - tune as you like!
    public static final int DEFAULT_EFFECT_DURATION = 100; // 5 seconds
    public static final int DEFAULT_EFFECT_AMPLIFIER = 1;

    public static final Codec<ElementalBulletAffix> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
            LootCategory.SET_CODEC.fieldOf("types").forGetter(AbstractValuedAffix::getApplicableCategories),
            GemBonus.VALUES_CODEC.fieldOf("values").forGetter(AbstractValuedAffix::getValues),
            Codec.STRING.fieldOf("element").forGetter(a -> a.element),
            Codec.STRING.optionalFieldOf("exclusive_group", "").forGetter(a -> a.exclusiveGroup)
    ).apply(inst, ElementalBulletAffix::new));

    protected final String element;
    protected final String exclusiveGroup;


    public String getElement() {
        return element;
    }

    public ElementalBulletAffix(Set<LootCategory> categories, Map<LootRarity, StepFunction> values, String element, String exclusiveGroup) {
        super(AffixType.ABILITY ,categories, values);
        this.element = element;
        this.exclusiveGroup = element;
    }
//
//    @Override
//    public boolean canApplyTo(ItemStack stack, LootCategory cat, LootRarity rarity) {
//        if (!super.canApplyTo(stack, cat, rarity)) return false;
//
//        Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);
//        for (DynamicHolder<? extends Affix> holder : affixes.keySet()) {
//            if (holder == null) continue;
//            if (!holder.isBound()) continue;
//            Affix affix = holder.get();
//            if (affix instanceof ElementalBulletAffix) {
//                return false;
//            }
//        }
//        return true;
//
//    }

    @Override
    public MutableComponent getDescription(ItemStack stack, LootRarity rarity, float level) {
        Component value =
                Component.literal(String.format("%d", (long) getValue(stack, rarity, level)))
                        .withStyle(ChatFormatting.WHITE);

        Component elementName = Component.literal(this.element)
                .withStyle(style ->
                        style.withColor(getSchoolColor(this.element)));
        return Component.translatable("affix.magitacz.elemental_bullet.desc",
                        value, elementName); // orange, for example
    }

    @Override
    public Component getAugmentingText(ItemStack stack, LootRarity rarity, float level) {
        double value = getValue(stack, rarity, level);
        double min = getValue(stack, rarity, 0.0F);
        double max = getValue(stack, rarity, 1.0F);
        return Component.translatable("affix.magitacz.elemental_bullet.augment",
                        value, min, max, element)
                .withStyle(s -> s.withColor(0xFF8000));
    }


    @Override
    public void onGunshotPost(ItemStack gun, AffixInstance instance, EntityHurtByGunEvent.Post event) {
        LivingEntity attacker = event.getAttacker();
        LivingEntity livingVictim = (LivingEntity) event.getHurtEntity();

        Level level = attacker.level();
        ElementalBulletAffix affix = (ElementalBulletAffix) instance.affix().get();
        double value = this.getValue(instance.stack(), instance.rarity().get(), instance.level());


        float elementalDamage = (float) (event.getAmount() * ((value/100)));


        var tag = livingVictim.getPersistentData();
        var subTag = tag.getCompound(ELEMENT_DAMAGE_TRACKER_TAG);



        float oldAmount = subTag.contains(element) ? subTag.getFloat(element) : 0f;
        float newAmount = oldAmount + elementalDamage;


        if (newAmount >= DAMAGE_THRESHOLD) {
            // apply effect

            if (element.equals("fire")) {
                livingVictim.setSecondsOnFire(5);
                // Optionally apply a visual effect too if you want to show something in tooltip
            } else if (element.equals("ice")) {
                livingVictim.setTicksFrozen(5 * 20);
            }

            MobEffect effect = getElementEffect(element);
            if (effect != null && !livingVictim.hasEffect(effect)) {
                livingVictim.addEffect(new MobEffectInstance(effect, DEFAULT_EFFECT_DURATION, DEFAULT_EFFECT_AMPLIFIER));
            }
            // reset counter
            subTag.putFloat(element, newAmount - DAMAGE_THRESHOLD); // or 0 for full reset
        } else {
            subTag.putFloat(element, newAmount);
        }

        tag.put(ELEMENT_DAMAGE_TRACKER_TAG, subTag);

        DamageSource elementalDamageSource = DamageSourceUtils.getDamageSourceFromResource(level, getElementISSDamageType(affix.element),attacker,livingVictim);

        float damageAmount = (float) (event.getAmount() * ((value/100)));

        livingVictim.hurt(elementalDamageSource ,damageAmount);
    }

    public static MobEffect getElementEffect(String element) {
        return switch (element.toLowerCase()) {
            case "ice" -> MobEffectRegistry.CHILLED.get(); // Or SpellRegistry.FREEZE.get() if Iron's Spellbooks exposes it
            case "lightning" -> MobEffects.WEAKNESS; // Could use Slowness or Stun if you have one
            case "blood" -> MobEffects.POISON;
            case "ender" -> MobEffects.LEVITATION;
            case "evocation" -> MobEffects.CONFUSION; // Or use a custom 'evocation' effect if available
            case "eldritch" -> MobEffects.BLINDNESS;  // Or Wither
            case "holy" -> MobEffects.GLOWING; // Or INSTANT_HEALTH for positive, or wither for undead
            case "nature" -> MobEffects.POISON; // Or REGEN if you want positive, else Poison for "nature's bite"
            default -> null;
        };
    }

    private static int getSchoolColor(String element) {
        return switch (element.toLowerCase()) {
            case "fire"      -> 0xFFFF8000;  // orange      (FF alpha)
            case "ice"       -> 0xFF60CCFF;  // icy blue    (FF alpha)
            case "lightning" -> 0xFFFFD700;  // yellow      (FF alpha)
            case "blood"     -> 0xFFAA0033;  // deep red    (FF alpha)
            case "ender"     -> 0xFFA020F0;  // purple      (FF alpha)
            case "evocation" -> 0xFF00FFAA;  // teal        (FF alpha)
            case "eldritch"  -> 0xFF4B0082;  // indigo      (FF alpha)
            case "holy"      -> 0xFFFFFF88;  // pearl       (FF alpha)
            case "nature"    -> 0xFF22CC22;  // green       (FF alpha)
            default -> 0xFFFFFFFF;           // white       (FF alpha)
        };
    }

    /** Map string name to ISB Attribute */
    public static Attribute getElementAttribute(String element) {
        return switch (element.toLowerCase()) {
            case "blood"     -> AttributeRegistry.BLOOD_SPELL_POWER.get();
            case "lightning" -> AttributeRegistry.LIGHTNING_SPELL_POWER.get();
            case "ender"     -> AttributeRegistry.ENDER_SPELL_POWER.get();
            case "evocation" -> AttributeRegistry.EVOCATION_SPELL_POWER.get();
            case "fire"      -> AttributeRegistry.FIRE_SPELL_POWER.get();
            case "ice"       -> AttributeRegistry.ICE_SPELL_POWER.get();
            case "eldritch"  -> AttributeRegistry.ELDRITCH_SPELL_POWER.get();
            case "holy"      -> AttributeRegistry.HOLY_SPELL_POWER.get();
            case "nature"    -> AttributeRegistry.NATURE_SPELL_POWER.get();
            default -> null;
        };
    }

    public static ResourceKey<DamageType> getElementISSDamageType(String element) {
        return switch (element.toLowerCase()) {
            case "blood"     -> ISSDamageTypes.BLOOD_MAGIC;
            case "lightning" -> ISSDamageTypes.LIGHTNING_MAGIC;
            case "ender"     -> ISSDamageTypes.ENDER_MAGIC;
            case "evocation" -> ISSDamageTypes.EVOCATION_MAGIC;
            case "fire"      -> ISSDamageTypes.FIRE_MAGIC;
            case "ice"       -> ISSDamageTypes.ICE_MAGIC;
            case "eldritch"  -> ISSDamageTypes.ELDRITCH_MAGIC;
            case "holy"      -> ISSDamageTypes.HOLY_MAGIC;
            case "nature"    -> ISSDamageTypes.NATURE_MAGIC;
            default -> null;
        };
    }

    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }
}