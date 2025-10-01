package net.playwright.magitacz.datagen.loot;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import com.tacz.guns.init.ModItems;

import java.util.stream.Stream;

public class MagitaczEntityLoot extends EntityLootSubProvider {

    public MagitaczEntityLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    private static final ResourceLocation TACZ_M1911 = new ResourceLocation("tacz", "modern_kinetic_gun");
    // add more constants for other TACZ guns you want

    public static Item m1911() {
        return BuiltInRegistries.ITEM.getOptional(TACZ_M1911)
                .orElseThrow(() -> new IllegalStateException("Missing TACZ gun: " + TACZ_M1911));
    }

    @Override
    public void generate() {



        add(
                EntityType.SKELETON,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(
                                        LootItem.lootTableItem(ModItems.MODERN_KINETIC_GUN.get())
                                        .apply(SetNbtFunction.setTag(buildGunNbtForM95()))
                                        .setWeight(1)
                                        .when(LootItemRandomChanceCondition.randomChance(1)
                                ))
                )
                );
    }



    private static CompoundTag buildGunNbtForM95() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("AttachmentLock", true);
        tag.putString("GunId", "tacz:m95");
        tag.putString("GunFireMode", "SEMI");
        tag.putInt("GunCurrentAmmoCount", 5);
        tag.putBoolean("HasBulletInBarrel", true);
        return tag;
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        // Only the entities you actually generate loot tables for.
        return Stream.of(EntityType.SKELETON);
    }
}
