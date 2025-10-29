package net.playwright.magitacz.loot;

import com.tacz.guns.init.ModItems;
import mod.chloeprime.apotheosismodernragnarok.common.loot.ApothReforgeFunction;

import mod.chloeprime.gunsmithlib.api.common.GunLootFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.Config;
import net.playwright.magitacz.MagitaczMod;

import java.util.Map;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootInjections {

    private static boolean matchesConfiguredTables(ResourceLocation id) {
        String s = id.toString();
        for (Pattern p : Config.attachmentTablePatterns) {
            if (p.matcher(s).matches()) return true;
        }
        return false;
    }


    @SubscribeEvent
    public static void onLootTableLoadWeighted(net.minecraftforge.event.LootTableLoadEvent event) {
        if (!Config.injectChestLoot) return;
        if (!matchesConfiguredTables(event.getName())) return; // your regex predicate

        if (!Config.attachmentWeights.isEmpty()) {
            injectAttachmentsWeighted(event.getTable(), Config.attachmentWeights, 0.6F); // poolChance=1.0 means always
        }
    }


    public static void onLootTableLoad(net.minecraftforge.event.LootTableLoadEvent event) {
        if (!Config.injectChestLoot) return;
        if (!matchesConfiguredTables(event.getName())) return;

        LootTable table = event.getTable();

        for (ResourceLocation attachmentId : Config.attachmentIds) {
            // Choose your injection flavor; here we use your existing “rare” method with 100% chance
            injectAttachment(table, attachmentId, 0.5F);
        }
    }

    private static void injectAttachmentsWeighted(LootTable table,
                                                  Map<ResourceLocation, Integer> weightsByAttachment,
                                                  float poolChance) {
        // 1 roll: pick one entry using weights
        LootPool.Builder pool = LootPool.lootPool()
                .name("magitacz_attachments_weighted")
                .setRolls(ConstantValue.exactly(3));

        if (poolChance < 1.0F) {
            pool.when(LootItemRandomChanceCondition.randomChance(poolChance));
        }

        for (Map.Entry<ResourceLocation, Integer> e : weightsByAttachment.entrySet()) {
            ResourceLocation attachmentId = e.getKey();
            int weight = Math.max(1, e.getValue());

            LootPoolSingletonContainer.Builder<?> entry =
                    LootItem
                            .lootTableItem((net.minecraft.world.level.ItemLike) ModItems.ATTACHMENT.get())
                            .setWeight(weight) // weight per attachment
                            .apply(GunLootFunctions.initAttachmentInfo(attachmentId))
                            // your rarity pipeline (adjust as you like)
                            .apply(ApothReforgeFunction.apothReforge(new ResourceLocation("apotheosis:common")))
                            .apply(reforgeWithCondition(new ResourceLocation("apotheosis:uncommon"), 0.40F))
                            .apply(reforgeWithCondition(new ResourceLocation("apotheosis:rare"),     0.20F))
                            .apply(reforgeWithCondition(new ResourceLocation("apotheosis:epic"),     0.10F))
                            .apply(reforgeWithCondition(new ResourceLocation("apotheosis:mythic"),   0.05F));

            pool.add(entry);
        }

        table.addPool(pool.build());
    }



    private static void injectAttachment(LootTable table, ResourceLocation attachmentId, float chance) {
        MagitaczMod.LOGGER.info("Loot Injection: Inject {} into {}", attachmentId, table.getLootTableId());

        LootPoolEntryContainer.Builder<?> entry =
                ((LootPoolSingletonContainer.Builder)
                        LootItem.lootTableItem((ItemLike) ModItems.ATTACHMENT.get()))
                        .apply(GunLootFunctions.initAttachmentInfo(attachmentId))
                        .apply(ApothReforgeFunction.apothReforge(new ResourceLocation("apotheosis:common")))
                        .apply(reforgeWithCondition(new ResourceLocation("apotheosis:uncommon"), 0.40F))
                        .apply(reforgeWithCondition(new ResourceLocation("apotheosis:rare"), 0.20F))
                        .apply(reforgeWithCondition(new ResourceLocation("apotheosis:epic"), 0.10F))
                        .apply(reforgeWithCondition(new ResourceLocation("apotheosis:mythic"), 0.05F));


        LootPool.Builder poolBuilder = LootPool.lootPool().add(entry);
        if (chance < 1.0F) {
            poolBuilder.when(LootItemRandomChanceCondition.randomChance(chance));
        }
        table.addPool(poolBuilder.build());
    }

    private static LootItemFunction.Builder reforgeWithCondition(ResourceLocation rarity, float chance) {
        return ApothReforgeFunction.apothReforge(rarity)
                .when(LootItemRandomChanceCondition.randomChance(chance));
    }
}