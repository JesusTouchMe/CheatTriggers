package cum.jesus.cheattriggers.scripting;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.utils.Logger;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ScriptManager {
    ScriptEngine engine;
    private final List<Script> scripts = new ArrayList<>();

    public void newEngine() {
        engine = new NashornScriptEngineFactory().getScriptEngine();

        // variables
        engine.put("mc", CheatTriggers.mc);

        // classes
        engine.put("Logger", StaticClass.forClass(Logger.class));

        // methods

        try {
            engine.eval("var twoplustwo = 4;");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

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
                File[] manifestMatches = file.listFiles((pathname -> pathname.getName().equals("manifest.json")));
                if (manifestMatches == null) throw new RuntimeException("No manifest file was found in " + file.getName() + " ('manifest.json')");
                File manifestEntry = manifestMatches[0];

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

        try {
            newEngine();
            engine.eval(content);
        } catch (ScriptException e) {
            throw new RuntimeException("Failed to compile script", e);
        }

        command.setEngine(engine);

        return command;
    }
}
