package listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class userListener extends ListenerAdapter
{
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e)
    {
        VoiceChannel oldVoiceChannel = e.getOldValue();

        VoiceChannel newVoiceChannel = e.getNewValue();

        if (newVoiceChannel.getMembers().size() == 1)
        {
            System.out.println("User joined an empty voice channel");
            System.out.println("Attempting to create new voice and text channels...");

            try { functions.createChannel.createPracticeRoom(e); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e)
    {
        System.out.println("User left a voice channel");


        try { functions.removeEmptyChannel.removeEmptyPracticeChannel(e); } catch (InterruptedException ex) { ex.printStackTrace(); }
    }

}
