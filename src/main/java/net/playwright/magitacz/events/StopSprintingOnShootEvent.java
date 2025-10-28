package net.playwright.magitacz.events;


import com.tacz.guns.api.event.common.GunFireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StopSprintingOnShootEvent {
    @SubscribeEvent
    public static void stopSprintOnGunFire(GunFireEvent event){
        event.getShooter().setSprinting(false);
    }

}
