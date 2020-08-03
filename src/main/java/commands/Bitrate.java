package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bitrate extends Command
{
    private static final Logger log = LoggerFactory.getLogger(Bitrate.class);

    public Bitrate()
    {
        this.name = "bitrate";
        this.help = "Will be added in future updates!";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        log.info("Bitrate command called");
        //TODO add usefull stuff here lol
        commandEvent.getChannel().sendMessage("This feature will be added in a future update!").queue();
    }
}
