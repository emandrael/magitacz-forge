package net.playwright.magitacz;

import com.mojang.logging.LogUtils;
import com.tacz.guns.api.resource.ResourceManager;
import com.tacz.guns.resource.modifier.AttachmentPropertyManager;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.playwright.magitacz.apoth.affix.ElementalBulletAffix;
import net.playwright.magitacz.apoth.affix.SpellAffix;
import net.playwright.magitacz.apoth.category.AttachmentLootCategories;
import net.playwright.magitacz.attributes.MagitaczAttributes;
import net.playwright.magitacz.blocks.MagitaczBlocks;
import net.playwright.magitacz.enchantments.ModEnchantments;
import net.playwright.magitacz.item.MagitaczItems;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MagitaczMod.MODID)
public class MagitaczMod {


    // Define mod id in a common place for everything to reference
    public static final String MODID = "magitacz";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String DEFAULT_PACK_NAME = "playwrights_gunpack";



    public static ResourceLocation loc(String path) {
        return new ResourceLocation("magitacz", path);
    }


    public MagitaczMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MagitaczItems.ITEMS.register(modEventBus);
        MagitaczBlocks.register(modEventBus);
        MagitaczAttributes.register(modEventBus);





        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModEnchantments.register(modEventBus);



        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        AffixRegistry.INSTANCE.registerCodec(loc("elemental_bullet"), ElementalBulletAffix.CODEC);
        AffixRegistry.INSTANCE.registerCodec(loc("spells_attachment"), SpellAffix.CODEC);

        modEventBus.addListener(this::gatherData);


        ResourceLocation hudResource = new ResourceLocation("magitacz","textures/item/elementium.png");

        registerDefaultExtraGunPack();

    }



    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        boolean includeServer = event.includeServer();
    }


    private static void registerDefaultExtraGunPack() {
        String jarDefaultPackPath = String.format("/assets/%s/custom/%s", MODID, DEFAULT_PACK_NAME);
        ResourceManager.registerExportResource(MagitaczMod.class, jarDefaultPackPath);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        AttachmentLootCategories.init();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(MagitaczItems.Elementium);
            event.accept(MagitaczItems.Raw_Elementium);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(MagitaczBlocks.Elementium_Block);
            event.accept(MagitaczBlocks.Raw_Elementium_Block);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
        }
    }
}
