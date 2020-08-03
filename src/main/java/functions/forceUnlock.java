package functions;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

public class forceUnlock
{
    public static void forceUnlock(GuildVoiceLeaveEvent e)
    {
        TextChannel textChannel = channels.getMatchingTextChannel(e, e.getChannelLeft());
        VoiceChannel voiceChannel = e.getChannelLeft();
        voiceChannel.getManager().setName(channels.getNextVoiceChannelName(e)).queue();
        assert textChannel != null;
        textChannel.getManager().setName(channels.getNextTextChannelName(e)).queue();

        for (int i = 0; i < voiceChannel.getMembers().size(); i++)
            voiceChannel.getMembers().get(i).mute(false).queue();
    }
}
