package cum.jesus.cheattriggers.scripting;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ScriptLoader {
    private static Context scriptContext;
    private static Context evalContext;
    private static Scriptable scope;

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
                String indexCode = null;

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

                eval(indexCode);
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
        return wrapInContext(evalContext, () -> {
            return Context.toString(evalContext.evaluateString(scope, code, "<eval>", 1, null));
        });
    }
}
