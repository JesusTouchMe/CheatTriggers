package cum.jesus.cheattriggers;

import com.google.gson.Gson;
import cum.jesus.cheattriggers.internal.command.CommandManager;
import cum.jesus.cheattriggers.internal.ConfigManager;
import cum.jesus.cheattriggers.internal.FileManager;
import cum.jesus.cheattriggers.internal.InternalCommandManager;
import cum.jesus.cheattriggers.scripting.ScriptManager;
import cum.jesus.cheattriggers.utils.Logger;
import net.minecraft.client.Minecraft;

public final class CheatTriggers {
    public static final String MODID = "cheattriggers";
    public static final String NAME = "CheatTriggers";
    public static final String VERSION = "1.0";

    private static boolean loaded = false;

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final Gson gson = new Gson();

    private static FileManager fileManager;
    private static ConfigManager configManager;
    private static CommandManager commandManager;
    private static ScriptManager scriptManager;

    //<editor-fold desc="Getters">
    public static final boolean isLoaded() {
        return loaded;
    }

    public static final FileManager getFileManager() {
        return fileManager;
    }

    public static final ConfigManager getConfigManager() {
        return configManager;
    }

    public static final CommandManager getCommandManager() {
        return commandManager;
    }

    public static final ScriptManager getScriptManager() {
        return scriptManager;
    }
    //</editor-fold>

    public static void preLoad() {
        loaded = false;

        fileManager = new FileManager();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
        scriptManager = new ScriptManager();
    }

    public static void load() {
        Logger.debug("loading");

        InternalCommandManager.addAll();

        scriptManager.setup();
        scriptManager.entryPass();

        loaded = true;
    }

    public static void unload() {
        commandManager.clearCommands();
        scriptManager.clean();

        fileManager = null;
        configManager = null;
        commandManager = null;
        scriptManager = null;

        loaded = false;
        System.gc();
    }

    public static void reload() {
        unload();
        preLoad();
        load();
    }
}
