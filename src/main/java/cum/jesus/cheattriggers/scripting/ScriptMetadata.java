package cum.jesus.cheattriggers.scripting;

public class ScriptMetadata {
    public String name = null;
    public String description = null;
    public String version = null;
    public String author = null;
    public String index = null;
    public int apiVersion = 1;

    public ScriptMetadata(String name, String description, String version, String author, String index, int apiVersion) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.index = index;
        this.apiVersion = apiVersion;
    }

    public ScriptMetadata(String name, String description, String version, String author, String index) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.index = index;
    }

    public ScriptMetadata() {
    }
}
