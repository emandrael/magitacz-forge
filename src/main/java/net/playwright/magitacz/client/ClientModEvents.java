package net.playwright.magitacz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.playwright.magitacz.MagitaczMod;
import net.tslat.tes.api.TESAPI;

import static net.playwright.magitacz.MagitaczMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("tslatentitystatus")) {
            event.enqueueWork(() -> {
                ResourceLocation hudId = new ResourceLocation(MagitaczMod.MODID, "damage_type_hud");
                TESAPI.addTESHudElement(hudId, new net.playwright.magitacz.hud.DamageTypeHud());
            });
        }
    }
}