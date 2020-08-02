package functions;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import utils.constants;

public class forceUnlock
{
    public static void forceUnlock(GuildVoiceLeaveEvent e)
    {
        int totalChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getVoiceChannels().size();
        totalChannels += 1;


        TextChannel textChannel = channels.getMatchingTextChannel(e, e.getChannelLeft());
        VoiceChannel voiceChannel = e.getChannelLeft();
        voiceChannel.getManager().setName(channels.getNextVoiceChannelName(totalChannels)).queue();
        assert textChannel != null;
        textChannel.getManager().setName(channels.getNextTextChannelName(totalChannels)).queue();

        for (int i = 0; i < voiceChannel.getMembers().size(); i++)
        {
            voiceChannel.getMembers().get(i).mute(false).queue();
        }
    }
}
