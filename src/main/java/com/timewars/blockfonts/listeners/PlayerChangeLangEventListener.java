package com.timewars.blockfonts.listeners;

import com.rexcantor64.triton.api.events.PlayerChangeLanguageSpigotEvent;
import com.rexcantor64.triton.api.players.LanguagePlayer;
import com.timewars.blockfonts.BlockFonts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerChangeLangEventListener implements Listener {

    BlockFonts blockFonts;

    public PlayerChangeLangEventListener(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        Player player = playerJoinEvent.getPlayer();
        LanguagePlayer languagePlayer = blockFonts.getTriton().getPlayerManager().get(player.getUniqueId());
        blockFonts.updatePlayerFrames(languagePlayer);

    }

    @EventHandler
    public void onPlayerChangeLang(PlayerChangeLanguageSpigotEvent playerChangeLanguageSpigotEvent){
        LanguagePlayer languagePlayer = playerChangeLanguageSpigotEvent.getLanguagePlayer();
        if (!playerChangeLanguageSpigotEvent.getNewLanguage().getName().equalsIgnoreCase(playerChangeLanguageSpigotEvent.getOldLanguage().getName())) {
            blockFonts.updatePlayerFrames(languagePlayer);
        }
    }

}
