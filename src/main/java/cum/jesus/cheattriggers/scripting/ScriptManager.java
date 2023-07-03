package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.scripting.triggers.TriggerType;
import cum.jesus.cheattriggers.utils.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ScriptManager {
    private List<Script> scripts;

    public void setup() {
        File[] zipFiles = CheatTriggers.getFileManager().scriptDataDir.listFiles(pathname -> pathname.getName().endsWith("zip") || pathname.getName().endsWith("cbs"));
        if (zipFiles == null) zipFiles = new File[0];
        File[] dirs = CheatTriggers.getFileManager().scriptsDir.listFiles(File::isDirectory);
        if (dirs == null) dirs = new File[0];
        List<File> scriptFiles = new ArrayList<>(Arrays.asList(zipFiles));
        scriptFiles.addAll(Arrays.asList(dirs));

        this.scripts = scriptFiles.stream().map(this::parseScript).collect(Collectors.toCollection(ArrayList::new));

        ScriptLoader.setup();
    }

    public void clean() {
        scripts.clear();
        ScriptLoader.clean();
    }

    public void entryPass() {
        ScriptLoader.indexSetup();

        scripts.stream().filter(it -> it.getMetadata().index != null && new File(it.getFile(), it.getMetadata().index).getName().endsWith(".js"))
                .forEach(it -> {
                    ScriptLoader.indexPass(it, it.getFile());
                });
    }

    public Script parseScript(File file) {
        try {
            boolean isZipped = !file.isDirectory();
            String metadataContent = null;

            if (isZipped) {
                ZipFile zipFile = new ZipFile(file);
                ZipEntry manifestEntry = zipFile.getEntry("manifest.json");
                if (manifestEntry == null) throw new RuntimeException(file.getName() + " doesn't contain 'manifest.json'");
                InputStream stream = zipFile.getInputStream(manifestEntry);
                metadataContent = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            } else {
                assert file.isDirectory();
                File metadataFile = new File(file, "manifest.json");
                if (!metadataFile.exists()) throw new RuntimeException(file.getName() + " doesn't contain 'manifest.json'");
                metadataContent = new String(Files.readAllBytes(metadataFile.toPath()));
            }

            ScriptMetadata metadata = new ScriptMetadata();
            try {
                metadata = CheatTriggers.gson.fromJson(metadataContent, ScriptMetadata.class);
            } catch (Exception e) {
                Logger.error(file.getName() + " has an invalid 'manifest.json'");
            }

            if (metadata.index != null) metadata.index = metadata.index.replace('/', File.separatorChar).replace('\\', File.separatorChar);

            return new Script(metadata, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void trigger(TriggerType type, Object[] args) {
        ScriptLoader.execTriggerType(type, args);
    }
}
