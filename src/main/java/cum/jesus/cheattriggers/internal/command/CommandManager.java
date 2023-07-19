package cum.jesus.cheattriggers.internal.command;

import cum.jesus.cheattriggers.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public void addCommand(Command c) {
        commands.add(c);
    }

    public void removeCommand(Command c) {
        commands.remove(c);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Command getCommandByName(String name) {
        return commands.stream().filter(c -> c.matchName(name)).findFirst().orElse(null);
    }

    /**
     * @return True if successfully executed. False otherwise
     */
    public boolean exec(String command) {
        String rawCommand = command.substring(1); // this is to remove the - in the beginning of the cmd (will be custom soon)
        String[] splitCommand = rawCommand.split(" ");

        if (splitCommand.length == 0) return false; // no command lol

        String name = splitCommand[0];
        Command cmd = getCommandByName(name);

        try {
            if (cmd == null) {
                ChatUtils.sendPrefixMessage("§c" + name + " is not a command. Run '-ct help' for a list of all commands");
                return false;
            }

            String[] args = new String[splitCommand.length - 1];
            System.arraycopy(splitCommand, 1, args, 0, splitCommand.length - 1);

            cmd.run(name, args);
            return true;
        } catch (CommandException e) {
            ChatUtils.sendPrefixMessage("§c" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public void clearCommands() {
        commands.clear();
    }
}
