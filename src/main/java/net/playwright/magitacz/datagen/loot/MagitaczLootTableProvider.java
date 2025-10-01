package net.playwright.magitacz.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class MagitaczLootTableProvider extends LootTableProvider {
    public MagitaczLootTableProvider(PackOutput output) {
        super(output,
                Set.of(),
                List.of(
                        new SubProviderEntry(MagitaczEntityLoot::new, LootContextParamSets.ENTITY)
                )
                );
    }
}
