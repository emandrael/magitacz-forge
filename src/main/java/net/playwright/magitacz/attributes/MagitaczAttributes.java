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

    public static final RegistryObject<Attribute> SPELL_SHOT_REDUCTION = ATTRIBUTES.register(
            "spell_shot_reduction",
            () -> new RangedAttribute("attribute." + MagitaczMod.MODID + ".spell_shot_reduction", 1.0D, 0.0D, 2.0D)
                    .setSyncable(true) // important for client visibility
    );

    public static void register(IEventBus modBus) {
        ATTRIBUTES.register(modBus);
    }
}