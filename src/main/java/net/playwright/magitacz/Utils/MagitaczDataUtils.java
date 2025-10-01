package net.playwright.magitacz.Utils;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.modifier.JsonProperty;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.pojo.data.attachment.AttachmentData;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;
import net.playwright.magitacz.attachment_modifiers.SpellModifier;

import java.util.ArrayList;
import java.util.List;

public class MagitaczDataUtils {

    public static AbstractSpell getSpellOnAttachment(ItemStack gunItem, GunData gunData, AttachmentType attachmentType) {
        AttachedSpell attachedSpell = getAttachmentSpellData(gunItem,gunData,attachmentType);

        if (attachedSpell == null) return null;

        String spellName = attachedSpell.getSpellName();
        AttachedSpell.SpellRegistryEnum spellRegistryEnum = attachedSpell.getSpellRegistry();

        ResourceLocation spellId = new ResourceLocation(spellRegistryEnum.registryName, spellName);
        return SpellRegistry.getSpell(spellId);
    }


    public static AttachedSpell getAttachmentSpellData(ItemStack gunItem, GunData gunData, AttachmentType attachmentType) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        AttachedSpell attachedSpell = null;
        if (iGun == null) {
            return new AttachedSpell();
        }
        else{
            ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, attachmentType);
            if (!DefaultAssets.isEmptyAttachmentId(attachmentId)) {
                AttachmentData attachmentData = (AttachmentData)gunData.getExclusiveAttachments().get(attachmentId);
                if (attachmentData != null) {
                    JsonProperty<?> m = attachmentData.getModifier().get(SpellModifier.ID);
                    if (m != null) {
                        Object spell = m.getValue();
                        if (spell instanceof AttachedSpell) {
                            attachedSpell =  (AttachedSpell) spell;
                        }
                    }
                } else {
                    CommonAttachmentIndex index = (CommonAttachmentIndex) TimelessAPI.getCommonAttachmentIndex(attachmentId).orElse(null);
                    if (index != null) {
                        JsonProperty<?> m = (JsonProperty)index.getData().getModifier().get(SpellModifier.ID);
                        if (m != null) {
                            Object spell = m.getValue();
                            if (spell instanceof AttachedSpell modifier) {
                                attachedSpell =  (AttachedSpell) spell;
                            }
                        }
                    }
                }
            }
            return attachedSpell;
        }
    }

    public static List<AttachedSpell> getAttachedSpells(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        List<AttachedSpell> attachedSpells = new ArrayList<>();


        if (iGun == null) {
            return new ArrayList<AttachedSpell>();
        } else {

            for(AttachmentType type : AttachmentType.values()) {
                ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, type);
                if (!DefaultAssets.isEmptyAttachmentId(attachmentId)) {
                    AttachmentData attachmentData = (AttachmentData)gunData.getExclusiveAttachments().get(attachmentId);
                    if (attachmentData != null) {
                        JsonProperty<?> m = attachmentData.getModifier().get(SpellModifier.ID);
                        if (m != null) {
                            Object spell = m.getValue();
                            if (spell instanceof AttachedSpell attachedSpell) {
                                attachedSpells.add(attachedSpell);
                            }
                        }
                    } else {
                        CommonAttachmentIndex index = (CommonAttachmentIndex) TimelessAPI.getCommonAttachmentIndex(attachmentId).orElse(null);
                        if (index != null) {
                            JsonProperty<?> m = (JsonProperty)index.getData().getModifier().get(SpellModifier.ID);
                            if (m != null) {
                                Object spell = m.getValue();
                                if (spell instanceof AttachedSpell modifier) {
                                    attachedSpells.add(modifier);
                                }
                            }
                        }
                    }
                }
            }

            return attachedSpells;
        }

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
