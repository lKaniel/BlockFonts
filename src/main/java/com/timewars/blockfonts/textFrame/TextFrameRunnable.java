package com.timewars.blockfonts.textFrame;

import com.rexcantor64.triton.api.players.LanguagePlayer;
import com.timewars.blockfonts.BlockFonts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class TextFrameRunnable implements Runnable{
    private Thread t;
    private String threadName;
    private LanguagePlayer languagePlayer;
    private BlockFonts blockFonts;


    public TextFrameRunnable(String threadName, LanguagePlayer languagePlayer, BlockFonts blockFonts) {
        this.threadName = threadName;
        this.languagePlayer = languagePlayer;
        this.blockFonts = blockFonts;
    }

    @Override
    public void run() {

        Player player = Bukkit.getPlayer(languagePlayer.getUUID());
        for (Map.Entry<String, TextFrame> entry : blockFonts.getTextFrameMap().entrySet()){
            entry.getValue().replaceWithLang(languagePlayer.getLang().getName(), player);
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
