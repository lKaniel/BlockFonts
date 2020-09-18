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
                    player.sendMessage(ChatColor.RED + "Text frame with name " + name + " doesn't exists");
                }
                String lang = args[1];
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
                    blockFonts.addLangFrame(name, lang, maxLoc, minLoc);
                    player.sendMessage(ChatColor.GREEN + "You successfully added language text frame " + name);
                    return true;
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "You have to select a region to add language to text frame");
                    return false;
                }
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
            for (String name : textFrameMap.keySet()){
                if (name.toLowerCase().contains(message.toLowerCase())){
                    list.add(name);
                }
            }
        }else if (args.length == 2){
            String message = args[1];
            List<Language> languages = blockFonts.getTriton().getLanguageManager().getAllLanguages();
            for (Language language : languages){
                String name = language.getName();
                System.out.println(name);
                System.out.println(name.toLowerCase());
                System.out.println(name.contains(message));
                if (name.toLowerCase().contains(message.toLowerCase())){
                    list.add(name);
                }
            }
        }
        return list;
    }
}
