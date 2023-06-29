package cum.jesus.cheattriggers.internal;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.internal.commands.*;

/**
 * Command manager for the internal (aka mod) commands
 */
public class InternalCommandManager {
    public static void addAll() {
        CheatTriggers.getCommandManager().addCommand(new CtCommand());
    }
}
