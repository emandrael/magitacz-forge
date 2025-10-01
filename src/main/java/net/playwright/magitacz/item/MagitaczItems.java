package net.playwright.magitacz.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.item.armor.ElementiumArmorItem;
import net.playwright.magitacz.item.custom.SlowingSword;

public class MagitaczItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MagitaczMod.MODID);

    public static final RegistryObject<Item> Elementium = ITEMS.register("elementium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> Elementium_Helmet = ITEMS.register("elementium_helmet",
            () -> new ElementiumArmorItem(ArmorItem.Type.HELMET,new Item.Properties()));

    public static final RegistryObject<Item> Elementium_Chestplate = ITEMS.register("elementium_chestplate",
            () -> new ElementiumArmorItem(ArmorItem.Type.CHESTPLATE,new Item.Properties()));

    public static final RegistryObject<Item> Elementium_Leggings = ITEMS.register("elementium_leggings",
            () -> new ElementiumArmorItem(ArmorItem.Type.LEGGINGS,new Item.Properties()));

    public static final RegistryObject<Item> Elementium_Boots = ITEMS.register("elementium_boots",
            () -> new ElementiumArmorItem(ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> Elementium_Sword = ITEMS.register("elementium_sword",
            () -> new SlowingSword(Tiers.NETHERITE,10,3,new Item.Properties().durability(633)));

    public static final RegistryObject<Item> Raw_Elementium = ITEMS.register("raw_elementium",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
