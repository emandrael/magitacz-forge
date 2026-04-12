package net.playwright.magitacz.Utils;

import com.tacz.guns.resource.pojo.data.gun.GunData;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.network.casting.OnClientCastPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.playwright.magitacz.MagitaczMod;

public class SpellCastUtils {
    // discount: 0.50f means 50% off
    public static void doSpellCastOnEntity(LivingEntity shooter, AbstractSpell spell, Level level, int spellLevel) {
        if (!(shooter instanceof ServerPlayer player)) return;              // must be a player
        if (level.isClientSide) return;                                     // server-side only
        if (!spell.isLearned(player)) return;



        MagicData data = MagicData.getPlayerMagicData(player);

        float baseCost = spell.getManaCost(spellLevel);
        float discount = 0.75f;                                             // 50% off
        float cost = Math.max(0f, baseCost * (1f - discount));

        // Check against the cost, not the post-deduction amount
        if (data.getMana() < cost) {
            return;
        }

        // Cast first, then charge (or charge then refund if cast can fail and returns a flag)
        castGunSpell(spell,level, spellLevel,discount, player, CastSource.SWORD, false);

    }

    public static void castGunSpell(AbstractSpell spell, Level world, int spellLevel,float discount, ServerPlayer serverPlayer, CastSource castSource, boolean triggerCooldown) {
        MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
        PlayerRecasts playerRecasts = magicData.getPlayerRecasts();
        boolean playerAlreadyHasRecast = playerRecasts.hasRecastForSpell(spell.getSpellId());

        // Be sure to change this to a spell modifier!

        float baseCost = spell.getManaCost(spellLevel);         // 50% off
        float cost = Math.max(0f, baseCost * (1f - discount));


        SpellOnCastEvent event = new SpellOnCastEvent(serverPlayer, spell.getSpellId(), spellLevel, (int) cost, spell.getSchoolType(), castSource);
        MinecraftForge.EVENT_BUS.post(event);

        if (castSource.consumesMana() && !playerAlreadyHasRecast) {
            float newMana = Math.max(magicData.getMana() - (float)event.getManaCost(), 0.0F);
            magicData.setMana(newMana);

            PacketDistributor.sendToPlayer(serverPlayer,new SyncManaPacket(magicData));
        }

        spell.onCast(world, event.getSpellLevel(), serverPlayer, castSource, magicData);
        boolean playerHasRecastsLeft = playerRecasts.hasRecastForSpell(spell.getSpellId());
        if (playerAlreadyHasRecast && playerHasRecastsLeft) {
            playerRecasts.decrementRecastCount(spell.getSpellId());
        } else if (!playerHasRecastsLeft && triggerCooldown) {
            MagicHelper.MAGIC_MANAGER.addCooldown(serverPlayer, spell, castSource);
        }


        PacketDistributor.sendToPlayer(serverPlayer, new OnClientCastPacket(spell.getSpellId(), spellLevel, castSource, magicData.getAdditionalCastData()));
    }

}
