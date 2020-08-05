package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.Channels;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bitrate extends Command
{
    private static final Logger log = LoggerFactory.getLogger(Bitrate.class);

    public Bitrate()
    {
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.name = "bitrate";
        this.help = "Sets the bitrate of the corresponding voice channel from the text channel you used this command in.\n(Only Bot Managers, VC Police or an Admin can use this command!)";
        this.guildOnly = true;
        this.arguments = "[bitrateToSet]";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        log.info("Bitrate command called");

        if (Channels.isPracticeRoom(commandEvent.getTextChannel()))
        {
            VoiceChannel voiceChannel = Channels.getMatchingVoiceChannel(commandEvent, commandEvent.getTextChannel());
            assert voiceChannel != null;

            int bitRate = 64;

            if (!commandEvent.getArgs().isEmpty())
            {
                int maxBitrateOfGuild = commandEvent.getGuild().getMaxBitrate()/1000;
                try
                {
                    bitRate = Integer.parseInt(commandEvent.getArgs());
                }
                catch (Exception e)
                {
                    log.info("Invalid bitrate entered: " + commandEvent.getArgs());
                    commandEvent.getChannel().sendMessage(commandEvent.getArgs() + " is not a valid integer!").queue();
                }

                if (bitRate > maxBitrateOfGuild)
                {
                    commandEvent.getChannel().sendMessage("Bitrate cannot go above the servers max bitrate! " + maxBitrateOfGuild + "kbps!").queue();
                }
                else if (bitRate < 8)
                {
                    commandEvent.getChannel().sendMessage("Bitrate cannot be below 8kbps!").queue();
                }
                else
                {
                    boolean failed = false;
                    try
                    {
                        voiceChannel.getManager().setBitrate(bitRate*1000).queue();
                    }
                    catch (Exception ex)
                    {
                        log.error(ex.getMessage());
                        failed = true;
                        commandEvent.getChannel().sendMessage("Failed to set the bitrate.").queue();
                    }
                    if (!failed)
                    {
                        log.info(voiceChannel.getName() + "'s bitrate has been set to: " + bitRate + "kbps!");
                        commandEvent.getChannel().sendMessage("Bitrate of the channel has been successfully set to " + bitRate +"kbps!").queue();
                    }
                }
            }
        }
        else
        {
            commandEvent.getChannel().sendMessage("You have to be in a practice room text channel in order to use this command!").queue();
        }
    }
}
