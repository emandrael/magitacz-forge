package net.playwright.magitacz.apoth.category;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.resource.index.CommonAttachmentIndex;

import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import mod.chloeprime.apotheosismodernragnarok.ApotheosisModernRagnarok;
import mod.chloeprime.apotheosismodernragnarok.common.CommonConfig;
import mod.chloeprime.apotheosismodernragnarok.common.affix.category.GunPredicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.MagitaczMod;
import org.jetbrains.annotations.NotNull;

public class ExtraLootCategories {
    public static LootCategory EXTENDED_MAG;
    public static LootCategory LASER;
    public static LootCategory GRIP;
    public static LootCategory STOCK;
    public static LootCategory MUZZLE;
    public static LootCategory SCOPE;


    private static final Set<LootCategory> ALL_ATTACHMENTS = new LinkedHashSet(12);

    public ExtraLootCategories() {
    }

    public static Set<LootCategory> all() {
        return Collections.unmodifiableSet(ALL_ATTACHMENTS);
    }

    public static boolean isAttachment(LootCategory category) {
        return ALL_ATTACHMENTS.contains(category);
    }

    public static void init() {
        MUZZLE = register("muzzle", AttachmentPredicate.matchIndex(ExtraLootCategories::isMuzzle), EquipmentSlot.MAINHAND);
        EXTENDED_MAG = register("extended_mag", AttachmentPredicate.matchIndex(ExtraLootCategories::isExtendedMagazine), EquipmentSlot.MAINHAND);
        LASER = register("laser", AttachmentPredicate.matchIndex(ExtraLootCategories::isLaser), EquipmentSlot.MAINHAND);
        GRIP = register("grip", AttachmentPredicate.matchIndex(ExtraLootCategories::isGrip), EquipmentSlot.MAINHAND);
        STOCK = register("stock", AttachmentPredicate.matchIndex(ExtraLootCategories::isStock), EquipmentSlot.MAINHAND);
        SCOPE = register("scope", AttachmentPredicate.matchIndex(ExtraLootCategories::isScope), EquipmentSlot.MAINHAND);
    }

    static boolean isMuzzle(@NotNull CommonAttachmentIndex index) { return index.getType() == AttachmentType.MUZZLE;}

    static boolean isScope(@NotNull CommonAttachmentIndex index) {
        return index.getType() == AttachmentType.SCOPE;
    }

    static boolean isGrip(@NotNull CommonAttachmentIndex index) {
        return index.getType() == AttachmentType.GRIP;
    }

    static boolean isStock(@NotNull CommonAttachmentIndex index) {
        return index.getType() == AttachmentType.STOCK;
    }

    static boolean isExtendedMagazine(@NotNull CommonAttachmentIndex index) {return index.getType() == AttachmentType.EXTENDED_MAG;}

    static boolean isLaser(@NotNull CommonAttachmentIndex index) {
        return index.getType() == AttachmentType.LASER;
    }


    private static LootCategory register(String path, Predicate<ItemStack> predicate, EquipmentSlot... slots) {
        LootCategory registered = LootCategory.register((LootCategory)null, MagitaczMod.loc(path).toString(), predicate, slots);
        ALL_ATTACHMENTS.add(registered);
        return registered;
    }
}
