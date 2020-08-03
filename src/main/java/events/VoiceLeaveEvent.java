package events;

import functions.Channels;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VoiceLeaveEvent
{
    private static final Logger log = LoggerFactory.getLogger(Channels.class);

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
            log.info("User: {} left a voice channel", e.getMember().getUser().getName());
            log.info("Checking if there are any empty extra voice channels...");
            try
            {
                Channels.removeEmptyPracticeChannel(e);
            } catch (InterruptedException ex)
            {
                log.warn("If the setup command was called before this warning, ignore.");
            }
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
            Channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("This room is empty and locked, unlocking...").queue();
            log.info("Empty locked room found, attempting to force unlock...");
            Channels.forceUnlock(e);
        }

        for (Member voiceChannelMember : voiceChannelMembers)
        {
            if (voiceChannelMember.getUser().getName().equals(hostName))
                break;
            else
            {
                //Unlock the room.
                Channels.getMatchingTextChannel(e, e.getChannelLeft()).sendMessage("The host left the locked room, unlocking...").queue();
                log.info("The host left the locked room, attempting to force unlock...");
                Channels.forceUnlock(e);
            }
        }
    }
}
