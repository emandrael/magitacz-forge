package net.playwright.magitacz.apoth.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixType;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.util.StepFunction;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.AbstractValuedAffix;
import mod.chloeprime.apotheosismodernragnarok.common.affix.framework.GunAffix;
import mod.chloeprime.apotheosismodernragnarok.common.gunpack.GunApothData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.Utils.MagitaczTooltipUtils;
import net.playwright.magitacz.apoth.category.ExtraLootCategories;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static net.playwright.magitacz.apoth.affix.SpellAffix.CastType.ONHIT;
import static net.playwright.magitacz.apoth.affix.SpellAffix.CastType.ONKILL;

public class SpellAffix extends AbstractValuedAffix implements GunAffix {


    double randomiser(long seed) {
        Random generator = new Random(seed);
        return generator.nextFloat();
    }

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
    public boolean canApplyTo(ItemStack stack, LootCategory category, LootRarity rarity) {
        if (category == LootCategory.NONE) {
            return false;
        } else {
            Set<LootCategory> validTypes = ExtraLootCategories.all();
            return validTypes.isEmpty() || validTypes.contains(category);
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
    public MutableComponent getDescription(ItemStack attachment, LootRarity rarity, float level) {

        int per_x = (int) getCastParameter();

        boolean singular = per_x == 1;

        ResourceLocation spellId = new ResourceLocation(IronsSpellbooks.MODID, spell);
        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        int spellColor = MagitaczDataUtils.getSchoolColor(spell.getSchoolType().getDisplayName().getString().toLowerCase());

        var spellNameComponent = Component.translatable(I18n.get(("spell." + IronsSpellbooks.MODID + "." + spell.getSpellName())))
                ;
        var levelComponent = Component.literal(String.valueOf(getSpellLevel(attachment,rarity,level)));

        spellNameComponent.append(Component.literal(" Lvl. "));
        spellNameComponent.append(levelComponent);
        spellNameComponent.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(spellColor)).withUnderlined(true).withBold(true));

        Component component;


        switch (CastType.valueOf(castType)) {
            case ONHIT -> {

                if (singular) {
                    var onHitComponent = Component.translatable("Lands").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                    return (Component.translatable("guns.magitacz:spell_attachment_on_hit_singular", spellNameComponent, onHitComponent));

                } else {
                    var onHitComponent = Component.translatable("Land").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_hit", spellNameComponent, getCastParameter(), onHitComponent));

                }

            }

            case ONKILL -> {
                if (singular) {
                    var onKillComponent = Component.translatable("Takedown").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_kill_singular", spellNameComponent, onKillComponent));
                } else {
                    var onKillComponent = Component.translatable("Taken Down").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_kill", spellNameComponent, getCastParameter(), onKillComponent));
                }
            }
            case ONSHOOT -> {
                if (singular) {
                    var onShootComponent = Component.translatable("Fired").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                    component = (Component.translatable("guns.magitacz:spell_attachment_on_shoot_singular", spellNameComponent, onShootComponent));

                }

                var onShootComponent = Component.translatable("Shot").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                component = (Component.translatable("guns.magitacz:spell_attachment_on_shoot", spellNameComponent, getCastParameter(), onShootComponent));

            }

            case ONHITCHANCE -> {
                component = Component.translatable("guns.magitacz:spell_attachment_on_chance", spellNameComponent, getCastParameter());
            }

            case ONKILLCHANCE -> {
                component = Component.translatable("guns.magitacz:spell_attachment_on_chance", spellNameComponent, getCastParameter());
            }

            case ONSHOOTCHANCE -> {
                component = Component.translatable("guns.magitacz:spell_attachment_on_chance", spellNameComponent, getCastParameter());
            }

            default -> {
                component = Component.literal("");
            }
        }
        var mutableComponent = Component.literal("");

        mutableComponent.append(Component.literal("Spell Attachement")
                .withStyle(ChatFormatting.GREEN));
        mutableComponent.append(Component.literal(" • \n"));
        mutableComponent.append(component);
        mutableComponent.append(Component.literal(""));

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

