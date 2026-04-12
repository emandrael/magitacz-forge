package net.playwright.magitacz.events.hardcore_revival;

import com.tacz.guns.api.event.common.GunFireEvent;
import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.blay09.mods.hardcorerevival.HardcoreRevivalManager;
import net.blay09.mods.hardcorerevival.capability.HardcoreRevivalData;
import net.blay09.mods.hardcorerevival.capability.HardcoreRevivalDataImpl;
import net.blay09.mods.hardcorerevival.client.HardcoreRevivalClient;
import net.blay09.mods.hardcorerevival.config.HardcoreRevivalConfig;
import net.blay09.mods.hardcorerevival.config.HardcoreRevivalConfigData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.Config;
import net.playwright.magitacz.MagitaczMod;

public class LostTimeOnShotWhenDown {

    @SubscribeEvent
    public static void  onShoot(GunFireEvent event) {
        if (ModList.get().isLoaded("hardcorerevival")) {
            LivingEntity shooter = event.getShooter();
            if (shooter instanceof ServerPlayer player) {
                HardcoreRevivalData revivalData = HardcoreRevival.getRevivalData(player);

                if (revivalData.isKnockedOut()) {

                    HardcoreRevivalConfigData config = HardcoreRevivalConfig.getActive();

                    int ticksTimeLeft =  config.ticksUntilDeath - revivalData.getKnockoutTicksPassed();

                    if (ticksTimeLeft >= Config.HARDCORE_REVIVAL_STOP_REDUCTION_AT_TICKS.get()) {

                        var new_time_Left =
                                (revivalData.getKnockoutTicksPassed() +
                                        Config.HARDCORE_REVIVAL_SHOT_REDUCTION_TIME.get());

                        revivalData.setKnockoutTicksPassed(new_time_Left);
                        HardcoreRevival.getClientRevivalData().setKnockoutTicksPassed(new_time_Left);

                    }

                }
            }
        }
    }
}
