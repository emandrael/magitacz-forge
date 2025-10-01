package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.Random;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastSpellOnHurtEvent {
    private static final Logger log = LoggerFactory.getLogger(CastSpellOnHurtEvent.class);
    static Random random = new Random();


    @SubscribeEvent
    public static void onGunFire(GunFireEvent event){
        LivingEntity shooter = event.getShooter();
        ItemStack gunItem = shooter.getMainHandItem();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(gunItem.getItem());
        String key = "magitacz_shots_with_gun_" + itemId.toString();
        CompoundTag tag = shooter.getPersistentData();

        int count = tag.getInt(key) + 1;
        tag.putInt(key, count);
    }

    @SubscribeEvent
    public static void onEntityHurtByGun(EntityHurtByGunEvent event) {
        LivingEntity shooter = event.getAttacker();
        Entity victim = event.getHurtEntity();


        if (!(shooter instanceof LivingEntity) || event.getLogicalSide() != LogicalSide.SERVER) return;


        ItemStack gunItem = shooter.getMainHandItem();
        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItem);
        GunData gunData = api.getGunIndex().getGunData();

        AbstractSpell spell  = MagitaczDataUtils.getSpellOnAttachment(gunItem, gunData, AttachmentType.STOCK);
        AttachedSpell attachedSpell = MagitaczDataUtils.getAttachmentSpellData(gunItem, gunData, AttachmentType.STOCK);

        if (spell == null) return; // bad id

        Level world = shooter.level();
        MagicData shooterData = MagicData.getPlayerMagicData(shooter);

        if (victim instanceof LivingEntity livingVictim) {
            shooterData.setAdditionalCastData(new TargetEntityCastData(livingVictim));
        }

        int spellLevel = attachedSpell.getSpellLevel();

        switch(attachedSpell.getCastType()) {
            case ONHIT: {
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
                    doSpellCastOnEntity( shooter, spell, world, spellLevel);
                }

                break;
            }
            case ONCHANCE: {
                Map<String, Double> params = attachedSpell.getCastTypeParameters();
                double chance = 1.0;
                if (params != null && params.containsKey("chance_to_cast")) {
                    chance = params.get("chance_to_cast");
                }
                if (random.nextDouble() < chance) {
                    doSpellCastOnEntity( shooter, spell, world, spellLevel);
                }
                break;
            }
            default:
                // not handled
                break;
        }
    }


    }

