package events;

import functions.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

import java.util.List;

public class VoiceLeaveEvent
{
    private static boolean isLockedRoom(VoiceChannel voiceChannel)
    {
        return voiceChannel.getName().startsWith("🔒");
    }

    public static void voiceLeave(GuildVoiceLeaveEvent e)
    {
        if (isLockedRoom(e.getChannelLeft()))
            lockedLeaveEvent(e);
        else
        {
            System.out.println("User left a voice channel");
            System.out.println("Checking if there are any empty extra voice channels...");
            try { Channels.removeEmptyPracticeChannel(e); } catch (InterruptedException ex) { ex.printStackTrace(); }
        }
    }

    public static void voiceMoveLeave(GuildVoiceLeaveEvent e)
    {
        if (isLockedRoom(e.getChannelLeft()))
            lockedLeaveEvent(e);
    }

    public static void lockedLeaveEvent (GuildVoiceLeaveEvent e)
    {
        String hostName = e.getChannelLeft().getName().split(" ")[4];
        List<Member> voiceChannelMembers = e.getChannelLeft().getMembers();

        //This is a safety mechanism, if bot somehow fails to unlock the room when the host leaves, this will make sure its unlocked.
        if (e.getChannelLeft().getMembers().size() == 0)
        {
            Channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("This room is empty and locked, attempting to unlock...").queue();
            Channels.forceUnlock(e);
        }

        for (Member voiceChannelMember : voiceChannelMembers)
        {
            if (voiceChannelMember.getUser().getName().equals(hostName))
                break;
            else
            {
                //Unlock the room.
                Channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("The host left the locked room, attempting to unlock...").queue();
                Channels.forceUnlock(e);
            }
        }
    }
}
