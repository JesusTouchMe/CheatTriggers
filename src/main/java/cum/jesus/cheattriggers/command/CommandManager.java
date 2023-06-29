package cum.jesus.cheattriggers.command;

import cum.jesus.cheattriggers.utils.ChatUtils;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();

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
                ChatUtils.sendPrefixMessage(name + " is not a command. Run '-help' for a list of all commands");
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
}
