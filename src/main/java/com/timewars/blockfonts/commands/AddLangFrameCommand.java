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

public class AddLangFrameCommand implements TabExecutor {

    BlockFonts blockFonts;

    public AddLangFrameCommand(BlockFonts blockFonts) {
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
                WorldEditPlugin worldEditPlugin = blockFonts.getWorldEditPlugin();
                try {
                    Region region = worldEditPlugin.getSession(player).getSelection(worldEditPlugin.getSession(player).getSelectionWorld());
                    World world = Bukkit.getWorld(Objects.requireNonNull(region.getWorld()).getName());
                    BlockVector3 max = region.getMaximumPoint();
                    int x1 = max.getX();
                    int y1 = max.getY();
                    int z1 = max.getZ();
                    Location maxLoc = new Location(world, x1, y1, z1);
                    BlockVector3 min = region.getMinimumPoint();
                    int x2 = min.getX();
                    int y2 = min.getY();
                    int z2 = min.getZ();
                    Location minLoc = new Location(world, x2, y2, z2);
                    Language language = blockFonts.getTriton().getLanguageManager().getLanguageByName(lang, true);
                    blockFonts.addLangFrame(name, lang, maxLoc, minLoc);
                    blockFonts.sendWarpedMessage(player, ChatColor.GRAY + "You successfully added lang " + language.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + " to the text frame " + ChatColor.AQUA + ChatColor.BOLD + name);
                    return true;
                } catch (IncompleteRegionException e) {
                    blockFonts.sendWarpedMessage(player, ChatColor.RED + "You have to select a region to add language to text frame");
                    return false;
                }
            }
            blockFonts.sendWarpedMessage(player, ChatColor.RED + "Wrong command usage, try:\n/textFrame addLangFrame <frame-name> <language>");
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
                if (name.toLowerCase().contains(message.toLowerCase())) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}
