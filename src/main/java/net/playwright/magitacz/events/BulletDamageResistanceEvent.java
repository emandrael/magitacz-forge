package net.playwright.magitacz.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BulletDamageResistanceEvent {


    @SubscribeEvent
    public static void onFinalDamage(net.minecraftforge.event.entity.living.LivingDamageEvent event) {

        var entity = event.getEntity();
        var src = event.getSource();

        Entity direct = src.getDirectEntity(); // e.g., an arrow or the same attacker for melee
        Entity attacker = src.getEntity();     // the living attacker (shooter), if any

        String directStr = direct != null ? direct.getType().toShortString() : "null";
        String attackerStr = attacker != null ? attacker.getType().toShortString() : "null";

        if (directStr.equals("bullet")) {
            MagitaczMod.LOGGER.info("IS A BULLET");
        }
    }

}
