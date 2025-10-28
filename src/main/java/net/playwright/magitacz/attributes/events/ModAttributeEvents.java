package net.playwright.magitacz.attributes.events;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.attributes.MagitaczAttributes;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributeEvents {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent e) {
        MagitaczMod.LOGGER.info("Adding Spell Shot Reduction Attribute");
        Attribute attr = MagitaczAttributes.CASTING_SHOT_AMPLIFICATION.get();
        e.add(EntityType.PLAYER, attr);
        attr = MagitaczAttributes.MANA_COST_REDUCTION.get();
        e.add(EntityType.PLAYER, attr);
    }
}