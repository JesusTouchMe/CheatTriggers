package cum.jesus.cheattriggers;

import com.google.gson.Gson;
import cum.jesus.cheattriggers.command.CommandManager;
import cum.jesus.cheattriggers.internal.ConfigManager;
import cum.jesus.cheattriggers.internal.FileManager;
import cum.jesus.cheattriggers.internal.InternalCommandManager;
import cum.jesus.cheattriggers.scripting.ScriptManager;
import cum.jesus.cheattriggers.utils.Logger;
import net.minecraft.client.Minecraft;

public class CheatTriggers {
    public static final String MODID = "cheattriggers";
    public static final String NAME = "CheatTriggers";
    public static final String VERSION = "1.0";

    public static boolean isLoaded = false;

    public static Minecraft mc = Minecraft.getMinecraft();
    public static Gson gson = new Gson();

    private static FileManager fileManager;
    private static ConfigManager configManager;
    private static CommandManager commandManager;
    private static ScriptManager scriptManager;

    //<editor-fold desc="Getters">
    public static FileManager getFileManager() {
        return fileManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static ScriptManager getScriptManager() {
        return scriptManager;
    }

    //</editor-fold>

    public static void preLoad() {
        isLoaded = false;

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

        isLoaded = true;
    }

    public static void unload() {
        fileManager = null;

        isLoaded = false;
    }

    public static void reload() {
        unload();
        preLoad();
        load();
    }
}
