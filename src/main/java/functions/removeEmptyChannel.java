package functions;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import utils.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class removeEmptyChannel
{
    public static void removeEmptyPracticeChannel(GuildVoiceLeaveEvent e) throws InterruptedException
    {
        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName,false).get(0).getVoiceChannels();
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName,false).get(0).getTextChannels();
        int emptyVoiceChannelCount = 0;

        for (int i = 0; i < voiceChannels.size(); i++)
            if (voiceChannels.get(i).getMembers().size() == 0)
                emptyVoiceChannelCount++;

        for (int i = voiceChannels.size()-1; i >= 0; i--)
        {
            if (emptyVoiceChannelCount <= constants.MAX_EMPTY_UNLOCKED_ROOMS)
            {
                System.out.println("Successfully deleted the extra empty rooms, breaking the loop.");
                break;
            }

            System.out.println("There are " + emptyVoiceChannelCount + " amount of empty voice channels, attempting to delete them " +
                    "until there are only " + constants.MAX_EMPTY_UNLOCKED_ROOMS + " empty voice channels left.");

            if (voiceChannels.get(i).getMembers().size() == 0)
            {
                System.out.println("Deleting the text channel...");
                textChannels.get(i).delete().queue();
                Thread.sleep(100);

                System.out.println("Deleting the voice channel...");
                voiceChannels.get(i).delete().queue();
                Thread.sleep(100);

                emptyVoiceChannelCount--;
            }
        }
    }

    /*private static TextChannel getMatchingTextChannel(GuildVoiceLeaveEvent e, VoiceChannel voiceChannel)
    {
        String[] voiceChannelName = voiceChannel.getName().split(" ");
        String channelEmoji = voiceChannelName[3];


        //List<TextChannel> textChannels = e.getGuild().getCategoryById(voiceChannel.getParent().getId()).getTextChannels();
        List<TextChannel> textChannels = e.getGuild().getTextChannels();

        for (int i = 0; i < textChannels.size(); i++)
        {
            String[] textChannelName = textChannels.get(i).getName().split("-");
            System.out.println(Arrays.toString(textChannelName));
            try
            {
                System.out.println("LEFT VOICE EMOJI: " + channelEmoji);
                System.out.println(textChannelName[3]);
                if (textChannelName[3].equals(channelEmoji))
                    return textChannels.get(i);
            }
            catch (Exception ex)
            {
                System.out.println(ex);
            }
        }
        System.out.println("fuck");
        return null;
    }*/
}
