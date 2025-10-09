package net.playwright.magitacz.loot;

import com.obscuria.aquamirae.common.entities.CaptainCornelia;
import mod.chloeprime.apotheosismodernragnarok.ApotheosisModernRagnarok;
import mod.chloeprime.apotheosismodernragnarok.common.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.Config;
import net.playwright.magitacz.MagitaczMod;

@Mod.EventBusSubscriber
public class LootInjections {
    public LootInjections() {};

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation id = event.getName();
        LootTable table = event.getTable();
        switch (id.toString()) {
            case "minecraft:chests/ancient_city":
                injectChest(table, "injects/chest/middle_east", 99.0F);
                break;
        }
    }

    private static void injectChest(LootTable table, String name, float chance) {
        if ((Boolean) Config.INJECT_CHEST_LOOT_TABLES.get()) {
            LootPool.Builder pool = LootPool.lootPool();
            if (chance < 1.0F) {
                pool = pool.when(LootItemRandomChanceCondition.randomChance(chance));
            }

            table.addPool(pool.add(LootTableReference.lootTableReference(MagitaczMod.loc(name))).build());
        }
    }
}
