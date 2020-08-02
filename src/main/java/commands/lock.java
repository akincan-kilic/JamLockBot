package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.channels;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import utils.constants;

public class lock extends Command
{
    public lock()
    {
        this.name = "lock";
        this.guildOnly = true;
        this.help = "Locks the practice room so only the host can talk.";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        System.out.println("Inside the lock execute event");
        TextChannel textChannel = commandEvent.getMessage().getTextChannel();


        if (!textChannel.getName().split("-")[0].equals("practice"))
        {
            commandEvent.getMessage().getTextChannel().sendMessage("You cannot use this command outside of a practice room!").queue();
        }
        else
        {
            VoiceChannel voiceChannel = channels.getMatchingVoiceChannel(commandEvent, textChannel);
            User hostUser = commandEvent.getMessage().getAuthor();

            assert voiceChannel != null;
            voiceChannel.getManager().setName(constants.LOCK_ICON + voiceChannel.getName() + " " + hostUser.getName()).queue();
            textChannel.getManager().setName(constants.LOCK_ICON + textChannel.getName()).queue();

            for (int i = 0; i < voiceChannel.getMembers().size(); i++)
            {
                if (voiceChannel.getMembers().get(i).getUser() != hostUser)
                {
                    voiceChannel.getMembers().get(i).mute(true).queue();
                }
            }
        }
    }
}
