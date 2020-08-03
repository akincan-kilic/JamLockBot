package functions;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import utils.constants;

import java.util.List;

public class channels
{
    public static VoiceChannel getMatchingVoiceChannel(CommandEvent e, TextChannel textChannel)
    {
        String textChannelEmoji = textChannel.getName().split("-")[3];

        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getVoiceChannels();

        for (VoiceChannel voiceChannel : voiceChannels)
        {
            if (voiceChannel.getName().split(" ")[3].equals(textChannelEmoji))
                return voiceChannel;
        }
        System.out.println("Couldn't find matching voice channel...");
        return null;
    }

    public static TextChannel getMatchingTextChannel(GuildVoiceLeaveEvent e, VoiceChannel voiceChannel)
    {
        String voiceChannelEmoji = voiceChannel.getName().split(" ")[3];

        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        for (TextChannel textChannel : textChannels)
        {
            if (textChannel.getName().split("-")[3].equals(voiceChannelEmoji))
                return textChannel;
        }
        System.out.println("Couldn't find matching text channel");
        return null;
    }

    public static String getNextTextChannelName(CommandEvent e)
    {
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = textChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String textChannelName = constants.practiceTextChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < textChannels.size(); j++)
            {
                if (!textChannels.get(i).getName().equals(textChannelName))
                {
                    System.out.println("Returning text channel name of: " + textChannelName);
                    return textChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }

    public static String getNextTextChannelName(GuildVoiceJoinEvent e)
    {
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = textChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String textChannelName = constants.practiceTextChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < textChannels.size(); j++)
            {
                if (!textChannels.get(i).getName().equals(textChannelName))
                {
                    System.out.println("Returning text channel name of: " + textChannelName);
                    return textChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }

    public static String getNextTextChannelName(GuildVoiceLeaveEvent e)
    {
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = textChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String textChannelName = constants.practiceTextChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < textChannels.size(); j++)
            {
                if (!textChannels.get(i).getName().equals(textChannelName))
                {
                    System.out.println("Returning text channel name of: " + textChannelName);
                    return textChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }

    public static String getNextVoiceChannelName(CommandEvent e)
    {
        //We are still getting emojis according to existing text channels but variable name is voiceChannels so reading the code is easier.
        List<TextChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = voiceChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String voiceChannelName = constants.practiceVoiceChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < voiceChannels.size(); j++)
            {
                if (!voiceChannels.get(i).getName().equals(voiceChannelName))
                {
                    System.out.println("Returning voice channel name of: " + voiceChannelName);
                    return voiceChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }

    public static String getNextVoiceChannelName(GuildVoiceJoinEvent e)
    {
        //We are still getting emojis according to existing text channels but variable name is voiceChannels so reading the code is easier.
        List<TextChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = voiceChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String voiceChannelName = constants.practiceVoiceChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < voiceChannels.size(); j++)
            {
                if (!voiceChannels.get(i).getName().equals(voiceChannelName))
                {
                    System.out.println("Returning voice channel name of: " + voiceChannelName);
                    return voiceChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }

    public static String getNextVoiceChannelName(GuildVoiceLeaveEvent e)
    {
        //We are still getting emojis according to existing text channels but variable name is voiceChannels so reading the code is easier.
        List<TextChannel> voiceChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getTextChannels();

        int totalChannelCount = voiceChannels.size();
        totalChannelCount += 1;

        for (int i = 0; i < constants.VC_IDENTIFIERS.length; i++)
        {
            String voiceChannelName = constants.practiceVoiceChannelName + constants.VC_IDENTIFIERS[totalChannelCount];
            for (int j = 0; j < voiceChannels.size(); j++)
            {
                if (!voiceChannels.get(i).getName().equals(voiceChannelName))
                {
                    System.out.println("Returning voice channel name of: " + voiceChannelName);
                    return voiceChannelName;
                }
            }
        }
        System.out.println("RAN OUT OF ROOM EMOTES");
        return "XXX";
    }


    /*public static String getNextTextChannelName(int totalChannelCount)
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
    }*/

}
