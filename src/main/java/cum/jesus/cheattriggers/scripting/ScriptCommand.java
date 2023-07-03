package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.internal.command.Command;
import cum.jesus.cheattriggers.internal.command.CommandException;
import org.mozilla.javascript.Function;

public class ScriptCommand extends Command {
    private Function runFunction;
    private Script containingScript;

    public ScriptCommand(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void run(String alias, String[] args) throws CommandException {
        ScriptLoader.callFunction(runFunction, alias, args);
    }

    public void setRunFunction(Function runFunction) {
        this.runFunction = runFunction;
    }

    public void setContainingScript(Script containingScript) {
        this.containingScript = containingScript;
    }
}
