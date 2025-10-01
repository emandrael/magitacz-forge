package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.event.common.GunDamageSourcePart;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.DamageSourceUtils;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;

import java.util.List;
@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElementalBulletOnShoot {

    @SubscribeEvent
    public static void onEntityHurtByElementalGun(EntityHurtByGunEvent.Pre event){

        Entity bullet = event.getBullet();
        Entity shooter = event.getAttacker();
        Level level = shooter.level();

        if (event.getHurtEntity() == null) return;


        try {
            LivingEntity hurtEntity = (LivingEntity) event.getHurtEntity();
        }
        catch (ClassCastException e){
            MagitaczMod.LOGGER.info(e.toString());
            return;
        }

        ItemStack gunItem = event.getAttacker().getMainHandItem();
        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItem);

        List<AttachedSpell> spells = MagitaczDataUtils.getAttachedSpells(gunItem, api.getGunIndex().getGunData());
        if (spells.isEmpty()) {
            MagitaczMod.LOGGER.info("No Spell attached!");
            return;
        };
        AttachedSpell attachedSpell = spells.get(0);

        ResourceLocation spellId = new ResourceLocation("irons_spellbooks", attachedSpell.getSpellName());


        AbstractSpell spell = SpellRegistry.getSpell(spellId);


        ResourceKey<DamageType> schoolDamageType = spell.getSchoolType().getDamageType(); // Adjust based on ISS's actual registry

        DamageSource schoolDamageSource = DamageSourceUtils.getDamageSourceFromResource(level, schoolDamageType, bullet, shooter);

        event.setDamageSource(GunDamageSourcePart.NON_ARMOR_PIERCING, schoolDamageSource);
        event.setDamageSource(GunDamageSourcePart.ARMOR_PIERCING, schoolDamageSource);
    }
}
