package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.command.Command;
import cum.jesus.cheattriggers.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ScriptMetadata {
    public String name = null;
    public String description = null;
    public String version = null;
    public String author = null;
    public String index = null;

    public ScriptMetadata(String name, String description, String version, String author, String index) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.index = index;
    }

    public ScriptMetadata() {
    }
}
