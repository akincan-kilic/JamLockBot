package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

public class Setup extends Command
{
    private static final Logger log = LoggerFactory.getLogger(Setup.class);

    public Setup()
    {
        this.guildOnly = true;
        this.name = "setup";
        this.help = "Sets up the practice rooms. (Only an Admin or a Bot Manager is allowed to use this command)";
    }


    protected void execute(CommandEvent e)
    {
        log.info("Setup command called");

        boolean canUseCommand = false;
        for (Role role: e.getMember().getRoles())
        {
            if (role.getPermissions().contains(Permission.MANAGE_CHANNEL))
            {
                log.info("{} has the permission, executing the rest of the setup command", e.getAuthor().getName());
                canUseCommand = true;
            }
        }

        if (canUseCommand)
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
            log.info("Setup completed successfully!");
        }
        else
        {
            log.info("Setup command permission denied!");
            e.getChannel().sendMessage("You need to have the required permission in order to use this command!").queue();
        }
    }

    private void unSetup(CommandEvent e) throws InterruptedException
    {
        log.info("Un-setup command called");
        for (GuildChannel guildChannel : e.getGuild().getChannels())
        {
            if (guildChannel.getName().contains("Practice Room") || guildChannel.getName().contains("practice-room"))
            {
                guildChannel.delete().queue();
                Thread.sleep(100);
            }
        }
    }
}
