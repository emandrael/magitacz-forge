package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;

import java.util.Map;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastSpellOnShootEvent {
    @SubscribeEvent
    public static void onGunFire(GunFireEvent event) {
        LivingEntity shooter = event.getShooter();
        ItemStack gunItem = shooter.getMainHandItem();
        Level world = shooter.level();

        if (!(shooter instanceof LivingEntity) || event.getLogicalSide() != LogicalSide.SERVER) return;

        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItem);
        GunData gunData = api.getGunIndex().getGunData();

        AbstractSpell spell  = MagitaczDataUtils.getSpellOnAttachment(gunItem, gunData, AttachmentType.STOCK);
        AttachedSpell attachedSpell = MagitaczDataUtils.getAttachmentSpellData(gunItem, gunData, AttachmentType.STOCK);

        if (spell == null || attachedSpell == null) return; // bad id


        switch(attachedSpell.getCastType()) {
            case ONSHOOT: {
                // Every X bullets, cast (if param is missing, default 1)
                Map<String, Double> params = attachedSpell.getCastTypeParameters();
                int castPerX = 1;

                if (params != null && params.containsKey("per_x")) {
                    castPerX = params.get("per_x").intValue();
                }

                // NBT key unique per gun type
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(gunItem.getItem());
                String key = "magitacz_shots_with_gun_" + itemId.toString();
                CompoundTag tag = shooter.getPersistentData();
                int count = tag.getInt(key);

                boolean shouldTrigger = count % castPerX == 0;

                if (shouldTrigger) {
                    doSpellCastOnEntity(shooter, spell, world, attachedSpell.getSpellLevel());
                }

                break;
            }

    }
}
}