package net.playwright.magitacz.item.armor;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

import java.util.Map;

public interface MagitaczArmorMaterial extends ArmorMaterial {

    public abstract Map<Attribute, AttributeModifier> getAdditionalAttributes();

}

