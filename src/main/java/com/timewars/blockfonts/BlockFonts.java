package com.timewars.blockfonts;

import com.rexcantor64.triton.api.Triton;
import com.rexcantor64.triton.api.TritonAPI;
import com.rexcantor64.triton.api.players.LanguagePlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.timewars.blockfonts.commands.*;
import com.timewars.blockfonts.files.ConfigManager;
import com.timewars.blockfonts.listeners.PlayerChangeLangEventListener;
import com.timewars.blockfonts.textFrame.TextFrame;
import com.timewars.blockfonts.textFrame.TextFrameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BlockFonts extends JavaPlugin {

    //HashMap of text frames
    private Map<String, TextFrame> textFrameMap;
    //Local configuration manager
    private ConfigManager configManager;
    //Plugins
    private PluginManager pluginManager;
    private Triton triton;
    private WorldEditPlugin worldEditPlugin;
    //Console for sending messages
    private ConsoleCommandSender console;

    @Override
    public void onEnable() {
        innitUtility();
        log("===============================================");
        innitStorage();
        registerCommands();
        registerEventListeners();
        connectToPlugins();
        printStatus();
        log("===============================================");
    }

    @Override
    public void onDisable() {
        log("===============================================");
        printStatus();
        log("===============================================");
    }

    //Initialize console and plugin manager
    public void innitUtility() {
        Server server = getServer();
        console = server.getConsoleSender();
        pluginManager = server.getPluginManager();
    }

    //initialize text frames from local storage
    public void innitStorage() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        configManager = new ConfigManager();
        textFrameMap = new HashMap<>();
        configManager.setupTextFrames(this);
    }

    //Register all commands
    public void registerCommands() {
        MainCommand textFrame = new MainCommand();
        textFrame.registerCommand("create", new CreateCommand(this));
        textFrame.registerCommand("remove", new RemoveCommand(this));
        textFrame.registerCommand("addLangFrame", new AddLangFrameCommand(this));
        textFrame.registerCommand("removeLangFrame", new RemoveLangFrameCommand(this));
        textFrame.registerCommand("list", new ListCommand(this));
        Objects.requireNonNull(getCommand("textFrame")).setExecutor(textFrame);
        Objects.requireNonNull(getCommand("textFrame")).setTabCompleter(textFrame);
    }

    //Register all event listeners
    public void registerEventListeners() {
        pluginManager.registerEvents(new PlayerChangeLangEventListener(this), this);
    }

    //Connect to depended plugins
    public void connectToPlugins() {
        triton = TritonAPI.getInstance();
        worldEditPlugin = (WorldEditPlugin) pluginManager.getPlugin("WorldEdit");

    }

    //Print status of the server
    public void printStatus() {
        log(ChatColor.AQUA + " BlockFonts: " + (this.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
    }

    //Send warped player message
    public void sendWarpedMessage(Player player, String message) {
        String warpedMessage = ChatColor.AQUA + "BlockFonts " + ChatColor.GOLD + "» " + ChatColor.RESET + message;
        player.sendMessage(warpedMessage);
    }

    //Load text frame from local storage
    public void loadTextFrame(String name, TextFrame textFrame) {
        textFrameMap.put(name, textFrame);
    }

    //Create new text frame
    public void createTextFrame(String name, Location max, Location min) {
        textFrameMap.put(name, new TextFrame(max, min));
        configManager.saveTextFrames();
    }

    //Add language bound to existing text frame
    public void addLangFrame(String name, String lang, Location max, Location min) {
        textFrameMap.get(name).addLangBound(lang, max, min);
        configManager.saveTextFrames();
    }

    //remove text frame
    public void removeTextFrame(String name) {
        textFrameMap.remove(name);
        configManager.saveTextFrames();
    }

    //remove language bound from existing text frame
    public void removeLangFrame(String name, String lang) {
        TextFrame textFrame = textFrameMap.get(name);
        textFrame.removeLangBound(lang);
    }

    //Update all text frames for the player
    public void updatePlayerFrames(LanguagePlayer languagePlayer) {
        Player player = Bukkit.getPlayer(languagePlayer.getUUID());
        new TextFrameRunnable(player.getDisplayName() + "-thread-" + languagePlayer.getLang().getName(), languagePlayer, this).start();
    }

    //Get Map of all text frames
    public Map<String, TextFrame> getTextFrameMap() {
        return textFrameMap;
    }

    //Send to player list of all current
    public void printListOfTextFrames(Player player) {
        StringBuilder result = new StringBuilder(ChatColor.DARK_GRAY + "-------[ " + ChatColor.AQUA + ChatColor.BOLD + "TextFrames" + ChatColor.DARK_GRAY + " ]-------");
        for (Map.Entry<String, TextFrame> entry : textFrameMap.entrySet()) {
            result.append("\n")
                    .append(ChatColor.AQUA + "" + ChatColor.BOLD + entry.getKey() + ": " + ChatColor.GRAY + "[")
                    .append(entry.getValue().getCreatedLanguages())
                    .append(ChatColor.GRAY + "]");
        }
        player.sendMessage(String.valueOf(result));
    }

    //Get triton plugin instance
    public Triton getTriton() {
        return triton;
    }

    //Get world edit plugin instance
    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    //Send message to server console
    public void log(String message) {
        console.sendMessage("[BlockFonts] " + message);
    }
}
