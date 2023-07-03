package cum.jesus.cheattriggers.scripting.triggers;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class ForgeTrigger extends Trigger {
    private static final Map<Class<?>, SortedSet<ForgeTrigger>> forgeTriggers = new LinkedHashMap<>();

    private final Class<?> eventClass;

    public ForgeTrigger(Object method, Class<?> eventClass) {
        super(method, TriggerType.FORGE);

        this.eventClass = eventClass;

        assert Event.class.isAssignableFrom(eventClass) : "ForgeTrigger expects an event class";

        forgeTriggers.computeIfAbsent(eventClass, k -> new TreeSet<>()).add(this);
    }

    @Override
    public Trigger register() {
        forgeTriggers.computeIfAbsent(eventClass, k -> new TreeSet<>()).add(this);
        return super.register();
    }

    @Override
    public Trigger unregister() {
        forgeTriggers.get(eventClass).remove(this);
        return super.unregister();
    }

    public static void unregisterTriggers() {
        for (SortedSet<ForgeTrigger> triggerSet : forgeTriggers.values()) {
            for (ForgeTrigger trigger : triggerSet) {
                trigger.unregister();
            }
        }
        forgeTriggers.clear();
    }

    @Override
    public void trigger(Object[] args) {
        callMethod(args);
    }

    public static class ForgeEventListener {
        @SubscribeEvent
        public void onEvent(Event event) {
            if (Thread.currentThread().getName().equals("Server thread")) return; // prevent single player issue

            SortedSet<ForgeTrigger> triggers = forgeTriggers.get(event.getClass());
            if (triggers != null) {
                for (ForgeTrigger trigger : triggers) {
                    trigger.trigger(new Object[] {event});
                }
            }
        }
    }

    public static ForgeEventListener eventListener = new ForgeEventListener();
}
