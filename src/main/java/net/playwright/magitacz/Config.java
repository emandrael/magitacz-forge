package net.playwright.magitacz;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {


    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ATTACHMENTS_WEIGHTED =
            BUILDER.comment("Attachments to inject with weights. Format: 'modid:path@weight'. Example: tacz:muzzle_silencer_mirage@5")
                    .defineListAllowEmpty("attachmentsWeighted",
                            List.of(
                                    "tacz:grip_cqr@10",
                                    "tacz:laser_nightstick@10",
                                    "tacz:muzzle_silencer_phantom_s1@10",
                                    "tacz:sight_fastfire_pistol@10", "tacz:sniper_extended_mag_1@10", "tacz:oem_stock_tactical@10", "tacz:sniper_extended_mag_2@10", "tacz:laser_compact@10", "tacz:sight_acro_pistol@10", "tacz:sniper_extended_mag_3@10", "tacz:oem_stock_light@10", "tacz:sight_pk06_rifle@10", "tacz:stock_ak12@10", "tacz:muzzle_silencer_ptilopsis@10", "tacz:laser_lopro@10", "tacz:extended_mag_3@10", "tacz:scope_acog_ta31@10", "tacz:extended_mag_2@10", "tacz:sight_p90@10", "tacz:extended_mag_1@10", "tacz:light_extended_mag_1@10", "tacz:light_extended_mag_2@10", "tacz:sight_deltapoint_pistol@10", "tacz:light_extended_mag_3@10", "tacz:stock_carbon_bone_c5@10", "tacz:ammo_mod_i@10", "tacz:sight_deltapoint_rifle@10", "tacz:stock_moe@10", "tacz:sight_552@10", "tacz:scope_lpvo_1_6@10", "tacz:scope_contender@10", "tacz:scope_elcan_4x@10", "tacz:sight_t1@10", "tacz:sight_t2@10", "tacz:stock_sba3@10", "tacz:bayonet_m9@10", "tacz:stock_tactical_ar@10", "tacz:ammo_mod_hp@10", "tacz:grip_rk0@10", "tacz:muzzle_brake_cthulhu@10", "tacz:grip_osovets_black@10", "tacz:muzzle_brake_timeless50@10", "tacz:ammo_mod_he@10", "tacz:muzzle_brake_pioneer@10", "tacz:grip_cobra@10", "tacz:oem_stock_heavy@10", "tacz:grip_rk6@10", "tacz:muzzle_brake_cyclone_d2@10", "tacz:sight_okp7@10", "tacz:scope_mk5hd@10", "tacz:grip_vertical_talon@10", "tacz:sight_acro_rifle@10", "tacz:muzzle_brake_trex@10", "tacz:muzzle_silencer_mirage@10", "tacz:sight_sro_dot@10", "tacz:stock_hk_slim_line@10", "tacz:bayonet_6h3@10", "tacz:muzzle_compensator_trident@10", "tacz:grip_vertical_military@10", "tacz:sight_fastfire_rifle@10", "tacz:stock_militech_b5@10", "tacz:ammo_mod_fmj@10", "tacz:scope_aug_default@10", "tacz:stock_m4ss@10", "tacz:scope_1873_6x@10", "tacz:laser_peq15@10", "tacz:grip_vertical_ranger@10", "tacz:scope_vudu@10", "tacz:grip_rk1_b25u@10", "tacz:scope_standard_8x@10", "tacz:sight_srs_02@10", "tacz:scope_hamr@10", "tacz:muzzle_silencer_vulture@10", "tacz:sight_exp3@10", "tacz:grip_magpul_afg_2@10", "tacz:sight_uh1@10", "tacz:grip_se_5@10", "tacz:sight_pk06_pistol@10", "tacz:grip_td@10", "tacz:sight_coyote@10", "tacz:muzzle_silencer_knight_qd@10", "tacz:deagle_golden_long_barrel@10", "tacz:muzzle_silencer_ursus@10", "tacz:sight_rmr_dot@10", "tacz:scope_retro_2x@10", "tacz:stock_ripstock@10"),
                            o -> o instanceof String);

    public static Map<ResourceLocation, Integer> attachmentWeights = Map.of();


    public static final ForgeConfigSpec.DoubleValue UNCOMMON_RARITY =
            BUILDER.comment("Uncommon Rarity")
                    .defineInRange("uncommonRarity", 0.20D, 0.0D, 1.0D);

    public static final ForgeConfigSpec.DoubleValue RARE_RARITY =
            BUILDER.comment("Rare Rarity")
                    .defineInRange("rareRarity", 0.10D, 0.0D, 1.0D);

    public static final ForgeConfigSpec.DoubleValue EPIC_RARITY =
            BUILDER.comment("Epic Rarity")
                    .defineInRange("epicRarity", 0.05D, 0.0D, 1.0D);

    public static final ForgeConfigSpec.DoubleValue MYTHIC_RARITY =
            BUILDER.comment("Mythic Rarity")
                    .defineInRange("mythicRarity", 0.01D, 0.0D, 1.0D);

    public static final ForgeConfigSpec.BooleanValue DEBUG_ENABLED =
            BUILDER.comment("Whether to use debug messages. WARNING A LOT OF LOGS INCOMING MUAHHAHAHAHA")
                    .define("debug", false);

    public static final ForgeConfigSpec.BooleanValue INJECT_CHEST_LOOT_TABLES =
            BUILDER.comment("Whether to inject attachments into matching loot tables.")
                    .define("injectChestLoot", true);

    public static final ForgeConfigSpec.BooleanValue HARDCORE_REVIVAL_RES =
            BUILDER.comment("Whether to allow player's to resurrect themselves")
                    .define("enableGunResurrect", true);

    public static final ForgeConfigSpec.BooleanValue HARDCORE_REVIVAL_REDUCE_TIME_ON_SHOT =
            BUILDER.comment("Whether to allow player's to resurrect themselves")
                    .define("reduceTimeOnMissedShot", true);

    public static final ForgeConfigSpec.ConfigValue<Integer> HARDCORE_REVIVAL_SHOT_REDUCTION_TIME =
            BUILDER.comment("Time Taken Away Per Shot")
                    .define("timeReducedPer", 5 * 20);

    public static final ForgeConfigSpec.ConfigValue<Integer> HARDCORE_REVIVAL_STOP_REDUCTION_AT_TICKS =
            BUILDER.comment("Stop reduction time at ticks")
                    .define("stopReductionShotsAt", 10*20);

    // Regex for loot table IDs to target (namespace:path)
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ATTACHMENT_TABLE_REGEX =
            BUILDER.comment("Regex patterns for loot table IDs to receive attachment injections.")
                    .defineListAllowEmpty("attachmentLootTableRegex",
                            List.of(".*treasure.*"),
                            o -> o instanceof String);

    // List of attachment IDs (namespace:path) to inject
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ATTACHMENT_IDS =
            BUILDER.comment("Attachment IDs (namespace:path) to inject into matched loot tables.")
                    .defineListAllowEmpty("attachments",
                            List.of(
                                    "tacz:grip_cqr", "tacz:laser_nightstick", "tacz:muzzle_silencer_phantom_s1", "tacz:sight_fastfire_pistol", "tacz:sniper_extended_mag_1", "tacz:oem_stock_tactical", "tacz:sniper_extended_mag_2", "tacz:laser_compact", "tacz:sight_acro_pistol", "tacz:sniper_extended_mag_3", "tacz:oem_stock_light", "tacz:sight_pk06_rifle", "tacz:stock_ak12", "tacz:muzzle_silencer_ptilopsis", "tacz:laser_lopro", "tacz:extended_mag_3", "tacz:scope_acog_ta31", "tacz:extended_mag_2", "tacz:sight_p90", "tacz:extended_mag_1", "tacz:light_extended_mag_1", "tacz:light_extended_mag_2", "tacz:sight_deltapoint_pistol", "tacz:light_extended_mag_3", "tacz:stock_carbon_bone_c5", "tacz:ammo_mod_i", "tacz:sight_deltapoint_rifle", "tacz:stock_moe", "tacz:sight_552", "tacz:scope_lpvo_1_6", "tacz:scope_contender", "tacz:scope_elcan_4x", "tacz:sight_t1", "tacz:sight_t2", "tacz:stock_sba3", "tacz:bayonet_m9", "tacz:stock_tactical_ar", "tacz:ammo_mod_hp", "tacz:grip_rk0", "tacz:muzzle_brake_cthulhu", "tacz:grip_osovets_black", "tacz:muzzle_brake_timeless50", "tacz:ammo_mod_he", "tacz:muzzle_brake_pioneer", "tacz:grip_cobra", "tacz:oem_stock_heavy", "tacz:grip_rk6", "tacz:muzzle_brake_cyclone_d2", "tacz:sight_okp7", "tacz:scope_mk5hd", "tacz:grip_vertical_talon", "tacz:sight_acro_rifle", "tacz:muzzle_brake_trex", "tacz:muzzle_silencer_mirage", "tacz:sight_sro_dot", "tacz:stock_hk_slim_line", "tacz:bayonet_6h3", "tacz:muzzle_compensator_trident", "tacz:grip_vertical_military", "tacz:sight_fastfire_rifle", "tacz:stock_militech_b5", "tacz:ammo_mod_fmj", "tacz:scope_aug_default", "tacz:stock_m4ss", "tacz:scope_1873_6x", "tacz:laser_peq15", "tacz:grip_vertical_ranger", "tacz:scope_vudu", "tacz:grip_rk1_b25u", "tacz:scope_standard_8x", "tacz:sight_srs_02", "tacz:scope_hamr", "tacz:muzzle_silencer_vulture", "tacz:sight_exp3", "tacz:grip_magpul_afg_2", "tacz:sight_uh1", "tacz:grip_se_5", "tacz:sight_pk06_pistol", "tacz:grip_td", "tacz:sight_coyote", "tacz:muzzle_silencer_knight_qd", "tacz:deagle_golden_long_barrel", "tacz:muzzle_silencer_ursus", "tacz:sight_rmr_dot", "tacz:scope_retro_2x", "tacz:stock_ripstock"

                            ),
                            Config::validateResourceLocation);

    static final ForgeConfigSpec SPEC = BUILDER.build();


    public static boolean debug;



    public static boolean injectChestLoot;

    public static double uncommonRarity;
    public static double rareRarity;
    public static double epicRarity;
    public static double mythicRarity;

    public static List<Pattern> attachmentTablePatterns = List.of();
    public static List<ResourceLocation> attachmentIds = List.of();

    private static boolean validateResourceLocation(final Object obj) {
        if (!(obj instanceof String s)) return false;
        try {
            new ResourceLocation(s);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @SubscribeEvent
    static void onLoadWeighted(final net.minecraftforge.fml.event.config.ModConfigEvent event) {

        attachmentWeights = ATTACHMENTS_WEIGHTED.get().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    String[] parts = s.split("@", 2);
                    try {
                        ResourceLocation id = new ResourceLocation(parts[0]);
                        int weight = parts.length > 1 ? Math.max(1, Integer.parseInt(parts[1])) : 1;
                        return Map.entry(id, weight);
                    } catch (Exception ex) {
                        MagitaczMod.LOGGER.warn("Invalid attachment entry '{}': {}", s, ex.getMessage());
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, java.util.LinkedHashMap::new));
    }


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

        MagitaczMod.LOGGER.info("Loaded Magitacz config file {}", event.getConfig().getFileName());


        debug = DEBUG_ENABLED.get();

        injectChestLoot = INJECT_CHEST_LOOT_TABLES.get();

        uncommonRarity = UNCOMMON_RARITY.get();
        rareRarity = RARE_RARITY.get();
        epicRarity = EPIC_RARITY.get();
        mythicRarity = MYTHIC_RARITY.get();

        // Compile table regex
        attachmentTablePatterns = ATTACHMENT_TABLE_REGEX.get().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(p -> {
                    try {
                        return Pattern.compile(p);
                    } catch (java.util.regex.PatternSyntaxException ex) {
                        MagitaczMod.LOGGER.warn("Invalid loot table regex '{}': {}", p, ex.getMessage());
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        // Parse attachment IDs
        attachmentIds = ATTACHMENT_IDS.get().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(ResourceLocation::new)
                .toList();
    }
}
