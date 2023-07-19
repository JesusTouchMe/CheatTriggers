package cum.jesus.cheattriggers.internal.config;

import cum.jesus.cheattriggers.CheatTriggers;
import cum.jesus.cheattriggers.scripting.Script;
import cum.jesus.cheattriggers.utils.font.GlyphPageFontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static cum.jesus.cheattriggers.utils.GLUtils.*;

public class Gui extends GuiScreen {
    private static Color background = new Color(33, 33, 33, 250);
    private static Color darkBackground = new Color(20, 20, 20, 250);
    private GlyphPageFontRenderer font;
    private GlyphPageFontRenderer bigFont;
    private Map<String, Script> scriptMap;
    private String currentPane = "cheattriggers"; // TODO: 7/19/2023 prevent scripts from being named cheattriggers

    public Gui() {
        font = GlyphPageFontRenderer.create("Consolas", 15, false, false, false);
        bigFont = GlyphPageFontRenderer.create("Consolas", 30, true, false, false);
        scriptMap = new HashMap<>();

        for (Script script : CheatTriggers.getScriptManager().getScripts()) {
            if (!scriptMap.containsKey(script.getMetadata().name)) scriptMap.put(script.getMetadata().name, script);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glLineWidth(1.0f);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);

        ScaledResolution res = new ScaledResolution(mc);
        int mainWidth = 650;
        int mainHeight = 400;
        int mainX = res.getScaledWidth() / 2 - mainWidth / 2;
        int mainY = res.getScaledHeight() / 2 - mainHeight / 2;

        mainWidth += mainX;
        mainHeight += mainY;

        drawStaticScreen(mainX, mainY, mainWidth, mainHeight);

        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    public void onGuiClosed() {
        // TODO: 7/19/2023 saving
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawStaticScreen(int x, int y, int w, int h) {
        glDrawRect(GL_QUADS, x, y, w, h, background.getRGB());
        glDrawRect(GL_QUADS, x + bigFont.getStringWidth(CheatTriggers.NAME) + 5, y, x + bigFont.getStringWidth(CheatTriggers.NAME) + 6.5, h, darkBackground.getRGB());
        glDrawRect(GL_QUADS, x, y + bigFont.getFontHeight() + 4, x + bigFont.getStringWidth(CheatTriggers.NAME) + 5.5, y + bigFont.getFontHeight() + 5.5, darkBackground.getRGB());

        bigFont.drawString(CheatTriggers.NAME, x + 1, y + 2, Color.WHITE.getRGB(), false);
    }
}
