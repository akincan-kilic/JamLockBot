package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.Objects;

public class ForceUnmute extends Command
{
    private static final Logger log = LoggerFactory.getLogger(ForceUnmute.class);

    ForceUnmute()
    {
        this.help = "Un-mutes an already force muted user";
        this.name = "forceunmute";
        this.aliases = new String[]{"unmute"};
        this.arguments = "<@USER>";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        boolean canUseCommand = false;
        for (Role role: commandEvent.getMember().getRoles())
        {
            if (role.getPermissions().contains(Permission.KICK_MEMBERS))
            {
                log.info("{} has the permission, executing the rest of the Force Unmute command", commandEvent.getAuthor().getName());
                canUseCommand = true;
            }
        }

        if (canUseCommand)
        {
            User userToUnmute = null;
            try
            {
                userToUnmute = commandEvent.getMessage().getMentionedUsers().get(0);
            }
            catch (Exception e)
            {
                commandEvent.getChannel().sendMessage("No mentioned users in the message!").queue();
            }

            if (userToUnmute != null)
            {
                userToUnmute.getId();
                Member memberToUnmute = commandEvent.getGuild().getMemberById(userToUnmute.getId());

                if (memberToUnmute != null)
                {
                    commandEvent.getGuild().removeRoleFromMember(memberToUnmute, Objects.requireNonNull(commandEvent.getGuild().getRoleById(Constants.MUTED_ROLE_ID))).queue();
                    memberToUnmute.mute(false).queue();
                }
                else
                    log.error("Couldn't find member to unmute!");
            }
            else
            {
                commandEvent.getChannel().sendMessage("No user specified in the message.").queue();
                log.error("Couldn't find user!");
            }
        }
    }
}
