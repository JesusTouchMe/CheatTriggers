package cum.jesus.cheattriggers.injection.mixins.net.minecraft.client.gui;

import cum.jesus.cheattriggers.CheatTriggers;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String msg, CallbackInfo ci) {
        if (msg.startsWith("-") && msg.length() > 1) {
            ci.cancel();

            CheatTriggers.getCommandManager().exec(msg);
            CheatTriggers.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
    }
}
