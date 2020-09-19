package com.timewars.blockfonts.textFrame;

import com.rexcantor64.triton.api.language.Language;
import com.timewars.blockfonts.BlockFonts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextFrame {
    BlockFonts blockFonts;

    //Bound of the text frame
    Bound mainBound;

    //Map of all bounds of the text frame
    Map<String, Bound> langBounds;

    public TextFrame(Location max, Location min, BlockFonts blockFonts) {
        this.mainBound = new Bound(max, min);
        this.langBounds = new HashMap<>();
        this.blockFonts = blockFonts;
    }

    public TextFrame(BlockFonts blockFonts) {
        this.langBounds = new HashMap<>();
        this.blockFonts = blockFonts;
    }

    //Add language bound to the text frame
    public void addLangBound(String lang, Location max, Location min) {
        Bound bound = new Bound(max, min);
        langBounds.put(lang, bound);
    }

    //remove language bound from the text frame
    public void removeLangBound(String lang) {
        langBounds.remove(lang);
    }

    //Update text frame for the player
    synchronized public void replaceWithLang(String lang, Player player) {
        Bound bound = langBounds.get(lang);
        if (bound == null) {
            bound = langBounds.get("en_GB");
        }
        Location max = mainBound.getLesserCorner();
        int x1 = (int) max.getX();
        int y1 = (int) max.getY();
        int z1 = (int) max.getZ();
        Location min = mainBound.getGreaterCorner();
        int x2 = (int) min.getX();
        int y2 = (int) min.getY();
        int z2 = (int) min.getZ();
        World mainWorld = mainBound.getWorld();
        World langWorld = bound.getWorld();
        min = bound.getGreaterCorner();
        int x3 = (int) min.getX();
        int y3 = (int) min.getY();
        int z3 = (int) min.getZ();
        for (int x = 0; x < x1 - x2 + 1; x++) {
            for (int y = 0; y < y1 - y2 + 1; y++) {
                for (int z = 0; z < z1 - z2 + 1; z++) {
                    Block block = langWorld.getBlockAt(x + x3, y + y3, z + z3);
                    Location location = new Location(mainWorld, x + x2, y + y2, z + z2);
                    if (blockFonts.checkNewApi()) {
                        player.sendBlockChange(location, block.getBlockData());
                    }else{

                        player.sendBlockChange(location, block.getType(), block.getState().getRawData());
                    }
                }
            }
        }
    }

    public String getCreatedLanguages() {
        List<Language> languages = blockFonts.getTriton().getLanguageManager().getAllLanguages();
        boolean containsAll = true;
        for (Language language : languages) {
            if (!langBounds.containsKey(language.getName())) {
                containsAll = false;
                break;
            }
        }
        StringBuilder result = new StringBuilder(ChatColor.RESET + "" + ChatColor.GRAY + "langs: " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "All");
        if (!containsAll) {
            result = new StringBuilder(ChatColor.RESET + "" + ChatColor.GRAY + "langs: ");
            for (String lang : langBounds.keySet()) {
                Language language = blockFonts.getTriton().getLanguageManager().getLanguageByName(lang, true);
                result.append(language.getDisplayName()).append(" ");
            }
        }
        return String.valueOf(result);
    }

    @Override
    public String toString() {
        return "TextFrame{" +
                "mainBound=" + mainBound +
                ", langBounds=" + langBounds +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        HashMap<String, Object> boundMap = new HashMap<>();
        boundMap.put("x", mainBound.getX());
        boundMap.put("y", mainBound.getY());
        boundMap.put("z", mainBound.getZ());
        boundMap.put("x1", mainBound.getX2());
        boundMap.put("y1", mainBound.getY2());
        boundMap.put("z1", mainBound.getZ2());
        boundMap.put("world", mainBound.getWorld().getName());
        map.put("mainBound", boundMap);
        ArrayList<Map<String, Object>> langBoundsList = new ArrayList<>();
        for (Map.Entry<String, Bound> entry : langBounds.entrySet()) {
            Map<String, Object> langBoundMap = new HashMap();
            String name = entry.getKey();
            langBoundMap.put("name", name);
            Bound bound = entry.getValue();
            langBoundMap.put("x", bound.getX());
            langBoundMap.put("y", bound.getY());
            langBoundMap.put("z", bound.getZ());
            langBoundMap.put("x1", bound.getX2());
            langBoundMap.put("y1", bound.getY2());
            langBoundMap.put("z1", bound.getZ2());
            langBoundMap.put("world", bound.getWorld().getName());
            langBoundsList.add(langBoundMap);
        }
        map.put("langBounds", langBoundsList);
        return map;
    }

    public TextFrame fromMap(Map<String, Object> map) {
        Map<String, Object> boundMap = (HashMap<String, Object>) map.get("mainBound");
        int x = (int) boundMap.get("x");
        int y = (int) boundMap.get("y");
        int z = (int) boundMap.get("z");
        int x1 = (int) boundMap.get("x1");
        int y1 = (int) boundMap.get("y1");
        int z1 = (int) boundMap.get("z1");
        String worldName = (String) boundMap.get("world");
        this.mainBound = new Bound(worldName, x, y, z, x1, y1, z1);
        ArrayList<Map<String, Object>> langBoundsList = (ArrayList<Map<String, Object>>) map.get("langBounds");
        for (Map<String, Object> langBoundMap : langBoundsList) {
            x = (int) langBoundMap.get("x");
            y = (int) langBoundMap.get("y");
            z = (int) langBoundMap.get("z");
            x1 = (int) langBoundMap.get("x1");
            y1 = (int) langBoundMap.get("y1");
            z1 = (int) langBoundMap.get("z1");
            worldName = (String) langBoundMap.get("world");
            String name = (String) langBoundMap.get("name");
            this.langBounds.put(name, new Bound(worldName, x, y, z, x1, y1, z1));
        }
        return this;
    }
}
