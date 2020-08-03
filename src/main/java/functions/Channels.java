package functions;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.List;

public class Channels
{
    private static final Logger log = LoggerFactory.getLogger(Channels.class);

    public static VoiceChannel getMatchingVoiceChannel(CommandEvent e, TextChannel textChannel)
    {
        String textChannelEmoji = textChannel.getName().split("-")[3];
        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getVoiceChannels();

        for (VoiceChannel voiceChannel : voiceChannels)
            if (voiceChannel.getName().split(" ")[3].equals(textChannelEmoji))
                return voiceChannel;

        log.error("Couldn't find matching voice channel!");
        return null;
    }

    public static TextChannel getMatchingTextChannel(GuildVoiceLeaveEvent e, VoiceChannel voiceChannel)
    {
        String voiceChannelEmoji = voiceChannel.getName().split(" ")[3];
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getTextChannels();

        for (TextChannel textChannel : textChannels)
            if (textChannel.getName().split("-")[3].equals(voiceChannelEmoji))
                return textChannel;

        log.error("Couldn't find matching text channel!");
        return null;
    }

    public static String getNextTextChannelName(JDA jda)
    {
        List<TextChannel> textChannels = jda.getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getTextChannels();

        for (String emote: Constants.VC_IDENTIFIERS)
        {
            boolean nameExists = false;
            String newTextChannelName = Constants.PRACTICE_TEXT_CHANNEL_NAME + emote;
            for (TextChannel textChannel: textChannels)
            {
                if (textChannel.getName().equals(newTextChannelName))
                {
                    nameExists = true;
                    break;
                }
            }
            if (!nameExists)
            {
                log.info("Creating a Text Channel:" + newTextChannelName);
                return newTextChannelName;
            }
        }
        log.error("Ran out of room emotes!");
        return "XXX";
    }

    public static String getNextVoiceChannelName(JDA jda)
    {
        //We are still getting the name according to existing text channels so we can avoid name mismatches.
        List<TextChannel> textChannels = jda.getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getTextChannels();

        for (String emote: Constants.VC_IDENTIFIERS)
        {
            boolean nameExists = false;
            String newTextChannelName = Constants.PRACTICE_TEXT_CHANNEL_NAME + emote;
            String newVoiceChannelName = Constants.PRACTICE_VOICE_CHANNEL_NAME + emote;

            for (TextChannel textChannel: textChannels)
            {
                if (textChannel.getName().equals(newTextChannelName))
                {
                    nameExists = true;
                    break;
                }
            }
            if (!nameExists)
            {
                log.info("Creating a Voice Channel: " + newVoiceChannelName);
                return newVoiceChannelName;
            }
        }
        log.error("Ran out of room emotes!");
            return "XXX";
    }

    public static void createPracticeRoom(GuildVoiceJoinEvent e) throws Exception
    {
        log.info("Attempting to create new voice and text channels...");

        String categoryName = Constants.PRACTICE_CATEGORY_NAME;
        List<Category> result = e.getGuild().getCategoriesByName(categoryName, false);

        //If we find the category then create a channel on that category, else create the category and the channel.

        if(result.isEmpty())
            e.getGuild().createCategory(categoryName).queue(category -> {
                try { createChannels(category, e); } catch (Exception ex) { ex.printStackTrace(); } });

        else
            createChannels(result.get(0), e);
    }

    public static void removeEmptyPracticeChannel(GuildVoiceLeaveEvent e) throws InterruptedException
    {
        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME,false).get(0).getVoiceChannels();
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME,false).get(0).getTextChannels();
        int emptyVoiceChannelCount = 0;

        for (VoiceChannel voiceChannel : voiceChannels)
            if (voiceChannel.getMembers().size() == 0)
                emptyVoiceChannelCount++;

        for (int i = voiceChannels.size()-1; i >= 0; i--) //For loop starts from the last index so the last rooms start getting deleted.
        {
            if (emptyVoiceChannelCount <= Constants.MAX_EMPTY_UNLOCKED_ROOMS)
            {
                log.info("Successfully deleted all the extra practice rooms.");
                break;
            }

            log.info("There are {} extra empty voice channels, attempting to delete them...", emptyVoiceChannelCount - Constants.MAX_EMPTY_UNLOCKED_ROOMS);

            if (voiceChannels.get(i).getMembers().size() == 0)
            {
                log.info("Deleting the extra text channel: {}", textChannels.get(i).getName());
                textChannels.get(i).delete().queue();
                Thread.sleep(100);

                log.info("Deleting the extra voice channel: {}", voiceChannels.get(i).getName());
                voiceChannels.get(i).delete().queue();
                Thread.sleep(100);

                emptyVoiceChannelCount--;
            }
        }
    }

    public static void forceUnlock(GuildVoiceLeaveEvent e)
    {
        TextChannel textChannel = Channels.getMatchingTextChannel(e, e.getChannelLeft());
        VoiceChannel voiceChannel = e.getChannelLeft();

        assert textChannel != null;
        voiceChannel.getManager().setName(Channels.getNextVoiceChannelName(e)).queue();
        textChannel.getManager().setName(Channels.getNextTextChannelName(e)).queue();

        for (int i = 0; i < voiceChannel.getMembers().size(); i++)
            voiceChannel.getMembers().get(i).mute(false).queue();
    }

    //PRIVATE METHODS
    private static void createChannels(Category category, GuildVoiceJoinEvent e) throws Exception
    {
        category.createTextChannel(Channels.getNextTextChannelName(e)).queue();
        Thread.sleep(100);
        category.createVoiceChannel(Channels.getNextVoiceChannelName(e)).queue();
        Thread.sleep(100);
    }
    //PRIVATE METHODS END.

    //OVERLOADERS
    public static String getNextTextChannelName(CommandEvent e)
    {
        return getNextTextChannelName(e.getJDA());
    }

    public static String getNextTextChannelName(GuildVoiceJoinEvent e)
    {
        return getNextTextChannelName(e.getJDA());
    }

    public static String getNextTextChannelName(GuildVoiceLeaveEvent e)
    {
        return getNextTextChannelName(e.getJDA());
    }

    public static String getNextVoiceChannelName(CommandEvent e)
    {
       return getNextVoiceChannelName(e.getJDA());
    }

    public static String getNextVoiceChannelName(GuildVoiceJoinEvent e)
    {
        return getNextVoiceChannelName(e.getJDA());
    }

    public static String getNextVoiceChannelName(GuildVoiceLeaveEvent e)
    {
        return getNextVoiceChannelName(e.getJDA());
    }
    //OVERLOADERS END.
}
