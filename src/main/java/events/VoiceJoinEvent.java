package events;

import functions.Channels;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

public class VoiceJoinEvent
{
    private static final Logger log = LoggerFactory.getLogger(VoiceJoinEvent.class);

    private static boolean isLockedRoom(VoiceChannel voiceChannel)
    {
        return voiceChannel.getName().startsWith("🔒");
    }

    public static void voiceJoin(GuildVoiceJoinEvent e)
    {
        VoiceChannel newVoiceChannel = e.getNewValue();

        //If an user joins a locked room, mute them.
        muteNewUserInLockedRoom(e);

        if (newVoiceChannel.getMembers().size() == 1)
        {
            log.info("User: {} joined an empty voice channel", newVoiceChannel.getMembers().get(0).getUser().getName());
            try { Channels.createPracticeRoom(e); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public static void voiceMoveJoin(GuildVoiceJoinEvent e)
    {
        muteNewUserInLockedRoom(e);
    }

    public static void muteNewUserInLockedRoom(GuildVoiceJoinEvent e)
    {
        if (isLockedRoom(e.getChannelJoined()))
        {
            log.info("Muting user: {} because they joined a locked room and they are not the host", e.getMember().getUser().getName());
            String hostID = e.getChannelJoined().getName().split(" ")[2];
            if (e.getMember().getUser().getId().equals(hostID))
                e.getMember().mute(false).queue();
            else
                e.getMember().mute(true).queue();
        }
        else
        {
            boolean muteUser = false;

            for (Role role: e.getMember().getRoles())
            {
                if (role.getId().equals(Constants.MUTED_ROLE_ID))
                {
                    e.getMember().mute(true).queue();
                    muteUser = true;
                }
            }

            if (muteUser)
            {
                e.getMember().mute(true).queue();
            }
            else
                e.getMember().mute(false).queue();
        }
    }
}
