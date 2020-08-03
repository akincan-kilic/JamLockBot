package events;

import functions.Channels;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

public class VoiceJoinEvent
{
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
            System.out.println("User joined an empty voice channel");
            System.out.println("Attempting to create new voice and text channels...");

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
            String hostName = e.getChannelJoined().getName().split(" ")[4];
            if (e.getMember().getUser().getName().equals(hostName))
                e.getMember().mute(false).queue();
            else
                e.getMember().mute(true).queue();
        }
        else
            e.getMember().mute(false).queue();
    }
}
