package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Sammich extends Command
{
    public Sammich()
    {
        this.help = "Makes you a sammich";
        this.name = "sammich";
    }

    @Override
    protected void execute(CommandEvent e)
    {
        e.getMember().getAsMention();
        e.getChannel().sendMessage("Here is your sammich, " + e.getMember().getAsMention()).queue();
        e.getChannel().sendMessage("🥪").queue();
    }
}
