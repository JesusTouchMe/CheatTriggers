package cum.jesus.cheattriggers.runtime.libs;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.utils.ChatUtils;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

public final class ChatLib {
    private ChatLib() {}

    /**
     * Sends a client sided message to the chat
     *
     * @param message The message to send
     */
    public static void sendChat(Object message) {
        ChatUtils.sendMessage(message);
    }

    /**
     * Simulates a chat message that will trigger events. Mainly for testing any chat events
     *
     * @param message The message to simulate
     */
    public static void simulateChat(Object message) {
        CheatTriggers.mc.addScheduledTask(() -> {
            CheatTriggers.mc.getNetHandler().handleChat(new S02PacketChat(new ChatComponentText(message.toString()), (byte) 0));
        });
    }

    /**
     * Will send a message as the player
     *
     * @param message The message to send
     */
    public static void say(Object message) {
        ChatUtils.sendMessageAsPlayer(message);
    }

    /**
     * Will run a command
     *
     * @param command The command to run
     * @param clientSide Whether to send it on the client or the server (default false)
     */
    public static void runCommand(String command, boolean clientSide) {
        String cmd = "/" + command;
        if (clientSide) ClientCommandHandler.instance.executeCommand(CheatTriggers.mc.thePlayer, cmd);
        else say(cmd);
    }

    /**
     * Will run a command
     *
     * @param command The command to run
     */
    public static void runCommand(String command) {
        runCommand(command, false);
    }
}
