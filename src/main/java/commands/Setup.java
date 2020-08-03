package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.GuildChannel;
import utils.Constants;

public class Setup extends Command
{
    public Setup()
    {
        this.guildOnly = true;
        this.name = "setup";
        this.help = "Sets up the practice rooms. (Also removes all the existing practice rooms before setup)";
    }

    private void unSetup(CommandEvent e) throws InterruptedException
    {
        for (GuildChannel guildChannel : e.getGuild().getChannels())
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

        String categoryName = Constants.PRACTICE_CATEGORY_NAME;
        String textChannelName = Constants.PRACTICE_TEXT_CHANNEL_NAME;
        String voiceChannelName = Constants.PRACTICE_VOICE_CHANNEL_NAME;

        e.getGuild().createCategory(categoryName).queue(cat ->
        {
            for (int i = 0; i < 5; i++)
            {
                cat.createTextChannel(textChannelName + (Constants.VC_IDENTIFIERS[i])).queue();
                cat.createVoiceChannel(voiceChannelName + (Constants.VC_IDENTIFIERS[i])).queue();
                try { Thread.sleep(100); } catch (InterruptedException ex) { ex.printStackTrace(); }
            }
        });
    }
}
