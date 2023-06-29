package cum.jesus.cheattriggers.internal.commands;

import cum.jesus.cheattriggers.command.Command;
import cum.jesus.cheattriggers.command.CommandException;
import cum.jesus.cheattriggers.utils.ChatUtils;

public class CtCommand extends Command {
    public CtCommand() {
        super("cheattriggers", "ct");
        setDescription("Main command for the whole mod");
    }

    @Override
    public void run(String alias, String[] args) throws CommandException {
        ChatUtils.sendPrefixMessage("test");
    }
}
