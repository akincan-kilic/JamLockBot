package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import functions.channels;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import utils.constants;

public class unlock extends Command
{
    public unlock()
    {
        this.name = "unlock";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent e)
    {
        String hostUserName = channels.getMatchingVoiceChannel(e, e.getMessage().getTextChannel()).getName().split(" ")[4];

        if (hostUserName.equals(e.getAuthor().getName()))
        {
            int totalChannels = e.getGuild().getCategoriesByName(constants.practiceCategoryName, false).get(0).getVoiceChannels().size();
            totalChannels += 1;

            TextChannel textChannel = e.getTextChannel();
            VoiceChannel voiceChannel = channels.getMatchingVoiceChannel(e, textChannel);
            assert voiceChannel != null;
            voiceChannel.getManager().setName(channels.getNextVoiceChannelName(totalChannels)).queue();
            textChannel.getManager().setName(channels.getNextTextChannelName(totalChannels)).queue();

            for (int i = 0; i < voiceChannel.getMembers().size(); i++)
            {
                voiceChannel.getMembers().get(i).mute(false).queue();
            }
        }
        else
        {
            e.getChannel().sendMessage("You need to be the host to unlock this channel!").queue();
        }
    }
}
