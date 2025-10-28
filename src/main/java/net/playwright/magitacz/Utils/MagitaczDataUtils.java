package net.playwright.magitacz.Utils;

import com.mojang.brigadier.Message;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.modifier.JsonProperty;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.pojo.data.attachment.AttachmentData;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.apoth.affix.SpellAffix;

import java.util.ArrayList;
import java.util.List;

public class MagitaczDataUtils {


    public static AbstractSpell getSpellOnAffix( SpellAffix spellAffix) {
        String spellName = spellAffix.getSpell();
        ResourceLocation spellId = new ResourceLocation(IronsSpellbooks.MODID, spellName);
        return SpellRegistry.getSpell(spellId);
    }


    public static Component getManaCostComponent(AbstractSpell spell, float manaCostReductionValue, int level){
        Component manaCostComponent;

        if (manaCostReductionValue == 1.0f) return Component.literal(String.format("Base Casting Shot Mana Cost : %s Mana",(spell.getManaCost(level)))).withStyle(ChatFormatting.AQUA);

        float newManaCost =  spell.getManaCost(level) / manaCostReductionValue;



        if (!Screen.hasShiftDown()) {
            manaCostComponent = Component.literal(String.format("Casting Shot Mana Cost : %.0f Mana",newManaCost)).withStyle(ChatFormatting.AQUA);
        }
        else {
            Component manaCostReductionPercentComponent;
            if  (manaCostReductionValue > 1) {
                float percent = (manaCostReductionValue - 1f) * 100f;
                String formattedManaCostReductionValue = String.format("%.0f%%", percent);
                manaCostReductionPercentComponent = Component.translatable(String.format("- %s%%", formattedManaCostReductionValue)).withStyle(ChatFormatting.BLUE);
            }
            else {
                float percent = (1f - manaCostReductionValue) * 100f;
                String formattedManaCostReductionValue = String.format("%.0f%%", percent);
                manaCostReductionPercentComponent = Component.literal(String.format("+ %s%%", formattedManaCostReductionValue)).withStyle(ChatFormatting.RED);
            }
            var equation = Component.translatable("%s %s", spell.getManaCost(level), manaCostReductionPercentComponent).withStyle(ChatFormatting.DARK_AQUA);
            manaCostComponent = Component.translatable("Casting Shot Mana Cost : %s = %s Mana", equation, String.format("%.0f",newManaCost)).withStyle(ChatFormatting.AQUA);
        }
        return manaCostComponent;
    }


    public static Component getAmplifiedCastParameter(int cast_type_parameter, String castType, float castShotAmplifier) {

        // If no amplification, just show the original value
        if (castShotAmplifier == 1.0f) {
            return Component.literal(String.valueOf(cast_type_parameter)).withStyle(ChatFormatting.GOLD);
        }

        // Shift is held: try to apply a special cast-type adjustment
        SpellAffix.CastType type;
        try {
            type = SpellAffix.CastType.valueOf(castType);
        } catch (IllegalArgumentException e) {
            // Unknown cast type; fall back to original value
            return Component.literal(String.valueOf(cast_type_parameter)).withStyle(ChatFormatting.YELLOW);
        }




        // If Shift is not held, show the new value only
        if (!Screen.hasShiftDown()) {
            int newCastParameterValue = Math.round(cast_type_parameter / castShotAmplifier);

            if (type == SpellAffix.CastType.ONKILL || type == SpellAffix.CastType.ONHIT || type == SpellAffix.CastType.ONSHOOT)  newCastParameterValue = Math.round(cast_type_parameter / castShotAmplifier);
            else newCastParameterValue = Math.round(cast_type_parameter * castShotAmplifier);

            return Component.literal(String.valueOf(newCastParameterValue)).withStyle(ChatFormatting.GOLD);
        }




        if (type == SpellAffix.CastType.ONKILL || type == SpellAffix.CastType.ONHIT || type == SpellAffix.CastType.ONSHOOT) {

            Component additionSubtractionComponent;
            if (cast_type_parameter > 1) {
                float percent = (castShotAmplifier - 1f) * 100f;
                String formatted = String.format("- %.0f%%", percent);
                additionSubtractionComponent = Component.translatable(formatted).withStyle(ChatFormatting.BLUE);
            } else {
                float percent = (1f - castShotAmplifier) * 100f;
                String formatted = String.format("+ %.0f%%", percent);
                additionSubtractionComponent = Component.literal(formatted).withStyle(ChatFormatting.RED);
            }

            int newCastParameterValue = Math.round(cast_type_parameter / castShotAmplifier);
            Component equation = Component.translatable("%s %s", cast_type_parameter, additionSubtractionComponent).withStyle(ChatFormatting.YELLOW);
            return Component.translatable("%s = %s", equation, String.valueOf(newCastParameterValue)).withStyle(ChatFormatting.GOLD);
        } else {
            int newCastParameterValue = Math.round(cast_type_parameter * castShotAmplifier);

            Component additionSubtractionComponent;
            if (cast_type_parameter > 1) {
                float percent = (castShotAmplifier - 1f) * 100f;
                String formatted = String.format("+ %.0f%%", percent);
                additionSubtractionComponent = Component.translatable(formatted).withStyle(ChatFormatting.BLUE);
            } else {
                float percent = (1f - castShotAmplifier) * 100f;
                String formatted = String.format("- %.0f%%", percent);
                additionSubtractionComponent = Component.literal(formatted).withStyle(ChatFormatting.RED);
            }

            Component equation = Component.translatable("%s %s", cast_type_parameter, additionSubtractionComponent).withStyle(ChatFormatting.YELLOW);

            return Component.translatable("%s = %s", equation, String.valueOf(newCastParameterValue)).withStyle(ChatFormatting.GOLD);
        }
    }

    public static Component getSpellComponent(AbstractSpell spell, String castType, int cast_type_parameter, boolean singular, int level,  float castShotAmplifier) {

        int spellColor = MagitaczDataUtils.getSchoolColor(spell.getSchoolType().getDisplayName().getString().toLowerCase());

        var spellNameComponent = Component.translatable(I18n.get(("spell." + IronsSpellbooks.MODID + "." + spell.getSpellName())));
        var levelComponent = Component.literal(String.valueOf(level));





        spellNameComponent.append(Component.literal(" Lvl. "));
        spellNameComponent.append(levelComponent);
        spellNameComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(spellColor)).withUnderlined(true));

        Component newCastParameterComponent = getAmplifiedCastParameter(cast_type_parameter, castType, castShotAmplifier);

        Component component;

        switch (SpellAffix.CastType.valueOf(castType)) {
            case ONHIT -> {

                if (singular) {
                    var onHitComponent = Component.translatable("Lands").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                    return (Component.translatable("guns.magitacz:spell_attachment_on_hit_singular", spellNameComponent, onHitComponent));

                } else {
                    var onHitComponent = Component.translatable("Land").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_hit", spellNameComponent, newCastParameterComponent , onHitComponent));

                }

            }

            case ONKILL -> {
                if (singular) {
                    var onKillComponent = Component.translatable("Takedown").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_kill_singular", spellNameComponent, onKillComponent));
                } else {
                    var onKillComponent = Component.translatable("Taken Down").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_kill", spellNameComponent, newCastParameterComponent, onKillComponent));
                }
            }
            case ONSHOOT -> {
                if (singular) {
                    var onShootComponent = Component.translatable("Fired").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_shoot_singular", spellNameComponent, onShootComponent));

                }

                var onShootComponent = Component.translatable("Shot").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                component = (Component.translatable("guns.magitacz:spell_attachment_on_shoot", spellNameComponent, newCastParameterComponent, onShootComponent));

            }

            case ONHITCHANCE -> {
                var onHitComponent = Component.translatable("Landing a shot").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                component = Component.translatable("guns.magitacz:spell_attachment_on_hit_chance", onHitComponent,newCastParameterComponent, spellNameComponent);
            }

            case ONKILLCHANCE -> {
                var onKillComponent = Component.translatable("Landing a killing shot").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                component = Component.translatable("guns.magitacz:spell_attachment_on_kill_chance",onKillComponent,newCastParameterComponent, spellNameComponent);
            }

            case ONSHOOTCHANCE -> {
                var onShootComponenet = Component.translatable("Firing").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                component = Component.translatable("guns.magitacz:spell_attachment_on_shoot_chance",onShootComponenet,newCastParameterComponent, spellNameComponent);
            }

            default -> {
                component = Component.literal("BROKEN");
            }
        }

//        component = Component.translatable("%s\n\n", component, manaCostComponent);

        return component;
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

    public static int getSchoolColor(String element) {
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
}
