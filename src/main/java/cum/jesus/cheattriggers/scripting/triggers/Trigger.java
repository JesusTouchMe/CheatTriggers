package cum.jesus.cheattriggers.scripting.triggers;

import cum.jesus.cheattriggers.scripting.ScriptLoader;
import org.jetbrains.annotations.NotNull;

public abstract class Trigger implements Comparable<Trigger> {
    private Object method;
    private TriggerType triggerType;

    private Priority priority = Priority.NORMAL;

    protected Trigger(Object method, TriggerType triggerType) {
        this.method = method;
        this.triggerType = triggerType;

        register();
    }

    public final Trigger setPriority(Priority priority) {
        this.priority = priority;

        unregister();
        register();

        return this;
    }

    public Trigger register() {
        ScriptLoader.addTrigger(this);
        return this;
    }

    public Trigger unregister() {
        ScriptLoader.removeTrigger(this);
        return this;
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

    @Override
    public int compareTo(@NotNull Trigger other) {
        int ordCmp = priority.ordinal() - other.priority.ordinal();
        return ordCmp == 0 ? hashCode() - other.hashCode() : ordCmp;
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
