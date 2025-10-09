package net.playwright.magitacz.Utils;

import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class test {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // Only log on the server to avoid double spam
        if (event.getEntity().level().isClientSide) return;

        DamageSource src = event.getSource();

        // 1) Get the DamageType holder from the source
        // Mojang/Forge 1.20.1 commonly exposes typeHolder(). If your IDE can’t find it,
        // see the fallback note below.
        Holder<DamageType> holder = src.typeHolder();
        // 2) Turn the holder into a registry id like "minecraft:mob_attack" or "irons_spellbooks:frost"
        String typeId = holder.unwrapKey()
                .map(ResourceKey::location)
                .map(ResourceLocation::toString)
                .orElse("unknown");

        // 3) Optional: also log who directly hit and who caused it
        Entity direct = src.getDirectEntity(); // e.g., an arrow or the same attacker for melee
        Entity attacker = src.getEntity();     // the living attacker (shooter), if any

        String victimName = event.getEntity().getName().getString();
        float amount = event.getAmount();


        String directStr = direct != null ? direct.getType().toShortString() : "null";
        String attackerStr = attacker != null ? attacker.getType().toShortString() : "null";

        MagitaczMod.LOGGER.info("[Damage] {} took {} damage | type={} | direct={} | attacker={}",
                victimName, amount, typeId, directStr, attackerStr);
    }
}
