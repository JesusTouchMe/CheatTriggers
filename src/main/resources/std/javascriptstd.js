// some things stolen from chattriggers

(function(global) {
    const Class = Java.type("java.lang.Class");

    const getClassName = path => path.substring(path.lastIndexOf('.') + 1);

    function getClass(path) {
        const clazz = Java.type(path);
        return clazz;
    }

    function loadClass(path, className) {
        className = className || getClassName(path);
        global[className] = getClass(path);
    }

    function loadInstance(path, className) {
        className = className || getClassName(path);
        global[className] = getClass(path).INSTANCE;
    }

    loadClass("java.util.ArrayList");
    loadClass("java.util.HashMap");
    loadClass("org.lwjgl.input.Keyboard");
    loadClass("org.lwjgl.input.Mouse");
    loadClass("org.lwjgl.opengl.Display");

    loadClass("cum.jesus.cheattriggers.CheatTriggers");

    loadClass("net.minecraft.client.renderer.GlStateManager");
    loadClass("org.lwjgl.opengl.GL11");
    loadClass("org.lwjgl.opengl.GL12");
    loadClass("org.lwjgl.opengl.GL13");
    loadClass("org.lwjgl.opengl.GL14");
    loadClass("org.lwjgl.opengl.GL15");
    loadClass("org.lwjgl.opengl.GL20");
    loadClass("org.lwjgl.opengl.GL21");
    loadClass("org.lwjgl.opengl.GL30");
    loadClass("org.lwjgl.opengl.GL31");
    loadClass("org.lwjgl.opengl.GL32");
    loadClass("org.lwjgl.opengl.GL33");
    loadClass("org.lwjgl.opengl.GL40");
    loadClass("org.lwjgl.opengl.GL41");
    loadClass("org.lwjgl.opengl.GL42");
    loadClass("org.lwjgl.opengl.GL43");
    loadClass("org.lwjgl.opengl.GL44");
    loadClass("org.lwjgl.opengl.GL45");
})(this);