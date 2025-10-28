package net.playwright.magitacz.apoth.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.apotheosis.adventure.affix.*;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import dev.shadowsoffire.placebo.util.StepFunction;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.AbstractValuedAffix;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.GunAffix;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.apoth.category.AttachmentLootCategories;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SpellAffix extends AbstractValuedAffix implements GunAffix {



    public static final Codec<SpellAffix> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    LootCategory.SET_CODEC.fieldOf("types").forGetter(AbstractValuedAffix::getApplicableCategories),
                    GemBonus.VALUES_CODEC.fieldOf("levels").forGetter(AbstractValuedAffix::getValues),
                    Codec.INT.fieldOf("cast_parameter").forGetter(a-> a.castParameter),
                    Codec.STRING.fieldOf("spell").forGetter(a -> a.spell),
                    Codec.STRING.fieldOf("cast_type").forGetter(a -> a.castType),
                    Codec.STRING.optionalFieldOf("exclusive_group", "all_spells").forGetter(a -> a.exclusiveGroup)
            ).apply(inst, SpellAffix::new));

    protected final int castParameter;
    protected final String spell;
    protected final String exclusiveGroup;
    protected final String castType;


    public SpellAffix(Set<LootCategory> categories, Map<LootRarity, StepFunction> values, int castParameter, String spell , String castType, String exclusiveGroup) {
        super(AffixType.ABILITY, categories, values);
        this.castParameter = castParameter;
        this.spell = spell;
        this.exclusiveGroup = exclusiveGroup;
        this.castType = castType;
    }


    public final double getSpellLevel(ItemStack gun, AffixInstance instance) {
        return this.getValue(gun, (LootRarity)instance.rarity().get(), instance.level());
    }


    public int getSpellLevel(ItemStack attachment, LootRarity rarity, float level) {
        return (int) Math.round(getValue(attachment,rarity, level));
    }

    public String getSpell() {
        return spell;
    }

    public CastType getCastType() {
        return CastType.valueOf(castType);
    }

    public int getCastParameter() {
        return this.castParameter;
    }



    public String getExclusiveGroup() {
        return exclusiveGroup;
    }


    @Override
    public boolean canApplyTo(ItemStack attachment, LootCategory category, LootRarity rarity) {
        // base type validation
        if (category == LootCategory.NONE) return false;
        Set<LootCategory> validTypes = AttachmentLootCategories.all();
        if (!validTypes.isEmpty() && !validTypes.contains(category)) return false;

        return true;
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
    public MutableComponent getDescription(ItemStack attachment, LootRarity rarity, float level) {



        int cast_type_parameter = (int) getCastParameter();

        boolean singular = cast_type_parameter == 1;

        ResourceLocation spellId = new ResourceLocation(IronsSpellbooks.MODID, spell);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);



        var mutableComponent = Component.literal("");

        var component = MagitaczDataUtils.getSpellComponent(spell, getCastType().name(),cast_type_parameter,singular,getSpellLevel(attachment,rarity,level));

        Component shift_component;

        if (Screen.hasShiftDown()){
            //TODO Change Shift to show calculations?
            shift_component = Component.translatable("magitacz.tooltip.show_extra_information").withStyle(ChatFormatting.GOLD);
        }
        else {
            shift_component = Component.translatable("magitacz.tooltip.hide_extra_information").withStyle(ChatFormatting.YELLOW);
        }

        Component cost = Component.translatable("magitacz.tooltip.mana_cost", spell.getManaCost((int) getSpellLevel(attachment,rarity,level))).withStyle(ChatFormatting.AQUA);


        mutableComponent.append(Component.literal("Spell Attachment")
                .withStyle(ChatFormatting.GREEN));
        mutableComponent.append(Component.literal(" • \n"));
        mutableComponent.append(component);
        mutableComponent.append(Component.literal("\n\n"));
        mutableComponent.append(cost);
        mutableComponent.append(Component.literal("\n"));




        return mutableComponent;
    }




    @Override
    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }

    public static enum CastType {
        ONHIT,
        ONHITCHANCE,
        ONKILL,
        ONKILLCHANCE,
        ONSHOOT,
        ONSHOOTCHANCE,
    }
}

