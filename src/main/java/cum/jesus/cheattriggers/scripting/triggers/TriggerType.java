package cum.jesus.cheattriggers.scripting.triggers;

import cum.jesus.cheattriggers.CheatTriggers;

public enum TriggerType {
    TICK,
    SECOND,

    // misc
    FORGE;

    public void triggerAll(Object[] args) {
        CheatTriggers.getScriptManager().trigger(this, args);
    }
}
