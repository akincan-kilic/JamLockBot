package functions;

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
        int totalChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getVoiceChannels().size();
        totalChannels += 1;
        category.createTextChannel(channels.getNextTextChannelName(totalChannels)).queue();
        Thread.sleep(100);
        category.createVoiceChannel(channels.getNextVoiceChannelName(totalChannels)).queue();
        Thread.sleep(100);
    }


}
