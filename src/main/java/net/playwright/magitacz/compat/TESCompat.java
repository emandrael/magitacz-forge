package net.playwright.magitacz.compat;

import net.minecraft.resources.ResourceLocation;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.hud.DamageTypeHud;
import net.tslat.tes.api.TESAPI;

public class TESCompat {
    public static void registerHUD() {
        ResourceLocation hudID =  new ResourceLocation(MagitaczMod.MODID, "damage_type_hud");
        TESAPI.addTESHudElement(hudID,new DamageTypeHud());
    }

}
