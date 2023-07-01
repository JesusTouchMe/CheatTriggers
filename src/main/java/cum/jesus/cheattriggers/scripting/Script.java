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
}
