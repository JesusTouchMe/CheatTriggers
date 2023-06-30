package cum.jesus.cheattriggers.scripting;

import cum.jesus.cheattriggers.CheatTriggers;
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

    /*
    public Script load(File file, boolean isZipped) {
        try {
            ZipFile zipFile = null;
            if (isZipped) zipFile = new ZipFile(file);
            else assert file.isDirectory();

            JsonElement manifestElement;

            if (isZipped) {
                ZipEntry manifestEntry = zipFile.getEntry("manifest.json");
                if (manifestEntry == null) throw new RuntimeException("No manifest file was found in " + file.getName() + " ('manifest.json')");

                manifestElement = new JsonParser().parse(new InputStreamReader(zipFile.getInputStream(manifestEntry)));
                if (!manifestElement.isJsonObject()) throw new RuntimeException("Manifest is not a valid Json object");
            } else {
                File manifestEntry = new File(file, "manifest.json");
                if (!manifestEntry.exists()) throw new RuntimeException("No manifest file was found in " + file.getName() + " ('manifest.json')");

                Reader reader = Files.newBufferedReader(manifestEntry.toPath());
                manifestElement = new JsonParser().parse(reader);
                if (!manifestElement.isJsonObject()) throw new RuntimeException("Manifest is not a valid Json object");
                reader.close();
            }

            JsonObject manifest = manifestElement.getAsJsonObject();

            Metadata metadata = new Metadata();

            //<editor-fold desc="Metadata">
            {
                if (!manifest.has("name")) throw new RuntimeException("Manifest does not contain 'name'");
                JsonElement element = manifest.get("name");

                if (element.isJsonPrimitive()) metadata.name = element.getAsString();
                else throw new RuntimeException("'name' is not valid");

                if (!manifest.has("description")) metadata.description = "No description found";
                else {
                    element = manifest.get("description");

                    if (element.isJsonPrimitive()) metadata.description = element.getAsString();
                    else throw new RuntimeException("'description' is not valid");
                }

                if (!manifest.has("version")) throw new RuntimeException("Manifest does not contain 'version'");
                element = manifest.get("version");

                if (element.isJsonPrimitive()) metadata.version = element.getAsString();
                else throw new RuntimeException("'version' is not valid");

                if (!manifest.has("author")) throw new RuntimeException("Manifest does not contain 'author'");
                element = manifest.get("author");

                if (element.isJsonPrimitive()) metadata.author = element.getAsString();
                else throw new RuntimeException("'authors' is not valid");

                if (!manifest.has("index")) throw new RuntimeException("Manifest does not contain 'index'");
                element = manifest.get("index");

                if (element.isJsonPrimitive()) metadata.index = element.getAsString();
                else throw new RuntimeException("'index' is not valid");
            }
            //</editor-fold>

            newEngine();

            Script script = new Script(metadata);

            if (manifest.has("modules")) {

            }

            if (manifest.has("commands")) {
                JsonElement element = manifest.get("commands");

                if (!element.isJsonArray()) throw new RuntimeException("'commands' has to be an array");

                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    ScriptCommand command = loadCommand(jsonElement, file, metadata.name, isZipped);
                    metadata.commands.add(command);
                }
            }

            script.register();

            Logger.info("Loaded script: " + metadata.name);

            scripts.add(script);
            return script;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScriptCommand loadCommand(JsonElement jsonElement, File file, String scriptName, boolean isZipped) throws IOException {
        ZipFile zipFile = null;
        if (isZipped) zipFile = new ZipFile(file);
        else assert file.isDirectory();

        if (!jsonElement.isJsonObject()) throw new RuntimeException("A command has to be a Json object");

        JsonObject obj = jsonElement.getAsJsonObject();

        String name;
        String desc;
        String[] alias;
        String indexFile;

        //<editor-fold desc="Metadata">
        {
            if (!obj.has("name")) throw new RuntimeException("Module does not contain 'name'");
            JsonElement element = obj.get("name");

            if (element.isJsonPrimitive()) name = element.getAsString();
            else throw new RuntimeException("'name' is invalid");
        }
        {
            if (!obj.has("description")) throw new RuntimeException("No 'description' was specified in '" + name + "'");
            JsonElement element = obj.get("description");

            if (element.isJsonPrimitive()) desc = element.getAsString();
            else throw new RuntimeException("'description' is invalid");
        }
        {
            if (!obj.has("alias")) alias = new String[0];
            else {
                JsonElement element = obj.get("alias");

                if (element.isJsonArray()) alias = new Gson().fromJson(element.getAsJsonArray(), String[].class);
                else throw new RuntimeException("'alias' is not valid");
            }
        }
        {
            if (!obj.has("index")) throw new RuntimeException("No 'index' was specified in '" + name + "'");
            JsonElement element = obj.get("index");

            if (element.isJsonPrimitive()) indexFile = element.getAsString();
            else throw new RuntimeException("'index' is invalid");
        }
        //</editor-fold>

        ScriptCommand command = new ScriptCommand(name, alias);
        command.setDescription(desc);

        String content;

        if (isZipped) {
            ZipEntry entry = zipFile.getEntry(indexFile);
            if (entry == null) {
                throw new RuntimeException("Script file doesn't contain '" + indexFile + "'");
            }

            try {
                content = new String(ByteStreams.toByteArray(zipFile.getInputStream(entry)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            File[] matches = file.listFiles((pathname -> pathname.getName().equals(indexFile)));
            if (matches == null) throw new RuntimeException("Script file doesn't contain '" + indexFile + "'");
            File entry = matches[0];

            content = new String(Files.readAllBytes(entry.toPath()));
        }

        newEngine();
        eval(content, indexFile);

        command.setContext(context);
        command.setScope(scope);

        return command;
    }
    */
}
