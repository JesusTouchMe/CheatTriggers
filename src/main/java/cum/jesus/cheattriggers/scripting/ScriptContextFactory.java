package cum.jesus.cheattriggers.scripting;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScriptContextFactory extends ContextFactory {
    private static ModifiedURLClassLoader classLoader = new ModifiedURLClassLoader();
    private static ScriptContextFactory global = new ScriptContextFactory();

    public static boolean optimize = true;

    @Override
    protected void onContextCreated(Context cx) {
        super.onContextCreated(cx);

        cx.setApplicationClassLoader(classLoader);
        cx.setOptimizationLevel(optimize ? 9 : 0);
        cx.setLanguageVersion(Context.VERSION_ES6);
        cx.setWrapFactory(new WrapFactory() {
            @Override
            public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
                if (obj instanceof Collection) {
                    return super.wrap(cx, scope, ((Collection) obj).toArray(), staticType);
                }

                return super.wrap(cx, scope, obj, staticType);
            }
        });
    }

    public static ScriptContextFactory getGlobal() {
        return global;
    }

    @Override
    public boolean hasFeature(Context cx, int featureIndex) {
        if (featureIndex == Context.FEATURE_LOCATION_INFORMATION_IN_ERROR) return true;

        return super.hasFeature(cx, featureIndex);
    }

    private static class ModifiedURLClassLoader extends URLClassLoader {
        List<URL> sources = new ArrayList<>();

        public ModifiedURLClassLoader() {
            super(new URL[0], ScriptContextFactory.class.getClassLoader());
        }

        void addAllUrls(List<URL> urls) {
            for (URL url : urls) {
                if (sources.contains(url)) continue;
                addURL(url);
            }
        }

        @Override
        protected void addURL(URL url) {
            super.addURL(url);
            sources.add(url);
        }
    }
}
