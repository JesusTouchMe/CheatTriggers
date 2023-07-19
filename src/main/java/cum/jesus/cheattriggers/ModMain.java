package cum.jesus.cheattriggers;

import cum.jesus.cheattriggers.runtime.listeners.ClientListener;
import cum.jesus.cheattriggers.runtime.listeners.WorldListener;
import cum.jesus.cheattriggers.scripting.triggers.ForgeTrigger;
import cum.jesus.cheattriggers.utils.ClientUser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Mod class is a separate class so {@link CheatTriggers} can remain static
 */
@Mod(modid=CheatTriggers.MODID, name=CheatTriggers.NAME, version = CheatTriggers.VERSION)
public final class ModMain {
    static GuiScreen display = null;

    public static boolean devMode = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") || ClientUser.username.equals("JesusTouchMe");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ClientListener.INSTANCE);
        MinecraftForge.EVENT_BUS.register(WorldListener.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ForgeTrigger.eventListener);

        CheatTriggers.preLoad();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CheatTriggers.load();
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (CheatTriggers.mc.thePlayer == null || CheatTriggers.mc.theWorld == null) return;

        if (display != null) {
            try {
                CheatTriggers.mc.displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }
    }
}
