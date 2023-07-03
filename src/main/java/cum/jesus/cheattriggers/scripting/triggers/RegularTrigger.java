package cum.jesus.cheattriggers.scripting.triggers;

public class RegularTrigger extends Trigger {

    protected RegularTrigger(Object method, TriggerType triggerType) {
        super(method, triggerType);
    }

    @Override
    public void trigger(Object[] args) {
        callMethod(args);
    }
}
