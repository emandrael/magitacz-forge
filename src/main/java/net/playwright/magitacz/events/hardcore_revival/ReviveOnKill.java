package net.playwright.magitacz.events.hardcore_revival;

import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.blay09.mods.hardcorerevival.capability.HardcoreRevivalData;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ReviveOnKill {

    @SubscribeEvent
    public static void onSleep(PlayerWakeUpEvent event) {
        MagitaczMod.LOGGER.info("AWOKEN");
    }

    @SubscribeEvent
    public static void onEntityKill(EntityKillByGunEvent event) {
        if (ModList.get().isLoaded("hardcorerevival")) {
            Player player = (Player) event.getAttacker();

            HardcoreRevivalData playerData =  HardcoreRevival.getManager().getRevivalData(player);

            if (playerData.isKnockedOut()) {
                HardcoreRevival.getManager().wakeup(player, true);


                if (player instanceof ServerPlayer sp) {
                    sp.level().broadcastEntityEvent(sp, (byte)35); // triggers overlay + particles on clients
                    sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
                            SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }

            if (player.level().isClientSide && player == Minecraft.getInstance().player) {
                Minecraft.getInstance().execute(() ->
                        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(Items.MELON_SLICE))
                );
            }

        }



    }


}
