package com.timewars.blockfonts.commands;

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

public class RemoveCommand implements TabExecutor {

    BlockFonts blockFonts;

    public RemoveCommand(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String name = args[0];
                if (blockFonts.getTextFrameMap().containsKey(name)) {
                    blockFonts.removeTextFrame(name);
                    blockFonts.sendWarpedMessage(player, ChatColor.GRAY + "You successfully removed text frame " + ChatColor.AQUA + ChatColor.BOLD + name);
                    return true;
                } else {
                    blockFonts.sendWarpedMessage(player, ChatColor.RED + "Text frame with name " + ChatColor.AQUA + ChatColor.BOLD + name + ChatColor.RESET + ChatColor.RED + " doesn't exist");
                }
            }
            blockFonts.sendWarpedMessage(player, ChatColor.RED + "Wrong command usage, try:\n/textFrame remove <frame-name>");
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
        }
        return list;
    }
}
