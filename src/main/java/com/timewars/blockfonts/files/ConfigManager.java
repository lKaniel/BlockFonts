package com.timewars.blockfonts.files;

import com.timewars.blockfonts.BlockFonts;
import com.timewars.blockfonts.textFrame.TextFrame;
import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class ConfigManager {

    private BlockFonts blockFonts;

    private File textFrames;

    public void innitTextFrames() throws IOException {
        textFrames.createNewFile();
        Map<String, Object> data = new HashMap<>();
        data.put("textFrames", new HashMap<String, Object>());

        Yaml textFramesYaml = new Yaml();
        StringWriter writer = new StringWriter();
        textFramesYaml.dump(data, writer);
        OutputStream textFramesOutputStream = new FileOutputStream(textFrames);

        textFramesOutputStream.write(writer.toString().getBytes());
        textFramesOutputStream.close();
    }

    public void setupTextFrames(BlockFonts blockFonts) {
        this.blockFonts = blockFonts;
        File folder = blockFonts.getDataFolder();
        textFrames = new File(folder, "textFrames.yml");

        if (!textFrames.exists()) {
            try {
                innitTextFrames();
            } catch (IOException e) {
                blockFonts.log(ChatColor.AQUA + " Text frames config: " + ChatColor.RED + "error");
                return;
            }
        }
        try {
            Yaml textFramesYaml = new Yaml();
            InputStream textFramesStream = new FileInputStream(textFrames);

            Map<String, Object> map = textFramesYaml.load(textFramesStream);

            for (Map.Entry<String, Map<String, Object>> entry: ((HashMap<String,Map<String, Object>>) map.get("textFrames")).entrySet()) {
                String name = entry.getKey();
                TextFrame textFrame = new TextFrame(blockFonts).fromMap(entry.getValue());
                blockFonts.loadTextFrame(name, textFrame);
            }
            textFramesStream.close();
            blockFonts.log(ChatColor.AQUA + " Text frames config: " + ChatColor.GREEN + "loaded");
        } catch (IOException e) {
            e.printStackTrace();
            blockFonts.log(ChatColor.AQUA + " Text frames config: " + ChatColor.RED + "error");
        }
    }

    public boolean saveTextFrames() {
        try {
            if (!textFrames.exists()) {
                try {
                    innitTextFrames();
                } catch (IOException e) {
                    blockFonts.log(ChatColor.RED + "Can't create text frames config file");
                    return false;
                }
            }

            Map<String, Object> textFrameMap = new HashMap<>();
            for (Map.Entry<String, TextFrame> entry : blockFonts.getTextFrameMap().entrySet()) {
                textFrameMap.put(entry.getKey(), entry.getValue().toMap());
            }

            Map<String, Object> data = new HashMap<>();

            data.put("textFrames", textFrameMap);

            Yaml textFramesYaml = new Yaml();
            StringWriter writer = new StringWriter();
            textFramesYaml.dump(data, writer);
            OutputStream textFramesOutputStream = new FileOutputStream(textFrames);

            textFramesOutputStream.write(writer.toString().getBytes());
            textFramesOutputStream.close();
            blockFonts.log(ChatColor.GREEN + "Successfully saved text frames config file");
            return true;
        } catch (IOException e) {
            blockFonts.log(ChatColor.RED + "Can't save text frames config file");
        }
        return false;
    }


}