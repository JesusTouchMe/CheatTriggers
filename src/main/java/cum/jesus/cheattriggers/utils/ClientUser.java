package cum.jesus.cheattriggers.utils;

import net.minecraft.util.Session;

import static cum.jesus.cheattriggers.CheatTriggers.mc;

public final class ClientUser {
    @NoteForDecompilers("a session variable does not mean i am trying to rat you. what you SHOULD be looking for is func_111286_b and func_148254_d (remember to search in strings too for reflection)") public static Session session = mc.getSession();
    public static String username = session.getUsername();
    public static String uuid = session.getProfile().getId().toString();
    public static String compactUUID = uuid.replace("-", "");
}
