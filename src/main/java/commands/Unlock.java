package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unlock extends Command
{
    private static final Logger log = LoggerFactory.getLogger(Unlock.class);

    public Unlock()
    {
        this.name = "unlock";
        this.guildOnly = true;
        this.help = "Unlocks the already locked practice room so that other users can talk.";
    }

    @Override
    protected void execute(CommandEvent e)
    {
        log.info("Unlock command called");

        String hostUserID = e.getChannel().getName().split("-")[2];

        if (hostUserID.equals(e.getAuthor().getId()))
        {
            TextChannel textChannel = e.getTextChannel();
            VoiceChannel voiceChannel = Channels.getMatchingVoiceChannel(e, textChannel);

            assert voiceChannel != null;
            String emote = Channels.getNextEmote(e);
            voiceChannel.getManager().setName(Channels.getNextVoiceChannelName(emote)).queue();
            textChannel.getManager().setName(Channels.getNextTextChannelName(emote)).queue();

            for (Member memberToUnmute : voiceChannel.getMembers())
                memberToUnmute.mute(false).queue();
        }
        else
        {
            log.info("Unlock command permission denied!");
            e.getChannel().sendMessage("You need to be the host to unlock this channel!").queue();
        }
    }
}
