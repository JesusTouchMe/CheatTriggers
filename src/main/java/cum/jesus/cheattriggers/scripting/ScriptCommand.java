package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.command.Command;
import cum.jesus.cheattriggers.command.CommandException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class ScriptCommand extends Command {
    private ScriptEngine engine;

    public ScriptCommand(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void run(String alias, String[] args) throws CommandException {
        try {
            ((Invocable)engine).invokeFunction("run", alias, args);
        } catch (NoSuchMethodException ignored) {
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }
}
