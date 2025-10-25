package net.playwright.magitacz.apoth.category;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mod.chloeprime.apotheosismodernragnarok.common.internal.EnhancedGunData;
import mod.chloeprime.gunsmithlib.api.util.GunInfo;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface AttachmentPredicate extends Predicate<ItemStack> {
    static AttachmentPredicate matchIndex(Predicate<CommonAttachmentIndex> predicate) {
        return of((stack, gun, index) -> predicate.test(index));
    }

    default boolean test(ItemStack stack) {
        Item attachmentItem = stack.getItem();
        boolean var10000;
        if (attachmentItem instanceof IAttachment attachment) {
            if (TimelessAPI.getCommonAttachmentIndex(
                    attachment.getAttachmentId(stack)).
                    filter((index) -> this.testAttachment(stack, attachment, index)).isPresent()) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    }

    static double getBuffCoefficient(ItemStack gun) {
        return (Double)Gunsmith.getGunInfo(gun).map(GunInfo::index).map(mod.chloeprime.apotheosismodernragnarok.common.affix.category.GunPredicate::getBuffCoefficient).orElse((double)1.0F);
    }

    static double getBuffCoefficient(ResourceLocation gunId) {
        return (Double)TimelessAPI.getCommonGunIndex(gunId).map(mod.chloeprime.apotheosismodernragnarok.common.affix.category.GunPredicate::getBuffCoefficient).orElse((double)1.0F);
    }


    boolean testAttachment(ItemStack var1, IAttachment var2, CommonAttachmentIndex var3);

    static AttachmentPredicate of(AttachmentPredicate lambda) {
        return lambda;
    }

    static AttachmentPredicate any() {
        return of((g, s, i) -> true);
    }

}
