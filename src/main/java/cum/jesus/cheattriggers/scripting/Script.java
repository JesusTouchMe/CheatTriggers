package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.utils.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Script {
    private final ScriptMetadata metadata;
    private final File file;

    public final List<ScriptCommand> commandList = new ArrayList<>();

    public Script(ScriptMetadata metadata, File file) {
        this.metadata = metadata;
        this.file = file;
    }

    public ScriptMetadata getMetadata() {
        return metadata;
    }

    public File getFile() {
        return file;
    }

    /**
     * Adds a custom command to this script
     * @param commandMeta The object containing the command info and run function.<br>Accepted fields are: "name" (string), "description" (string), "aliases" (js array), "run" (js function)
     */
    public void addCommand(NativeObject commandMeta) {
        Function runFun = (Function) commandMeta.get("run");
        String cmdName = (String) commandMeta.get("name");
        String cmdDescription = (String) commandMeta.get("description");
        NativeArray cmdAliases = (NativeArray) commandMeta.get("aliases");

        String[] aliases = (String[]) cmdAliases.stream().toArray(String[]::new);

        ScriptCommand cmd = new ScriptCommand(cmdName, aliases);
        if (cmdDescription != null) cmd.setDescription(cmdDescription);

        cmd.setRunFunction(runFun);
        cmd.setContainingScript(this);

        commandList.add(cmd);
        CheatTriggers.getCommandManager().addCommand(cmd);
    }

    public void addModule(NativeObject moduleMeta) {

    }

    public void on(Object trigger, Object triggerFunction) {

    }
}
