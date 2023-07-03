package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.scripting.triggers.Trigger;
import cum.jesus.cheattriggers.scripting.triggers.TriggerType;
import org.mozilla.javascript.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ScriptLoader {
    private static final Map<TriggerType, ConcurrentSkipListSet<Trigger>> triggers = new ConcurrentHashMap<>();

    private static Context scriptContext;
    private static Context evalContext;
    private static Scriptable scope;

    public static void addTrigger(Trigger trigger) {
        ConcurrentSkipListSet<Trigger> triggerList = triggers.get(trigger.getTriggerType());
        if (triggerList == null) {
            triggerList = new ConcurrentSkipListSet<>();
            triggers.put(trigger.getTriggerType(), triggerList);
        }

        triggerList.add(trigger);
    }

    public static void clearTriggers() {
        triggers.clear();
    }

    public static void removeTrigger(Trigger trigger) {
        triggers.get(trigger.getTriggerType()).remove(trigger);
    }

    public static void trigger(Trigger trigger, Object method, Object[] args) {
        wrapInContext(() -> {
           try {
               assert method instanceof Function : "An actual function has to be passed";

               ((Function) method).call(Context.getCurrentContext(), scope, scope, args);
           } catch (Throwable e) {
               e.printStackTrace();
               removeTrigger(trigger);
           }

           return null;
        });
    }

    public static void execTriggerType(TriggerType type, Object[] args) {
        triggers.get(type).forEach(trigger -> trigger.trigger(args));
    }

    public static void instanceContexts() {
        scriptContext = ScriptContextFactory.getGlobal().enterContext();
        scope = new ImporterTopLevel(scriptContext);

        Context.exit();

        ScriptContextFactory.optimize = false;
        evalContext = ScriptContextFactory.getGlobal().enterContext();
        Context.exit();
        ScriptContextFactory.optimize = true;
    }

    public static void setup() {
        instanceContexts();

        wrapInContext(() -> {
            File asmLibFile = new File(ScriptLoader.class.getClassLoader().getResource("std/asm.js").toURI());
            String asmLib = new String(Files.readAllBytes(asmLibFile.toPath()));

            try {
                scriptContext.evaluateString(scope, asmLib, "asmProvidedLib", 1, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public static void clean() {
        try {
            Context.exit();
            scriptContext = null;
            evalContext = null;
            scope = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void indexSetup() {
        wrapInContext(() -> {
            File stdLibFile = new File(ScriptLoader.class.getClassLoader().getResource("std/javascriptstd.js").toURI());
            String stdLib = new String(Files.readAllBytes(stdLibFile.toPath()));

            try {
                scriptContext.evaluateString(scope, stdLib, "stdLib", 1, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public static void indexPass(Script script, File file) {
        wrapInContext(() -> {
            try {
                boolean isZipped = !file.isDirectory();
                String indexCode;

                if (isZipped) {
                    ZipFile zipFile = new ZipFile(file);
                    ZipEntry index = zipFile.getEntry(script.getMetadata().index);
                    if (index == null) throw new RuntimeException(script.getMetadata().index + " is not in " + file.getName());
                    InputStream stream = zipFile.getInputStream(index);
                    indexCode = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                } else {
                    assert file.isDirectory();
                    File index = new File(file, script.getMetadata().index);
                    if (!index.exists()) throw new RuntimeException(script.getMetadata().index + " is not in " + file.getName());
                    indexCode = new String(Files.readAllBytes(index.toPath()));
                }

                scope.put("script", scope, script);

                scriptContext.evaluateString(scope, indexCode, script.getMetadata().index, 1, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public static <T> T wrapInContext(Context context, Callable<T> block) {
        boolean missingContext = Context.getCurrentContext() == null;
        if (missingContext) {
            try {
                ScriptContextFactory.getGlobal().enterContext(context);
            } catch (Throwable e) {
                ScriptContextFactory.getGlobal().enterContext();
            }
        }

        try {
            return block.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (missingContext) Context.exit();
        }
    }

    public static <T> T wrapInContext(Callable<T> block) {
        return wrapInContext(scriptContext, block);
    }

    public static String eval(String code) {
        return wrapInContext(evalContext, () -> Context.toString(evalContext.evaluateString(scope, code, "<eval>", 1, null)));
    }

    public static void callFunction(Function func, Object... args) {
        wrapInContext(() -> {
            func.call(Context.getCurrentContext(), scope, scope, args);
            return null;
        });
    }
}
