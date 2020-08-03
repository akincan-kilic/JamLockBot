package listeners;

import events.VoiceJoinEvent;
import events.VoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class UserListener extends ListenerAdapter
{
    private static final Logger log = LoggerFactory.getLogger(UserListener.class);

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e)
    {
        VoiceJoinEvent.voiceJoin(e);
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e)
    {
        VoiceLeaveEvent.voiceLeave(e);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e)
    {
        log.info("User moved, initiating fake join and leave events...");

        GuildVoiceJoinEvent joinEvent = new GuildVoiceJoinEvent(e.getJDA(), e.getResponseNumber(), e.getMember());
        VoiceJoinEvent.voiceMoveJoin(joinEvent);

        GuildVoiceLeaveEvent leaveEvent = new GuildVoiceLeaveEvent(e.getJDA(), e.getResponseNumber(), e.getMember(), e.getChannelLeft());
        VoiceLeaveEvent.voiceMoveLeave(leaveEvent);
    }
}
