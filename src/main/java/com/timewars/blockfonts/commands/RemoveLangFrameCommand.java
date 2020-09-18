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
                    blockFonts.sendWarpedMessage(player, ChatColor.RED + "Text frame with name " + ChatColor.AQUA + ChatColor.BOLD + name + ChatColor.RESET + ChatColor.RED + " doesn't exists");
                    return false;
                }
                String lang = args[1];
                List<Language> languages = blockFonts.getTriton().getLanguageManager().getAllLanguages();
                boolean isValid = false;
                for (Language language : languages) {
                    if (language.getName().equalsIgnoreCase(lang)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    blockFonts.sendWarpedMessage(player, ChatColor.RED + "Language with code " + ChatColor.DARK_AQUA + ChatColor.BOLD + lang + ChatColor.RESET + ChatColor.RED + " doesn't exists");
                    return false;
                }
                blockFonts.removeLangFrame(name, lang);
                Language language = blockFonts.getTriton().getLanguageManager().getLanguageByName(lang, true);
                blockFonts.sendWarpedMessage(player, ChatColor.GRAY + "You successfully removed lang " + ChatColor.AQUA + ChatColor.BOLD + language.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + " from text frame " + ChatColor.AQUA + ChatColor.BOLD + name);
                return true;
            }
            blockFonts.sendWarpedMessage(player, ChatColor.RED + "Wrong command usage, try " + ChatColor.AQUA + "/textFrame addLangFrame <frame-name> <language>");
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
