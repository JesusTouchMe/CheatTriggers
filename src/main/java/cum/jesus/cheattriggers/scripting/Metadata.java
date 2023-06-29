package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.command.Command;
import cum.jesus.cheattriggers.module.Module;

import java.util.ArrayList;
import java.util.List;

public class Metadata {
    /**
     * The name of the script
     */
    public String name;

    /**
     * The description of the script
     */
    public String description;

    /**
     * The version (in string form) of the script
     */
    public String version;

    /**
     * The author of the script
     */
    public String author;

    /**
     * The index file for the script. The index is the file that will actually be run when loading the script
     */
    public String index;

    /**
     * A list of the modules for the script
     */
    public List<Module> modules = new ArrayList<>();

    /**
     * A list of the commands for the script
     */
    public List<Command> commands = new ArrayList<>();
}
