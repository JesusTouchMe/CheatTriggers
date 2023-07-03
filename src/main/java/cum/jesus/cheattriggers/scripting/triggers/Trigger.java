package cum.jesus.cheattriggers.scripting.triggers;

import cum.jesus.cheattriggers.scripting.ScriptLoader;

public abstract class Trigger {
    private Object method;
    private TriggerType triggerType;

    private Priority priority = Priority.NORMAL;

    protected Trigger(Object method, TriggerType triggerType) {
        this.method = method;
        this.triggerType = triggerType;

        register();
    }

    public final void setPriority(Priority priority) {
        this.priority = priority;

        unregister();
        register();
    }

    public void register() {
        ScriptLoader.addTrigger(this);
    }

    public void unregister() {
        ScriptLoader.removeTrigger(this);
    }

    protected void callMethod(Object[] args) {
        ScriptLoader.trigger(this, method, args);
    }

    public abstract void trigger(Object[] args);

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public Priority getPriority() {
        return priority;
    }

    /**
     * Higher is run first
     */
    public enum Priority {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST
    }
}
