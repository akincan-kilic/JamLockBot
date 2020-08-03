package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

public class Lock extends Command
{
    private static final Logger log = LoggerFactory.getLogger(Lock.class);

    public Lock()
    {
        this.name = "lock";
        this.guildOnly = true;
        this.help = "Locks the practice room so only the host can talk.";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        log.info("Lock command called");

        TextChannel textChannel = commandEvent.getMessage().getTextChannel();

        if (!textChannel.getName().split("-")[0].equals("practice"))
            commandEvent.getMessage().getTextChannel().sendMessage("You cannot use this command outside of a practice room!").queue();
        else
        {
            VoiceChannel matchingVoiceChannel = Channels.getMatchingVoiceChannel(commandEvent, textChannel);
            User hostUser = commandEvent.getMessage().getAuthor();
            boolean foundHost = false;

            assert matchingVoiceChannel != null;
            for(Member member : matchingVoiceChannel.getMembers())
            {
                if (member.getUser().getName().equals(hostUser.getName()))
                {
                    log.info("Found the host user in the corresponding voice channel, locking the room...");
                    foundHost = true;
                    matchingVoiceChannel.getManager().setName(Constants.LOCK_ICON + matchingVoiceChannel.getName() + " " + hostUser.getName()).queue();
                    textChannel.getManager().setName(Constants.LOCK_ICON + textChannel.getName()).queue();

                    for(Member memberToMute : matchingVoiceChannel.getMembers())
                        if (memberToMute.getUser() != hostUser)
                            memberToMute.mute(true).queue();
                }
            }
            if (!foundHost)
            {
                log.info("Couldn't find the host in the voice channel after lock command was used");
                textChannel.sendMessage("You have to be in the corresponding voice channel in order to use this command!").queue();
            }
        }
    }
}
