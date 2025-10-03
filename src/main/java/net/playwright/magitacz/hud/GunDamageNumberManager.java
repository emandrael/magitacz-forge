package net.playwright.magitacz.hud;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ConcurrentHashMap;

public class GunDamageNumberManager {

    private static final GunDamageNumberManager INSTANCE = new GunDamageNumberManager();

    public final ConcurrentHashMap<Integer, Float> HealthMap = new ConcurrentHashMap<>();

    // 获取单例实例的方法
    public static GunDamageNumberManager getInstance() {
        return INSTANCE;
    }


    public static Float CalculateDamageDelta(Integer entityId, Float currentHealth) {
        Float previousHealth = getInstance().HealthMap.get(entityId);
        if (previousHealth == null) {
            // 没有记录，返回0或你认为合适的值
            return (float) 0;
        }
        return previousHealth - currentHealth;
    }

    public static void createElementalNumberParticle(LivingEntity entity, float currentHealth, float damage, Boolean isHeadshot) {

        var healthDelta = CalculateDamageDelta(entity.getId(), currentHealth);
        // 检测是否为0，如果为0则不创建粒子
        if (healthDelta == 0) {
            return;
        }
    }
}
