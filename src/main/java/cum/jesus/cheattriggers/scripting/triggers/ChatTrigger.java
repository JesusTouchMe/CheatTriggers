package cum.jesus.cheattriggers.scripting.triggers;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatTrigger extends Trigger {
    public ChatTrigger(Object method, TriggerType triggerType) {
        super(method, triggerType);
    }

    @Override
    public void trigger(Object[] args) {
        assert args[0] instanceof String && args[1] instanceof ClientChatReceivedEvent;

        ClientChatReceivedEvent chatEvent = (ClientChatReceivedEvent) args[1];
        if (chatEvent.isCanceled()) return;

        String chatMessage = (String) args[1];

        callMethod(new Object[] {chatMessage, chatEvent});
    }
}
