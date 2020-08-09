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
import java.util.concurrent.TimeUnit;

public class ForceMute extends Command
{

    private static final Logger log = LoggerFactory.getLogger(ForceMute.class);

    ForceMute ()
    {
        this.help = "Mutes an user forever, even on unlocked rooms.";
        this.name = "forcemute";
        this.aliases = new String[]{"mute"};
        this.arguments = "<@USER>, <TIME_MINUTES>, <REASON>";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        boolean canUseCommand = false;
        for (Role role: commandEvent.getMember().getRoles())
        {
            if (role.getPermissions().contains(Permission.KICK_MEMBERS))
            {
                log.info("{} has the permission, executing the rest of the Force Mute command", commandEvent.getAuthor().getName());
                canUseCommand = true;
            }
        }

        if (canUseCommand)
        {
            User userToMute = null;
            try
            {
                userToMute = commandEvent.getMessage().getMentionedUsers().get(0);
            }
            catch (Exception e)
            {
                commandEvent.getChannel().sendMessage("No mentioned users in the message!").queue();
            }

            if (userToMute != null)
            {
                userToMute.getId();
                Member memberToMute = commandEvent.getGuild().getMemberById(userToMute.getId());

                if (memberToMute != null)
                {
                    String[] commandArgs = commandEvent.getArgs().split(" ");//p!forcemute 0USER 1TIMEOUT 2+REASON...
                    if (commandArgs.length < 3)
                    {
                        commandEvent.getMessage().getChannel().sendMessage("You need to specify the <@USER> <TIMEOUT_MINUTES> <REASON>" +
                                "\n Leave only one space between each argument!").queue();
                    }
                    else
                    {
                        boolean failed = false;

                        int timeOutMinutes = 1;
                        String timeOutPerma = "";
                        try
                        {
                            timeOutMinutes = Integer.parseInt(commandArgs[1]);
                        }
                        catch (Exception e)
                        {
                            try
                            {
                                timeOutPerma = commandArgs[1];
                            }
                            catch (Exception e2)
                            {
                                failed = true;
                                commandEvent.getChannel().sendMessage("Entered time wasn't a number or a perm.").queue();
                            }
                        }

                        StringBuilder toTheLogs = new StringBuilder();
                        try
                        {
                            for (int i = 2; i < commandArgs.length; i++)
                            {
                                toTheLogs.append(commandArgs[i]).append(" ");
                            }
                        }
                        catch (Exception e)
                        {
                            failed = true;
                            commandEvent.getChannel().sendMessage("Failed when getting the reason...").queue();
                        }

                        if (!failed)
                        {
                            if (timeOutPerma.equals("perm"))
                            {
                                timeOutPerma = "";
                                commandEvent.getGuild().getTextChannelById(Constants.LOG_CHANNEL_ID).sendMessage
                                        (userToMute.getAsMention() + " has been muted for eternity by, "
                                                + commandEvent.getAuthor().getAsMention() + "\nREASON: " + toTheLogs.toString()).queue();

                                //MUTE AFTER USING COMMAND
                                commandEvent.getGuild().addRoleToMember(memberToMute, Objects.requireNonNull(commandEvent.getGuild().getRoleById(Constants.MUTED_ROLE_ID))).queue();
                                memberToMute.mute(true).queue();
                                commandEvent.getChannel().sendMessage(userToMute.getName() + " has been successfully force muted for eternity."
                                + "\nREASON: " + toTheLogs.toString()).queue();
                            }
                            else
                            {
                                //LOG TO THE CHANNEL
                                commandEvent.getGuild().getTextChannelById(Constants.LOG_CHANNEL_ID).sendMessage
                                        (userToMute.getAsMention() + " has been muted for " + timeOutMinutes + " minutes by, "
                                                + commandEvent.getAuthor().getAsMention() + "\nREASON: " + toTheLogs.toString()).queue();

                                //UNMUTE AFTER TIMEOUT
                                commandEvent.getGuild().removeRoleFromMember(memberToMute, Objects.requireNonNull(commandEvent.getGuild().getRoleById(Constants.MUTED_ROLE_ID))).queueAfter(timeOutMinutes, TimeUnit.MINUTES);
                                memberToMute.mute(false).queueAfter(timeOutMinutes+1, TimeUnit.MINUTES);

                                //MUTE AFTER USING COMMAND
                                commandEvent.getGuild().addRoleToMember(memberToMute, Objects.requireNonNull(commandEvent.getGuild().getRoleById(Constants.MUTED_ROLE_ID))).queue();
                                memberToMute.mute(true).queue();
                                commandEvent.getChannel().sendMessage(userToMute.getName() + " has been successfully force muted for " + timeOutMinutes + " minutes."
                                        + "\nREASON: " + toTheLogs.toString()).queue();
                            }
                        }
                    }
                }
                else
                    log.error("Couldn't find member!");
            }
            else
            {
                log.error("Couldn't find user!");
            }
        }
        else
        {
            commandEvent.getChannel().sendMessage("You need to have the permission to kick users to be able to use this command!").queue();
        }
    }
}
