package cum.jesus.cheattriggers.runtime.listeners;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.scripting.triggers.TriggerType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class ClientListener {
    public static final ClientListener INSTANCE = new ClientListener();

    private static int ticksPassed = 0; // will last about 1200 days
    private static int secondsPassed = 0; // will last a long ass time
    private static byte ticksToCalculateSeconds = 0;

    private ClientListener() {
        ticksPassed = 0;
        secondsPassed = 0;
        ticksToCalculateSeconds = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (CheatTriggers.mc.theWorld == null) return;

        TriggerType.TICK.triggerAll(new Object[] {ticksPassed});
        ticksPassed++;

        if (ticksToCalculateSeconds == 20) {
            TriggerType.SECOND.triggerAll(new Object[] {secondsPassed});
            secondsPassed++;
            ticksToCalculateSeconds = 0;
        } else {
            ticksToCalculateSeconds++;
        }
    }
}
