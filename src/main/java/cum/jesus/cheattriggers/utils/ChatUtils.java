package cum.jesus.cheattriggers.utils;

import cum.jesus.cheattriggers.CheatTriggers;
import net.minecraft.util.ChatComponentText;

public final class ChatUtils {
    public static void sendMessage(Object message) {
        CheatTriggers.mc.thePlayer.addChatMessage(new ChatComponentText(message.toString()));
    }

    public static void sendPrefixMessage(Object message) {
        sendMessage("§8[§4CheatTriggers§8]§r " + message.toString());
   }
}
