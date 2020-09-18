package com.timewars.blockfonts.commands;

import com.rexcantor64.triton.api.language.Language;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.timewars.blockfonts.BlockFonts;
import com.timewars.blockfonts.textFrame.TextFrame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RemoveLangFrameCommand implements TabExecutor {

    BlockFonts blockFonts;

    public RemoveLangFrameCommand(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                String name = args[0];
                if (!blockFonts.getTextFrameMap().containsKey(name)) {
                    player.sendMessage(ChatColor.RED + "Text frame with name " + name + " doesn't exists");
                }
                String lang = args[1];
                blockFonts.removeLangFrame(name, lang);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Wrong command usage, try /textFrame addLangFrame <frame-name> <language>");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            String message = args[0];
            Map<String, TextFrame> textFrameMap = blockFonts.getTextFrameMap();
            for (String name : textFrameMap.keySet()) {
                if (name.toLowerCase().contains(message.toLowerCase())) {
                    list.add(name);
                }
            }
        } else if (args.length == 2) {
            String message = args[1];
            List<Language> languages = blockFonts.getTriton().getLanguageManager().getAllLanguages();
            for (Language language : languages) {
                String name = language.getName();
                System.out.println(name);
                System.out.println(name.toLowerCase());
                System.out.println(name.contains(message));
                if (name.toLowerCase().contains(message.toLowerCase())) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}
