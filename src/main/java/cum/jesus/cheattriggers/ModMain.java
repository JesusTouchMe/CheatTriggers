package cum.jesus.cheattriggers;

import cum.jesus.cheattriggers.utils.ClientUser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=CheatTriggers.MODID, name=CheatTriggers.NAME, version = CheatTriggers.VERSION)
public final class ModMain {
    public static boolean devMode = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") || ClientUser.username.equals("JesusTouchMe");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CheatTriggers.preLoad();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CheatTriggers.load();
    }
}
