package net.playwright.magitacz.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.playwright.magitacz.MagitaczMod;

public class MagitaczAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MagitaczMod.MODID);

    public static final RegistryObject<Attribute> CASTING_SHOT_AMPLIFICATION = ATTRIBUTES.register(
            "casting_shot_amplification",
            () -> new RangedAttribute("attribute." + MagitaczMod.MODID + ".casting_shot_amplification", 1.0D, 0.0D, 2.0D)
                    .setSyncable(true) // important for client visibility
    );

    public static final RegistryObject<Attribute> MANA_COST_REDUCTION = ATTRIBUTES.register(
            "mana_cost_reduction.json",
            () -> new RangedAttribute("attribute." + MagitaczMod.MODID + ".mana_cost_reduction.json", 1.0D, 0.0D, 2.0D)
                    .setSyncable(true) // important for client visibility
    );


    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
    }
}