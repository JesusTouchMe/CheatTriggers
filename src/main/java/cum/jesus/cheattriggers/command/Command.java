package cum.jesus.cheattriggers.command;

import cum.jesus.cheattriggers.CheatTriggers;
import net.minecraft.client.Minecraft;

public abstract class Command {
    protected final Minecraft mc = CheatTriggers.mc;

    private final String name;
    private String description;
    private final String[] aliases;

    protected Command(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }
}
