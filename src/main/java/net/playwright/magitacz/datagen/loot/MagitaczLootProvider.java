package net.playwright.magitacz.datagen.loot;

import com.tacz.guns.init.ModItems;
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

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
            output.accept(MagitaczMod.loc("kits/tacz/pistol/deagle"), this.gunAndAmmo("tacz:deagle", "tacz:50ae", 1, 18));
            output.accept(
                    MagitaczMod.loc("injects/chest/middle_east"),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .add(LootTableReference.lootTableReference(MagitaczMod.loc("kits/tacz/pistol/deagle"))
                                                    .setWeight(100000)
                                                    .setQuality(2)
                                                    .apply(ApothReforgeFunction.apothReforge(new ResourceLocation("apotheosis:mythic"))))));


        }
    }


}
