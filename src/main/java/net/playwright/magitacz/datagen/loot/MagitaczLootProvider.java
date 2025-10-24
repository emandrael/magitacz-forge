package net.playwright.magitacz.datagen.loot;

import com.tacz.guns.init.ModItems;
import mod.chloeprime.apotheosismodernragnarok.ApotheosisModernRagnarok;
import mod.chloeprime.apotheosismodernragnarok.common.loot.ApothReforgeFunction;
import mod.chloeprime.gunsmithlib.api.common.GunLootFunctions;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.playwright.magitacz.MagitaczMod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class MagitaczLootProvider {

    public MagitaczLootProvider() {}

    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(MagitaczLootProvider.InjectSubProvider::new, LootContextParamSets.CHEST)));

    }

    @ParametersAreNonnullByDefault
    public static class InjectSubProvider implements LootTableSubProvider {
        public InjectSubProvider() {
        }

        private LootTable.Builder gunAndAmmo(String gunId, String ammoId, int rolls, int ammoCountPerRoll) {
            return this.gunAndAmmo(gunId, ammoId, ConstantValue.exactly((float)rolls), ConstantValue.exactly((float)ammoCountPerRoll));
        }

        private LootTable.Builder gunAndAmmo(String gunId, String ammoId, NumberProvider rolls, NumberProvider ammoCountPerRoll) {
            return LootTable.lootTable().withPool(LootPool.lootPool().add(((LootPoolSingletonContainer.Builder) LootItem.lootTableItem((ItemLike) ModItems.MODERN_KINETIC_GUN.get()).when(GunLootFunctions.isGunInstalled(new ResourceLocation(gunId)))).apply(GunLootFunctions.initGunInfo(new ResourceLocation(gunId))))).withPool(LootPool.lootPool().add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)ModItems.AMMO.get()).when(GunLootFunctions.isAmmoInstalled(new ResourceLocation(ammoId)))).apply(GunLootFunctions.initAmmoInfo(new ResourceLocation(ammoId))).apply(SetItemCountFunction.setCount(ammoCountPerRoll))).setRolls(rolls));

        }


        private static final String pillagedStr = "_pillaged";
        List<String> guns = List.of("pistol_revolver_whirly", "pistol_auto_stress", "rifle_assult_peacock", "shotgun_db_rock", "sniper_semi_unclassical");


        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {

            for (String gun : guns) {
                String pillagedGunId = MagitaczMod.DEFAULT_PACK_NAME + ":" + gun;
                output.accept(
                        MagitaczMod.loc("kits/tacz/pistol/" + gun + pillagedStr),
                        this.gunAndAmmo(pillagedGunId, "create_armorer:rbapb", 3, 4)
                );
            }

            for (String gun : guns) {
                String pillagedGunId = MagitaczMod.DEFAULT_PACK_NAME + ":" + gun + pillagedStr;
                output.accept(
                        MagitaczMod.loc("kits/tacz/pistol/" + gun + pillagedStr),
                        this.gunAndAmmo(pillagedGunId, "create_armorer:rbapb", 3, 4)
                );
            }


            output.accept(MagitaczMod.loc("kits/tacz/pistol/whirly_pillaged"), this.gunAndAmmo("playwrights_gunpack:pistol_revolver_whirly_pillaged", "create_armorer:rbapb", 3, 4));

            output.accept(
                    MagitaczMod.loc("injects/chest/integrated_dungeons"),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .add(LootTableReference.lootTableReference(MagitaczMod.loc("kits/tacz/pistol/whirly_pillaged"))
                                                    .setWeight(100)
                                                    .setQuality(2)
                                                    .apply(ApothReforgeFunction.apothReforge(new ResourceLocation("apotheosis:mythic"))))));

        }
    }


}
