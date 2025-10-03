package net.playwright.magitacz.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.DamageTypeUtils;
import net.tslat.tes.api.TESAPI;
import net.tslat.tes.api.TESHudElement;
import net.tslat.tes.api.util.TESClientUtil;
import net.tslat.tes.api.util.TESUtil;

public class DamageTypeHud implements TESHudElement {

    private static final String spellIconLoc = "textures/gui/spell_icons/";

    private static final ResourceLocation FIRE_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "fireball.png");
    private static final ResourceLocation ICE_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "ice_block.png");
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "lightning_bolt.png");
    private static final ResourceLocation NATURE_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "stomp.png");
    private static final ResourceLocation EVOCATION_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "lob_creeper.png");
    private static final ResourceLocation HOLY_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "angel_wing.png");
    private static final ResourceLocation BLOOD_TEXTURE = new ResourceLocation("irons_spellbooks", spellIconLoc + "devour.png");

    private static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation("magitacz", "textures/gui/shield.png");
    private static final ResourceLocation SKULL_TEXTURE = new ResourceLocation("magitacz", "textures/gui/skull.png");



    @Override
    public int render(GuiGraphics guiGraphics, Minecraft minecraft, float partialTick, LivingEntity livingEntity, float opacity, boolean inWorldHud) {
        if (inWorldHud) {
            if (!TESAPI.getConfig().inWorldHudEntityIcons()) {
                return 0;
            }
        } else if (!TESAPI.getConfig().hudEntityIcons()) {
            return 0;
        }

        int x = 0;



        TESClientUtil.prepRenderForTexture(FIRE_TEXTURE);
        if (DamageTypeUtils.fireSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        } else if (DamageTypeUtils.fireSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        TESClientUtil.prepRenderForTexture(ICE_TEXTURE);
        if (DamageTypeUtils.iceSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);

            x += 20;
        } else if (DamageTypeUtils.iceSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        TESClientUtil.prepRenderForTexture(LIGHTNING_TEXTURE);
        if (DamageTypeUtils.lightningSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }else if (DamageTypeUtils.lightningSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        TESClientUtil.prepRenderForTexture(HOLY_TEXTURE);
        if (DamageTypeUtils.lightSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }else if (DamageTypeUtils.lightSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        TESClientUtil.prepRenderForTexture(EVOCATION_TEXTURE);
        if (DamageTypeUtils.evocationSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }else if (DamageTypeUtils.evocationSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        TESClientUtil.prepRenderForTexture(NATURE_TEXTURE);
        if (DamageTypeUtils.natureSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }else if (DamageTypeUtils.natureSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }


        TESClientUtil.prepRenderForTexture(BLOOD_TEXTURE);
        if (DamageTypeUtils.bloodSpellResistOf(livingEntity) > 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SHIELD_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }else if (DamageTypeUtils.bloodSpellResistOf(livingEntity) < 1) {
            TESClientUtil.drawSimpleTexture(guiGraphics, x, 0, 16, 16, 0.0F, 0.0F, 16);
            TESClientUtil.prepRenderForTexture(SKULL_TEXTURE);
            TESClientUtil.drawSimpleTexture(guiGraphics, x + 10, 10, 8, 8, 0.0F, 0.0F, 8);
            x += 20;
        }

        return x == 0 ? 0 : 8;

    }
}
