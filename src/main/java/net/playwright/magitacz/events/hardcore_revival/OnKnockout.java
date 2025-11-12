package net.playwright.magitacz.events.hardcore_revival;

import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.blay09.mods.hardcorerevival.api.PlayerKnockedOutEvent;
import net.blay09.mods.hardcorerevival.capability.HardcoreRevivalData;
import net.blay09.mods.hardcorerevival.config.HardcoreRevivalConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OnKnockout {

    private static final String TAG_CURRENT_REMAINING = "magitacz_current_knockout_remaining";
    private static final String TAG_KO_PROCESSED     = "magitacz_knockout_processed";
    private static final int MIN_REMAINING_TICKS     = 5 * 20; // 5 seconds

    private static int getDefaultKnockoutTicks() {
        return HardcoreRevivalConfig.getActive().ticksUntilDeath;
    }

    @SubscribeEvent
    public static void onKnockout(PlayerKnockedOutEvent event) {

        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        CompoundTag persistent = player.getPersistentData();

        HardcoreRevivalData data = HardcoreRevival.getManager().getRevivalData(player);

        int totalTicks = getDefaultKnockoutTicks();
        int minRemaining = Math.min(MIN_REMAINING_TICKS, totalTicks);

        int previousRemaining = persistent.contains(TAG_CURRENT_REMAINING)
                ? persistent.getInt(TAG_CURRENT_REMAINING)
                : totalTicks;

        int newRemaining = Math.max(minRemaining, previousRemaining / 2);
        int halved = Math.max(totalTicks - newRemaining, 0);


        data.setKnockoutTicksPassed(halved);

        persistent.putInt(TAG_CURRENT_REMAINING, newRemaining);
        MagitaczMod.LOGGER.info(String.format("Remaining ticks: %d", newRemaining));
        persistent.putBoolean(TAG_KO_PROCESSED, true);
    }


    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        CompoundTag persistent = player.getPersistentData();
        persistent.remove(TAG_KO_PROCESSED);
        persistent.remove(TAG_CURRENT_REMAINING);

    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        event.getEntity().getPersistentData().remove(TAG_KO_PROCESSED);
        event.getEntity().getPersistentData().remove(TAG_CURRENT_REMAINING);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        event.getEntity().getPersistentData().remove(TAG_KO_PROCESSED);
        event.getEntity().getPersistentData().remove(TAG_CURRENT_REMAINING);
    }
}