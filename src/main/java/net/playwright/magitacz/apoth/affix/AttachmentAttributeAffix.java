package net.playwright.magitacz.apoth.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.resource.pojo.data.gun.FeedType;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.codec.PlaceboCodecs;
import dev.shadowsoffire.placebo.util.StepFunction;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class AttachmentAttributeAffix extends AttributeAffix {
    private final int miniumMagazineCapacity;
    private final boolean disableOnInventoryBulletFeedGuns;
//    public static final Codec<AttachmentAttributeAffix> CODEC;
    /** @deprecated */
    @Deprecated
//    public static final Codec<AttachmentAttributeAffix> CODEC_WITH_OLD_NAME;

    public AttachmentAttributeAffix(Attribute attr, AttributeModifier.Operation op, Map<LootRarity, StepFunction> values, Set<LootCategory> types, int miniumMagazineCapacity, boolean disableOnInventoryBulletFeedGuns) {
        super(attr, op, values, types);
        this.miniumMagazineCapacity = miniumMagazineCapacity;
        this.disableOnInventoryBulletFeedGuns = disableOnInventoryBulletFeedGuns;
    }

    public boolean canApplyTo(GunData gunData) {
        boolean ammoCap = gunData.getAmmoAmount() >= this.miniumMagazineCapacity;
        boolean feedTyp = !this.disableOnInventoryBulletFeedGuns || gunData.getReloadData().getType() != FeedType.INVENTORY;
        return ammoCap && feedTyp;
    }

    public boolean canApplyTo(ItemStack stack, LootCategory cat, LootRarity rarity) {
        return true;
    }

    public Codec<? extends Affix> getCodec() {
        return CODEC;
    }
}
