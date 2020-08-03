package events;

import functions.channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

import java.util.List;


public class voiceLeaveEvent
{
    private static boolean isLockedRoom(VoiceChannel voiceChannel)
    {
        return voiceChannel.getName().startsWith("🔒");
    }

    public static void voiceLeave(GuildVoiceLeaveEvent e)
    {
        if (isLockedRoom(e.getChannelLeft()))
        {
            System.out.println("Left locked channel");
            String hostName = e.getChannelLeft().getName().split(" ")[4];
            List<Member> voiceChannelMembers = e.getChannelLeft().getMembers();

            //This is a safety mechanism, if bot somehow fails to unlock the room when the host leaves, this will make sure its unlocked.
            if (e.getChannelLeft().getMembers().size() == 0)
            {
                channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("This room is empty and locked, attempting to unlock...").queue();
                functions.forceUnlock.forceUnlock(e);
            }

            for (Member voiceChannelMember : voiceChannelMembers)
            {
                if (voiceChannelMember.getUser().getName().equals(hostName))
                    break;
                else
                {
                    channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("The host left the locked room, attempting to unlock...").queue();
                    functions.forceUnlock.forceUnlock(e);
                    //unlock room
                }
            }
        }
    }
}
