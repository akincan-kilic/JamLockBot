package functions;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.List;
import java.util.Random;

public class Channels
{
    private static final Logger log = LoggerFactory.getLogger(Channels.class);

    public static boolean isPracticeRoom(GuildChannel guildChannel)
    {
        return guildChannel.getName().startsWith("Practice Room") || guildChannel.getName().startsWith("practice-room") || guildChannel.getName().startsWith("🔒");
    }

    public static VoiceChannel getMatchingVoiceChannel(CommandEvent e, TextChannel textChannel)
    {
        String textChannelEmoji = textChannel.getName().split("-")[3];
        List<VoiceChannel> voiceChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getVoiceChannels();

        for (VoiceChannel voiceChannel : voiceChannels)
        {
            if (voiceChannel.getName().split(" ")[3].equals(textChannelEmoji))
                return voiceChannel;
        }

        log.error("Couldn't find matching voice channel!");
        return null;
    }

    public static TextChannel getMatchingTextChannel(GuildVoiceLeaveEvent e, VoiceChannel voiceChannel)
    {
        String voiceChannelEmoji = voiceChannel.getName().split(" ")[3];
        List<TextChannel> textChannels = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getTextChannels();

        for (TextChannel textChannel : textChannels)
        {
            if (textChannel.getName().split("-")[3].equals(voiceChannelEmoji))
                return textChannel;
        }
        log.error("Couldn't find matching text channel!");
        return null;
    }

    public static String getNextEmote(Guild guild)
    {

        List<TextChannel> textChannels = guild.getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0).getTextChannels();

        for (int i = 0; i < 100; i++)
        {
            Random random = new Random();
            int randomIndex = random.nextInt(Constants.VC_IDENTIFIERS.length);
            String emote = Constants.VC_IDENTIFIERS[randomIndex];

            boolean emoteFound = false;

            for (TextChannel textChannel : textChannels)
            {
                String existingEmote = textChannel.getName().split("-")[3];

                if (emote.equals(existingEmote))
                {
                    emoteFound = true;
                    break;
                }
            }
            if (!emoteFound)
            {
                return emote;
            }
        }
        log.error("Getting new room emote failed after 100 attempts.");
        return "XXX";
    }
    public static String getNextTextChannelName(String emote)
    {
        String newTextChannelName = Constants.PRACTICE_TEXT_CHANNEL_NAME.concat(emote);
        log.info("Returning a Text Channel name:" + newTextChannelName);
        return newTextChannelName;
    }

    public static String getNextVoiceChannelName(String emote)
    {
        String newVoiceChannelName = Constants.PRACTICE_VOICE_CHANNEL_NAME.concat(emote);
        log.info("Returning a Text Channel name:" + newVoiceChannelName);
        return newVoiceChannelName;
    }

    public static void createLockedPracticeRoomsAndMove(CommandEvent e, String lockedVoiceName, String lockedTextName, VoiceChannel previousVoiceChannel)
    {
        int maxBitRate = e.getGuild().getMaxBitrate();
        Category category = e.getGuild().getCategoriesByName(Constants.PRACTICE_CATEGORY_NAME, false).get(0);
        category.createTextChannel(lockedTextName).complete();
        category.createVoiceChannel(lockedVoiceName).setBitrate(maxBitRate).complete();

        for (VoiceChannel voiceChannel : category.getVoiceChannels())
        {
            String userID = voiceChannel.getName().split(" ")[2];
            if (e.getMember().getUser().getId().equals(userID))
            {
                for (Member memberToMove : previousVoiceChannel.getMembers())
                {
                    e.getGuild().moveVoiceMember(memberToMove, voiceChannel).queue();
                }
                break;
            }
        }
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
        String emote = getNextEmote(e);
        voiceChannel.getManager().setName(Channels.getNextVoiceChannelName(emote)).queue();
        textChannel.getManager().setName(Channels.getNextTextChannelName(emote)).queue();

        for (int i = 0; i < voiceChannel.getMembers().size(); i++)
            voiceChannel.getMembers().get(i).mute(false).queue();
    }

    //PRIVATE METHODS
    private static void createChannels(Category category, GuildVoiceJoinEvent e) throws Exception
    {
        String emote = getNextEmote(e);
        int maxBitrate = e.getGuild().getMaxBitrate();
        category.createTextChannel(Channels.getNextTextChannelName(emote)).queue();
        Thread.sleep(100);
        category.createVoiceChannel(Channels.getNextVoiceChannelName(emote)).setBitrate(maxBitrate).queue();
        Thread.sleep(100);
        log.info("New channel has been created and it's bitrate automatically set to the max available bitrate: " + maxBitrate/1000);
    }
    //PRIVATE METHODS END.

    //OVERLOADERS
    public static void createPracticeRoom(CommandEvent e) throws Exception
    {
        GuildVoiceJoinEvent fakeJoinEvent = new GuildVoiceJoinEvent(e.getJDA(), e.getResponseNumber(), e.getMember());
        createPracticeRoom(fakeJoinEvent);
    }

    public static String getNextEmote(CommandEvent e)
    {
        return getNextEmote(e.getGuild());
    }

    public static String getNextEmote(GuildVoiceJoinEvent e)
    {
        return getNextEmote(e.getGuild());
    }

    public static String getNextEmote(GuildVoiceLeaveEvent e)
    {
        return getNextEmote(e.getGuild());
    }
    //OVERLOADERS END.
}
