package net.playwright.magitacz.Utils;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.playwright.magitacz.MagitaczMod;

public class SpellCastUtils {
    public static void doSpellCastOnEntity( LivingEntity shooter, AbstractSpell spell, Level world, int spellLevel) {
        MagicData shooterData = MagicData.getPlayerMagicData(shooter);

        // Actually cast the spell (if possible)
        if (shooterData.getMana() >= spell.getManaCost(spellLevel) && spell.isLearned((Player)shooter)) {
            spell.castSpell(world, spellLevel, (ServerPlayer) shooter, CastSource.SWORD,false);
        } else {
            MagitaczMod.LOGGER.info("Not enough mana in player");
        }
    }


}
