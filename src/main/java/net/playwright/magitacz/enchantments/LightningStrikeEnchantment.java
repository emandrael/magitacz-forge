package net.playwright.magitacz.enchantments;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LightningStrikeEnchantment extends Enchantment {
    protected LightningStrikeEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int pLevel) {
        if(!attacker.level().isClientSide) {
            ServerLevel level = (ServerLevel) attacker.level();
            BlockPos position = target.blockPosition();

            if (pLevel == 1){
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
            }

            if (pLevel == 2){
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
            }

            if (pLevel == 3){
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
                EntityType.LIGHTNING_BOLT.spawn(level,null,(Player) null,position,MobSpawnType.TRIGGERED,true,true);
            }

        };

        super.doPostAttack(attacker, target, pLevel);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
