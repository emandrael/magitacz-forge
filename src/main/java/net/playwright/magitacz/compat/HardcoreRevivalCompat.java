package net.playwright.magitacz.compat;

import net.minecraftforge.common.MinecraftForge;
import net.playwright.magitacz.events.hardcore_revival.LostTimeOnShotWhenDown;
import net.playwright.magitacz.events.hardcore_revival.OnKnockout;
import net.playwright.magitacz.events.hardcore_revival.ReviveOnKill;

public class HardcoreRevivalCompat {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(OnKnockout.class);
        MinecraftForge.EVENT_BUS.register(ReviveOnKill.class);
        MinecraftForge.EVENT_BUS.register(LostTimeOnShotWhenDown.class);

    }
}
