package cum.jesus.cheattriggers.scripting.triggers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class TriggerRegistry {
    private static final Map<String, Method> methodMap = new LinkedHashMap<>();

    public static Trigger register(Object triggerType, Object method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (triggerType instanceof Class) {
            return new ForgeTrigger(method, (Class<?>) triggerType);
        }

        assert triggerType instanceof String : "A String or a Class is required for triggers";

        Method meth = methodMap.get((String) triggerType);
        if (meth == null) {
            String name = ((String) triggerType).toLowerCase();
            Method[] methods = TriggerRegistry.class.getDeclaredMethods();
            for (Method it : methods) {
                if (it.getName().equalsIgnoreCase("register" + name)) {
                    meth = it;
                    break;
                }
            }
            if (meth == null) throw new NoSuchMethodException("No trigger type named '" + triggerType + "'");
        }

        return (Trigger) meth.invoke(null, method);
    }

    /**
     * Register a trigger which runs every game tick
     * <br><br>
     * Passes 1 argument:
     * <p> - Elapsed ticks
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerTick(Object method) {
        return new RegularTrigger(method, TriggerType.TICK);
    }

    /**
     * Register a trigger which runs every 20 game ticks (every second)
     * <br><br>
     * Passes 1 argument:
     * <p> - Elapsed seconds
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerSecond(Object method) {
        return new RegularTrigger(method, TriggerType.SECOND);
    }

    /**
     * Register a trigger that runs before a chat message is received
     * <br><br>
     * Passes 2 arguments
     * <p> - The unformatted chat message
     * <p> - The ClientChatReceivedEvent
     *
     * @param method
     * @return
     */
    public static ChatTrigger registerChat(Object method) {
        return new ChatTrigger(method, TriggerType.CHAT);
    }

    /**
     * Register a trigger which will run once the game has loaded and after the mod has been reloaded
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerGameLoad(Object method) {
        return new RegularTrigger(method, TriggerType.GAME_LOAD);
    }

    /**
     * Register a trigger which will run before the JVM shuts down or when the mod gets reloaded
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerGameUnload(Object method) {
        return new RegularTrigger(method, TriggerType.GAME_UNLOAD);
    }
}
