package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.command.Command;
import cum.jesus.cheattriggers.command.CommandException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptCommand extends Command {
    private Context context;
    private Scriptable scope;

    public ScriptCommand(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void run(String alias, String[] args) throws CommandException {
        ScriptableObject.callMethod(scope, "run", new Object[] {alias, args});
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Scriptable getScope() {
        return scope;
    }

    public void setScope(Scriptable scope) {
        this.scope = scope;
    }
}
