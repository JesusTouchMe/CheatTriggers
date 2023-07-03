package cum.jesus.cheattriggers.scripting.triggers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TriggerRegistry {
    private static Map<String, Method> methodMap = new LinkedHashMap<>();

    public static Trigger register(Object triggerType, Object method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (triggerType instanceof Class) {
            return new ForgeTrigger(method, (Class<?>) triggerType);
        }

        assert triggerType instanceof String : "A String or a Class is required for register()";

        Method meth = methodMap.get((String) triggerType);
        if (meth == null) {
            String name = ((String) triggerType).toLowerCase();
            List<Method> methods = Arrays.asList(TriggerRegistry.class.getDeclaredMethods());
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
     *
     * Passes 1 argument:
     * - Elapsed ticks
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerTick(Object method) {
        return new RegularTrigger(method, TriggerType.TICK);
    }

    /**
     * Register a trigger which runs every 20 game ticks (every second)
     *
     * Passes 1 argument:
     * - Elapsed seconds
     *
     * @param method The method to call on event fire
     * @return The trigger
     */
    public static RegularTrigger registerSecond(Object method) {
        return new RegularTrigger(method, TriggerType.SECOND);
    }
}
