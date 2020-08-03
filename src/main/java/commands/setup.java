package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.GuildChannel;
import utils.constants;

import java.util.List;

public class setup extends Command
{
    public setup()
    {
        this.guildOnly = true;
        this.name = "setup";
        this.help = "Sets up the practice rooms. (Also removes all the existing practice rooms before setup)";
    }

    private void unSetup(CommandEvent e) throws InterruptedException
    {
        List<GuildChannel> guildChannelList = e.getGuild().getChannels();

        for (GuildChannel guildChannel : guildChannelList)
        {
            if (guildChannel.getName().contains("Practice Room") || guildChannel.getName().contains("practice-room"))
            {
                guildChannel.delete().queue();
                Thread.sleep(100);
            }
        }
    }

    protected void execute(CommandEvent e)
    {
        try { unSetup(e); } catch (InterruptedException ex) { ex.printStackTrace(); }

        String categoryName = constants.practiceCategoryName;
        String textChannelName = constants.practiceTextChannelName;
        String voiceChannelName = constants.practiceVoiceChannelName;

        e.getGuild().createCategory(categoryName).queue(cat ->
        {
            for (int i = 0; i < 5; i++)
            {
                cat.createTextChannel(textChannelName + (constants.VC_IDENTIFIERS[i])).queue();
                cat.createVoiceChannel(voiceChannelName + (constants.VC_IDENTIFIERS[i])).queue();
                try { Thread.sleep(100); } catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        });
    }
}
