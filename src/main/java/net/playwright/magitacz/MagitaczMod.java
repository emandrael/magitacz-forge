package net.playwright.magitacz;

import com.mojang.logging.LogUtils;
import com.tacz.guns.api.resource.ResourceManager;
import com.tacz.guns.resource.modifier.AttachmentPropertyManager;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.playwright.magitacz.affix.ElementalBulletAffix;
import net.playwright.magitacz.attachment_modifiers.SpellModifier;
import net.playwright.magitacz.blocks.MagitaczBlocks;
import net.playwright.magitacz.datagen.loot.MagitaczLootTableProvider;
import net.playwright.magitacz.enchantments.ModEnchantments;
import net.playwright.magitacz.hud.DamageTypeHud;
import net.playwright.magitacz.item.MagitaczItems;
import net.tslat.tes.api.TESAPI;
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



        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModEnchantments.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        AttachmentPropertyManager.registerModifier();
        AttachmentPropertyManager.getModifiers().put("spell", new SpellModifier());

        AffixRegistry.INSTANCE.registerCodec(loc("elemental_bullet"), ElementalBulletAffix.CODEC);

        modEventBus.addListener(this::gatherData);


        ResourceLocation hudResource = new ResourceLocation("magitacz","textures/item/elementium.png");


        TESAPI.addTESHudElement(hudResource, new DamageTypeHud());

        LOGGER.info("HELLO THERE:"+ AttachmentPropertyManager.getModifiers());

    }


    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        boolean includeServer = event.includeServer();

        generator.addProvider(includeServer, new MagitaczLootTableProvider(output));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
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
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
