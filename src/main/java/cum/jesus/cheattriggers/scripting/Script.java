package cum.jesus.cheattriggers.scripting;

import java.io.File;

public class Script {
    private final ScriptMetadata metadata;
    private final File file;

    public Script(ScriptMetadata metadata, File file) {
        this.metadata = metadata;
        this.file = file;
    }

    public ScriptMetadata getMetadata() {
        return metadata;
    }

    public File getFile() {
        return file;
    }
}
