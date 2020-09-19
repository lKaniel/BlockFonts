package com.timewars.blockfonts.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.timewars.blockfonts.BlockFonts;
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
import java.util.Objects;

public class WE6CreateCommand implements TabExecutor {

    BlockFonts blockFonts;

    public WE6CreateCommand(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String name = args[0];
                WorldEditPlugin worldEditPlugin = blockFonts.getWorldEditPlugin();
                try {
                    Selection selection = worldEditPlugin.getSelection(player);
                    Location maxLoc = selection.getMaximumPoint();
                    Location minLoc = selection.getMinimumPoint();
                    blockFonts.createTextFrame(name, maxLoc, minLoc);
                    blockFonts.sendWarpedMessage(player, ChatColor.GRAY + "You successfully created text frame " + ChatColor.AQUA + ChatColor.BOLD + name);
                } catch (Exception e) {
                    blockFonts.sendWarpedMessage(player, ChatColor.RED + "You have to select a region to create text frame");
                }
                return true;
            }
            blockFonts.sendWarpedMessage(player, ChatColor.RED + "Wrong command usage, try:\n/textFrame create <frame-name>");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("<frame-name>");
        }
        return list;
    }
}
