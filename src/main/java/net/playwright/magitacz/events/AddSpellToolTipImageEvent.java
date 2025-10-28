package net.playwright.magitacz.events;


import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.init.ModAttributes;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.apoth.affix.SpellAffix;
import net.playwright.magitacz.attributes.MagitaczAttributes;

import javax.print.attribute.Attribute;
import java.util.Map;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;


@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AddSpellToolTipImageEvent {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack gunItemStack = event.getItemStack();

        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItemStack);



        Player shooter = event.getEntity();;

        IGun iGun = IGun.getIGunOrNull(gunItemStack);

        if (iGun == null) return;

        for (AttachmentType type : AttachmentType.values())
        {



            var attachment = iGun.getAttachment(gunItemStack,type);



            if (attachment.hasTag()) {
                Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(attachment);
                affixes.forEach((afx, inst) -> {

                    if ((inst.affix().get() instanceof SpellAffix spellAffix)) {

                        AbstractSpell spell = MagitaczDataUtils.getSpellOnAffix(spellAffix);

                        AttributeInstance spell_shot_reduction = shooter.getAttribute(MagitaczAttributes.CASTING_SHOT_AMPLIFICATION.get());
                        var spell_shot_reduction_value = spell_shot_reduction.getValue();

                        AttributeInstance mana_cost_reduction = shooter.getAttribute(MagitaczAttributes.MANA_COST_REDUCTION.get());
                        var mana_cost_reduction_value = mana_cost_reduction.getValue();

                        int player_cast_type_parameter;
                        boolean singular = false;

                        switch (spellAffix.getCastType()){
                            case ONHIT -> {
                                player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() / spell_shot_reduction_value);
                                singular = player_cast_type_parameter == 1;
                            }
                            case ONSHOOT -> {
                                player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() / spell_shot_reduction_value);
                                singular = player_cast_type_parameter == 1;
                            }
                            case ONKILL -> {
                                player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() / spell_shot_reduction_value);
                                singular = player_cast_type_parameter == 1;
                            }
                            case ONHITCHANCE -> player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() * spell_shot_reduction_value);
                            case ONSHOOTCHANCE -> player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() * spell_shot_reduction_value);
                            case ONKILLCHANCE -> player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() * spell_shot_reduction_value);
                            default -> player_cast_type_parameter = 0;
                        }



                        var attachment_heading_string = type.name().toLowerCase();
                        var first_letter_cap = attachment_heading_string.substring(0,1).toUpperCase();

                        Component bullet_point = Component.translatable("magitacz.tooltip.bullet_point").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
                        Component attachment_heading_component = Component.translatable("magitacz.tooltip.attachment_heading", (first_letter_cap + attachment_heading_string.substring(1))).withStyle(ChatFormatting.GREEN);

                        Component full_heading_component = Component.empty().append(bullet_point).append(attachment_heading_component).append(bullet_point);

                        Component manaCost = MagitaczDataUtils.getManaCostComponent(spell, (float) mana_cost_reduction_value, (int) spellAffix.getSpellLevel(attachment,inst));


                        Component affix_spell_component;

                        affix_spell_component = MagitaczDataUtils.getSpellComponent(
                                spell,
                                spellAffix.getCastType().name(),
                                spellAffix.getCastParameter(),
                                singular,
                                (int) spellAffix.getSpellLevel(attachment,inst),
                                (float) spell_shot_reduction_value);




                        if (affix_spell_component != null) {

                            event.getToolTip().add(1,full_heading_component);
                            event.getToolTip().add(2,affix_spell_component);
                            event.getToolTip().add(2,Component.empty());
                            event.getToolTip().add(4,manaCost);

                        }

                    }


                });
            }


        }


    }
}
