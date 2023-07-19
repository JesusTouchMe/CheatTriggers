package cum.jesus.cheattriggers.internal.command.commands;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.internal.command.Command;
import cum.jesus.cheattriggers.internal.command.CommandException;
import cum.jesus.cheattriggers.internal.config.Gui;
import cum.jesus.cheattriggers.utils.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CtCommand extends Command {
    public CtCommand() {
        super("cheattriggers", "ct");
        setDescription("Main command for the whole mod");
        setHelp(getDescription());
    }

    @Override
    public void run(String alias, String[] _args) throws CommandException {
        if (_args.length < 1) {
            CheatTriggers.displayGuiScreen(new Gui());
            return;
        }

        List<String> args = new ArrayList<>(Arrays.asList(_args));
        String cmd = args.get(0);
        args.remove(0);

        switch (cmd.toLowerCase()) {
            case "help":
                if (args.size() < 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("§l§7All CheatTriggers commands\n§r");
                    for (Command c : CheatTriggers.getCommandManager().getCommands()) {
                        sb.append("§b").append(c.getName()).append(" §7- §o").append(c.getHelp() != null ? c.getHelp() : "No help found").append("\n§r");
                    }

                    if (sb.toString().endsWith("\n§r")) {
                        sb.deleteCharAt(sb.length() - 3);
                    }

                    ChatUtils.sendMessage(sb.toString());
                } else {
                    String commandName = args.get(0);
                    Command command = CheatTriggers.getCommandManager().getCommandByName(commandName);

                    if (command == null)
                        throw new CommandException("The command: '" + commandName + "' does not exist");

                    StringBuilder sb = new StringBuilder();
                    sb.append("§l§7Displaying help for ").append(command.getName()).append("\n§r");
                    if (command.getHelp() != null) sb.append("§7§o").append(command.getHelp()).append("\n§r");

                    if (command.getAliases() != null) {
                        sb.append("\n§7Aliases:\n§r");
                        for (String a : command.getAliases())
                            sb.append("§b- ").append(a).append("\n§r");
                    }

                    if (command.getDescription() != null && !Objects.equals(command.getDescription(), command.getHelp())) sb.append("\n").append("§7").append(command.getDescription()).append("§r");

                    if (sb.toString().endsWith("\n§r")) {
                        sb.deleteCharAt(sb.length() - 3);
                    }

                    ChatUtils.sendMessage(sb.toString());
                }

                break;
            case "reload":
                CheatTriggers.reload();
                break;
        }
    }
}
