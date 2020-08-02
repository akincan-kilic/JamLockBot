package listeners;

import functions.channels;
import net.dv8tion.jda.api.entities.Member;
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
        //VoiceChannel oldVoiceChannel = e.getOldValue();
        VoiceChannel newVoiceChannel = e.getNewValue();

        //If an user joins a locked room, mute them.
        if (isLockedRoom(e.getChannelJoined()))
        {
            String hostName = e.getChannelJoined().getName().split(" ")[4];
            if (e.getMember().getUser().getName().equals(hostName))
            {
                e.getMember().mute(false).queue();
            }
            else
            {
                e.getMember().mute(true).queue();
            }
        }
        else
        {
            e.getMember().mute(false).queue();
        }

        if (newVoiceChannel.getMembers().size() == 1)
        {
            System.out.println("User joined an empty voice channel");
            System.out.println("Attempting to create new voice and text channels...");

            try { functions.createChannel.createPracticeRoom(e); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private boolean isLockedRoom(VoiceChannel voiceChannel)
    {
        return voiceChannel.getName().startsWith("🔒");
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e)
    {
        if (isLockedRoom(e.getChannelLeft()))
        {
            System.out.println("Left locked channel");
            String hostName = e.getChannelLeft().getName().split(" ")[4];

            List<Member> voiceChannelMembers = e.getChannelLeft().getMembers();

            if (e.getChannelLeft().getMembers().size() == 0)
            {
                channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("This room is empty and locked, attempting to unlock...").queue();
                functions.forceUnlock.forceUnlock(e);
            }

            for (int i = 0; i < voiceChannelMembers.size(); i++)
            {
                if (voiceChannelMembers.get(i).getUser().getName().equals(hostName))
                {
                    System.out.println("aaa");
                    break;
                }
                else
                {
                    System.out.println("Unlock the room idiot");
                    channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("The host left the locked room, attempting to unlock...").queue();
                    functions.forceUnlock.forceUnlock(e);
                    //unlock room
                }
            }
        }

        System.out.println("User left a voice channel");
        try { functions.removeEmptyChannel.removeEmptyPracticeChannel(e); } catch (InterruptedException ex) { ex.printStackTrace(); }
    }



}
