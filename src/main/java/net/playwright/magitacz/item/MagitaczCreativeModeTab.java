package net.playwright.magitacz.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.blocks.MagitaczBlocks;

public class MagitaczCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagitaczMod.MODID);

    public static final RegistryObject<CreativeModeTab> Course_Tab = CREATIVE_MODE_TABS.register("coursetab",() ->  CreativeModeTab.builder().icon(() -> new ItemStack(MagitaczItems.Elementium.get())).title(Component.translatable("creativetab.magitacz")).displayItems((itemDisplayParameters, output) -> {
        output.accept(MagitaczItems.Elementium.get());
        output.accept(MagitaczItems.Raw_Elementium.get());
        output.accept(MagitaczItems.Elementium_Sword.get());
        output.accept(MagitaczBlocks.Elementium_Block.get());
        output.accept(MagitaczBlocks.Raw_Elementium_Block.get());
        output.accept(MagitaczItems.Elementium_Helmet.get());
        output.accept(MagitaczItems.Elementium_Chestplate.get());
        output.accept(MagitaczItems.Elementium_Leggings.get());
        output.accept(MagitaczItems.Elementium_Boots.get());

    }).build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
