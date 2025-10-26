package net.playwright.magitacz.apoth.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.AbstractValuedAffix;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.GunAffix;
import mod.chloeprime.apotheosismodernragnarok.common.gunpack.GunApothData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.Utils.MagitaczTooltipUtils;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;

import java.util.Map;
import java.util.Set;

public class SpellAffix extends AbstractValuedAffix implements GunAffix {

    public static final Codec<SpellAffix> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    LootCategory.SET_CODEC.fieldOf("types").forGetter(AbstractValuedAffix::getApplicableCategories),
                    GemBonus.VALUES_CODEC.fieldOf("values").forGetter(AbstractValuedAffix::getValues),
                    Codec.STRING.fieldOf("spell").forGetter(a -> a.spell),
                    Codec.STRING.optionalFieldOf("exclusive_group", "").forGetter(a -> a.exclusiveGroup)
            ).apply(inst, SpellAffix::new));

    protected final String spell;
    protected final String exclusiveGroup;

    public SpellAffix(Set<LootCategory> categories, Map<LootRarity, StepFunction> values, String spell, String exclusiveGroup) {
        super(AffixType.ABILITY, categories, values);
        this.spell = spell;
        this.exclusiveGroup = exclusiveGroup;
    }

    public String getSpell() {
        return spell;
    }


    public boolean canApplyTo(ItemStack stack, LootCategory category, LootRarity rarity) {
        if (category == LootCategory.NONE) {
            return false;
        } else {
            boolean isInBlacklist = GunApothData.of(stack).filter((apoth) -> apoth.getDisabledAffixes().contains(AffixRegistry.INSTANCE.getKey(this))).isPresent();
            if (isInBlacklist) {
                return false;
            } else {
                Set<LootCategory> validTypes = this.getApplicableCategories();
                return validTypes.isEmpty() || validTypes.contains(category);
            }
        }
    }


    @Override
    public Component getAugmentingText(ItemStack stack, LootRarity rarity, float level) {
        double value = getValue(stack, rarity, level);
        double min = getValue(stack, rarity, 0.0F);
        double max = getValue(stack, rarity, 1.0F);
        return Component.translatable("affix.magitacz.elemental_bullet.augment",
                        value, min, max, spell)
                .withStyle(s -> s.withColor(0xFF8000));
    }

    @Override
    public MutableComponent getDescription(ItemStack stack, LootRarity rarity, float level) {

        ItemStack gunItemStack = stack;

        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItemStack);

        CommonGunIndex gunIndex = api.getGunIndex();


        if (gunItemStack.getItem() instanceof ModernKineticGunItem gunItem && gunIndex != null) {

            GunData gunData = api.getGunIndex().getGunData();

            AttachedSpell attachedSpell = MagitaczDataUtils.getAttachmentSpellData(gunItemStack, gunData, AttachmentType.MUZZLE);

            Component component = Component.literal(this.spell);

            if (component != null) {
                return (MutableComponent) component;
            }
        }
        return Component.literal(String.format("%d", (long) getValue(stack, rarity, level)));
    }


    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }
}
