package functions;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import utils.constants;

import java.util.List;

public class channels
{
    public static VoiceChannel getMatchingVoiceChannel(CommandEvent e, TextChannel textChannel)
    {
        String textChannelEmoji = textChannel.getName().split("-")[3];

        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getVoiceChannels();

        for (int i = 0; i < voiceChannels.size(); i++)
        {
            if (voiceChannels.get(i).getName().split(" ")[3].equals(textChannelEmoji))
                return voiceChannels.get(i);
        }
        System.out.println("Couldn't find matching voice channel...");
        return null;
    }
    
    public static TextChannel getMatchingTextChannel(GuildVoiceLeaveEvent e, VoiceChannel voiceChannel)
    {
        String voiceChannelEmoji = voiceChannel.getName().split(" ")[3];
        
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        for (int i = 0; i < textChannels.size(); i++)
        {
            if (textChannels.get(i).getName().split("-")[3].equals(voiceChannelEmoji))
                return textChannels.get(i);
        }
        System.out.println("Couldn't find matching text channel");
        return null;
    }

    public static String getNextTextChannelName(int totalChannelCount)
    {
        String textChannelName = constants.practiceTextChannelName + constants.VC_IDENTIFIERS[totalChannelCount];

        System.out.println("Creating a text channel with the name of " + textChannelName);
        return textChannelName;
    }

    public static String getNextVoiceChannelName(int totalChannelCount)
    {
        String voiceChannelName = constants.practiceVoiceChannelName + constants.VC_IDENTIFIERS[totalChannelCount];

        System.out.println("Creating a voice channel with the name of " + voiceChannelName);
        return voiceChannelName;
    }

}
