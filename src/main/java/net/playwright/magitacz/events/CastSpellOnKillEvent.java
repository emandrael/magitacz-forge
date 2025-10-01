package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
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
public class CastSpellOnKillEvent {
    @SubscribeEvent
    public static void onEntityKill(EntityKillByGunEvent event) {
        LivingEntity shooter = event.getAttacker();
        LivingEntity victim = event.getKilledEntity();

        assert shooter != null;


        if (!(shooter instanceof LivingEntity) || event.getLogicalSide() != LogicalSide.SERVER) return;


        ItemStack gunItem = shooter.getMainHandItem();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(gunItem.getItem());
        String key = "magitacz_kills_with_gun_" + itemId.toString();
        CompoundTag tag = shooter.getPersistentData();

        int killCount = tag.getInt(key) + 1;
        tag.putInt(key, killCount);

        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItem);
        GunData gunData = api.getGunIndex().getGunData();

        AbstractSpell spell  = MagitaczDataUtils.getSpellOnAttachment(gunItem, gunData, AttachmentType.STOCK);
        AttachedSpell attachedSpell = MagitaczDataUtils.getAttachmentSpellData(gunItem, gunData, AttachmentType.STOCK);

        if(attachedSpell == null) return;

        Level world = shooter.level();
        MagicData shooterData = MagicData.getPlayerMagicData(shooter);



        Map<String, Double> params = attachedSpell.getCastTypeParameters();
        int castPerX = 1;
        int spellLevel = attachedSpell.getSpellLevel();



        if (params != null && params.containsKey("per_x")) {
            castPerX = params.get("per_x").intValue();
        }

        MagitaczMod.LOGGER.info("Kill Count: {}", killCount);


        boolean shouldTrigger = killCount % castPerX == 0;

        MagitaczMod.LOGGER.info("{} % {}",killCount, castPerX);

        if (shouldTrigger) {
            assert victim != null;
            shooterData.setAdditionalCastData(new TargetEntityCastData(victim));
            doSpellCastOnEntity( shooter, spell, world, spellLevel);
        }

        if (spell == null) return; // bad id
    }



}
