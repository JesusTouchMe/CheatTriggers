package cum.jesus.cheattriggers.scripting.triggers;

import cum.jesus.cheattriggers.CheatTriggers;

public enum TriggerType {
    // client
    TICK,
    SECOND,
    CHAT,
    GAME_LOAD,
    GAME_UNLOAD,

    // render

    // world

    // misc
    FORGE;

    public void triggerAll(Object[] args) {
        CheatTriggers.getScriptManager().trigger(this, args);
    }

    public void triggerAll() {
        triggerAll(new Object[0]);
    }
}
