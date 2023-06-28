package cum.jesus.cheattriggers.internal;

import cum.jesus.cheattriggers.CheatTriggers;

import java.io.File;
import java.io.IOException;

public class FileManager {
    // directories
    public final File rootDir;
    public final File tmpDir;

    // files
    public final File configFile;

    public FileManager() {
        // dirs
        rootDir = new File(CheatTriggers.mc.mcDataDir, CheatTriggers.NAME.replace(" ", ""));
        tmpDir = new File(rootDir, "tmp");

        // files
        configFile = new File(rootDir, "config.json");

        if (!rootDir.exists()) rootDir.mkdirs();
        if (!tmpDir.exists()) tmpDir.mkdirs();

        File[] tmpList = tmpDir.listFiles();
        if (tmpList != null) {
            for (File file : tmpList) {
                file.delete();
            }
        }
    }

    public boolean createTempFile(String name) throws IOException {
        File tmpFile = new File(tmpDir, name);
        tmpFile.deleteOnExit();
        return tmpFile.createNewFile();
    }
}