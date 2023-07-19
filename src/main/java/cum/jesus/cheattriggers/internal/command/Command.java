package cum.jesus.cheattriggers.internal.command;

import cum.jesus.cheattriggers.CheatTriggers;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected final Minecraft mc = CheatTriggers.mc;

    private final String name;
    private String description;
    private String help;
    private final String[] aliases;

    protected Command(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;

        description = null;
        help = null;
    }

    /**
     * @param alias The alias used to run the command (useful for error messages)
     * @param args The arguments passed to the command
     */
    public abstract void run(String alias, String[] args) throws CommandException;

    public boolean matchName(String name) {
        if (this.name.equalsIgnoreCase(name)) return true;
        for (String alias : aliases) {
            if(alias.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return help;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String[] getAliases() {
        return aliases;
    }

    public List<String> getNameWithAliases() {
        List<String> listThatWillBeReturnedByThisFunctionAfterAddingTheCommandNameAndAliasesToIt = new ArrayList<>();
        listThatWillBeReturnedByThisFunctionAfterAddingTheCommandNameAndAliasesToIt.add(name);
        listThatWillBeReturnedByThisFunctionAfterAddingTheCommandNameAndAliasesToIt.addAll(Arrays.asList(aliases));
        return listThatWillBeReturnedByThisFunctionAfterAddingTheCommandNameAndAliasesToIt;
    }
}
