package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.command.Command;

public class Script {
    private final Metadata metadata;

    public Script(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void register() {
        for (Command cmd : metadata.commands) {
            cmd.setDescription("(" + metadata.name + ") ");

            CheatTriggers.getCommandManager().addCommand(cmd);
        }
    }
}
