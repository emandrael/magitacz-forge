package net.playwright.magitacz.events.hardcore_revival;

import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.blay09.mods.hardcorerevival.capability.HardcoreRevivalData;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
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

//    static final Item melon = Items.MELON_SEEDS;


    @SubscribeEvent
    public static void onEntityKill(EntityKillByGunEvent event) {


        if (ModList.get().isLoaded("hardcorerevival")) {
            Player player = (Player) event.getAttacker();

            HardcoreRevivalData playerData =  HardcoreRevival.getManager().getRevivalData(player);

            if (playerData.isKnockedOut()) {


                if (player instanceof ServerPlayer sp) {
                    Minecraft.getInstance().execute(() ->
                            Minecraft.getInstance().gameRenderer.displayItemActivation(Items.MELON_SLICE.getDefaultInstance()));


                    ServerLevel serverLevel = (ServerLevel) player.level();

                    Vec3 pos = player.position(); // or any location you want
                    serverLevel.sendParticles(
                            ParticleTypes.HEART, // particle type
                            pos.x, pos.y + 1.0D, pos.z,     // center position
                            32,                             // count
                            0.5D, 0.5D, 0.5D,               // spread on X/Y/Z
                            0.0D                            // speed (motion magnitude)
                    );

                    HardcoreRevival.getManager().wakeup(player, true);


                }


            }



        }



    }


}
