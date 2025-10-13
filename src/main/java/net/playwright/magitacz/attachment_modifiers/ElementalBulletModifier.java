package net.playwright.magitacz.attachment_modifiers;

import com.tacz.guns.api.modifier.CacheValue;
import com.tacz.guns.api.modifier.IAttachmentModifier;
import com.tacz.guns.api.modifier.JsonProperty;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ElementalBulletModifier implements IAttachmentModifier<String,String > {

    private static final String ID = "elemental_bullet";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public JsonProperty readJson(String s) {
        return null;
    }

    @Override
    public CacheValue initCache(ItemStack itemStack, GunData gunData) {
        return null;
    }

    @Override
    public void eval(List list, CacheValue cacheValue) {

    }
}
