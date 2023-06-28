package cum.jesus.cheattriggers.utils;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.ModMain;
import jline.internal.Log;
import org.lwjgl.Sys;

import java.io.PrintStream;

public final class Logger {
    private static PrintStream out = null;

    public static void setOutputStream(PrintStream out) {
        Logger.out = out;
    }

    /**
     * @deprecated do not use this. use the other one pls
     */
    private static void log(String inBracket, Object... objects) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(inBracket).append("] ");

        for (Object obj : objects) {
            sb.append(obj).append(" ");
        }

        assert out != null : "There is no output stream bruh";

        out.print(sb.toString() + "\n");
    }

    private static void log(LogLevel level, Object... objects) {
        log("CT | " + level, objects);
    }

    public static void trace(Object... objects) {
        StackTraceElement stackTrace = new Throwable().getStackTrace()[2];
        String trace = stackTrace.getClassName() + ":" + stackTrace.getLineNumber() + "." + stackTrace.getMethodName();

        log("CT | " + trace, objects);
    }

    public static void debug(Object... objects) {
        if (ModMain.devMode) {
            log(LogLevel.DEBUG, objects);
        }
    }

    public static void log(Object... objects) {
        log(LogLevel.INFO, objects);
    }

    public static void warn(Object... objects) {
        log(LogLevel.WARN, objects);
    }

    public static void error(Object... objects) {
        log(LogLevel.ERROR, objects);
    }

    static {
        out = System.err;
    }

    private enum LogLevel {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}
