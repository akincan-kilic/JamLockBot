package functions;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import utils.constants;

import java.util.List;

public class createChannel
{
    public static void createPracticeRoom(GuildVoiceJoinEvent e) throws Exception
    {
        System.out.println("Creating a new voice channel because an user joined an empty voice channel...");

        String categoryName = constants.practiceCategoryName;

        List<Category> result = e.getGuild().getCategoriesByName(categoryName, false);

        //If we find the category then create a channel on that category, else create the category and the channel.
        if(result.isEmpty())
            e.getGuild().createCategory(categoryName).queue(category -> {
                try { createChannels(category, e); } catch (Exception ex) { ex.printStackTrace(); } });
        else
            createChannels(result.get(0), e);
    }

    private static void createChannels(Category category, GuildVoiceJoinEvent e) throws Exception
    {
        category.createTextChannel(getNextTextChannelName(e)).queue();
        Thread.sleep(100);
        category.createVoiceChannel(getNextVoiceChannelName(e)).queue();
        Thread.sleep(100);
    }

    private static String getNextTextChannelName(GuildVoiceJoinEvent e)
    {
        int textChannelCount = e.getGuild().getTextChannels().size();
        String textChannelName = constants.practiceTextChannelName + constants.VC_IDENTIFIERS[textChannelCount];

        System.out.println("Creating a text channel with the name of " + textChannelName);
        return textChannelName;
    }

    private static String getNextVoiceChannelName(GuildVoiceJoinEvent e)
    {
        int textChannelCount = e.getGuild().getTextChannels().size();
        //int voiceChannelCount = e.getGuild().getVoiceChannels().size();
        String voiceChannelName = constants.practiceVoiceChannelName + constants.VC_IDENTIFIERS[textChannelCount];

        System.out.println("Creating a voice channel with the name of " + voiceChannelName);
        return voiceChannelName;
    }
}
