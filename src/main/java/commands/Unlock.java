package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Unlock extends Command
{
    public Unlock()
    {
        this.name = "unlock";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent e)
    {
        String hostUserName = Channels.getMatchingVoiceChannel(e, e.getMessage().getTextChannel()).getName().split(" ")[4];
        if (hostUserName.equals(e.getAuthor().getName()))
        {
            TextChannel textChannel = e.getTextChannel();
            VoiceChannel voiceChannel = Channels.getMatchingVoiceChannel(e, textChannel);

            assert voiceChannel != null;
            voiceChannel.getManager().setName(Channels.getNextVoiceChannelName(e)).queue();
            textChannel.getManager().setName(Channels.getNextTextChannelName(e)).queue();

            for (Member memberToUnmute : voiceChannel.getMembers())
                memberToUnmute.mute(false).queue();
        }
        else
            e.getChannel().sendMessage("You need to be the host to unlock this channel!").queue();
    }
}
