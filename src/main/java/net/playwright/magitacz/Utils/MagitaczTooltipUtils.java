package net.playwright.magitacz.Utils;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;

public class MagitaczTooltipUtils {

    public static Component getGunAttachmentTooltip(AttachedSpell attachedSpell) {

        if (attachedSpell != null) {
            ResourceLocation spellId = new ResourceLocation(attachedSpell.getSpellRegistry().registryName, attachedSpell.getSpellName());
            AbstractSpell spell = SpellRegistry.getSpell(spellId);
            int spellColor = MagitaczDataUtils.getSchoolColor(spell.getSchoolType().getDisplayName().getString().toLowerCase());

            int per_x = 1;
            if (attachedSpell.getCastTypeParameters().get("per_x") != null) {
                per_x = attachedSpell.getCastTypeParameters().get("per_x").intValue();
            }

            if (per_x <= 0) {
                return (Component.literal("cast_type_parameters/per_x are not configured correctly. please check config").withStyle(ChatFormatting.RED));
            }


            boolean singular = per_x == 1;

            var spellNameComponent = Component.translatable(I18n.get(("spell." + attachedSpell.getSpellRegistry().registryName + "." + attachedSpell.getSpellName())))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(spellColor)).withUnderlined(true).withBold(true));


            switch (attachedSpell.getCastType()) {
                case ONHIT -> {

                    if (singular) {
                        var onHitComponent = Component.translatable("lands").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                        return (Component.translatable("guns.magitacz:spell_attachment_on_hit_singular", spellNameComponent, onHitComponent));

                    } else {
                        var onHitComponent = Component.translatable("land").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.UNDERLINE);
                        return (Component.translatable("guns.magitacz:spell_attachment_on_hit", spellNameComponent, attachedSpell.getCastTypeParameters().get("per_x").intValue(), onHitComponent));

                    }

                }

                case ONKILL -> {
                    if (singular) {
                        var onKillComponent = Component.translatable("takedown").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                        return (Component.translatable("guns.magitacz:spell_attachment_on_kill_singular", spellNameComponent, onKillComponent));
                    } else {
                        var onKillComponent = Component.translatable("taken down").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE);
                        return (Component.translatable("guns.magitacz:spell_attachment_on_kill", spellNameComponent, attachedSpell.getCastTypeParameters().get("per_x").intValue(), onKillComponent));
                    }
                }
                case ONSHOOT -> {
                    if (singular) {
                        var onShootComponent = Component.translatable("fired").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                        return (Component.translatable("guns.magitacz:spell_attachment_on_shoot_singular", spellNameComponent, onShootComponent));

                    }

                    var onShootComponent = Component.translatable("hit").withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.UNDERLINE);
                    return (Component.translatable("guns.magitacz:spell_attachment_on_shoot", spellNameComponent, attachedSpell.getCastTypeParameters().get("per_x").intValue(), onShootComponent));

                }

                case ONCHANCE -> {

                }
            }
        }
        return null;
    }
}
