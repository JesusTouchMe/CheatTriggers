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

    // basic utils
    loadClass("java.util.ArrayList");
    loadClass("java.util.HashMap");
    loadClass("org.lwjgl.input.Keyboard");
    loadClass("org.lwjgl.input.Mouse");
    loadClass("org.lwjgl.opengl.Display");

    // libs
    loadClass("cum.jesus.cheattriggers.runtime.libs.ChatLib");

    // triggers
    loadClass("cum.jesus.cheattriggers.scripting.triggers.TriggerRegistry");
    loadClass("cum.jesus.cheattriggers.scripting.triggers.ChatTrigger", "OnChatTrigger");
    loadClass("cum.jesus.cheattriggers.scripting.triggers.ForgeTrigger", "OnForgeTrigger");
    loadClass("cum.jesus.cheattriggers.scripting.triggers.RegularTrigger", "OnRegularTrigger");
    loadClass("cum.jesus.cheattriggers.scripting.triggers.Trigger", "OnTrigger")

    global.Priority = OnTrigger.Priority;

    global.TriggerType = {
        // client
        TICK: "tick",
        SECOND: "second",
        CHAT: "chat",
        GAME_LOAD: "gameLoad",
        GAME_UNLOAD: "gameUnload"

        // render

        // world
    }

    // misc
    loadClass("cum.jesus.cheattriggers.CheatTriggers");
    loadClass("cum.jesus.cheattriggers.internal.command.CommandException");

    // opengl
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