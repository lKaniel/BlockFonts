package com.timewars.blockfonts.listeners;

import com.rexcantor64.triton.api.events.PlayerChangeLanguageSpigotEvent;
import com.rexcantor64.triton.api.players.LanguagePlayer;
import com.timewars.blockfonts.BlockFonts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChangeLangEventListener implements Listener {

    BlockFonts blockFonts;

    public PlayerChangeLangEventListener(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
    }

    @EventHandler
    public void onPlayerChangeLang(PlayerChangeLanguageSpigotEvent playerChangeLanguageSpigotEvent){
        LanguagePlayer languagePlayer = playerChangeLanguageSpigotEvent.getLanguagePlayer();
        blockFonts.updatePlayerFrames(languagePlayer);
        System.out.println(blockFonts.getTriton().getLanguageManager().getAllLanguages());
    }

}
