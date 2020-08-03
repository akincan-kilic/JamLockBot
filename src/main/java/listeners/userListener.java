package listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class userListener extends ListenerAdapter
{
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e)
    {
        events.voiceJoinEvent.voiceJoin(e);
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e)
    {
        events.voiceLeaveEvent.voiceLeave(e);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e)
    {
        System.out.println("User moved, initiating fake join and leave events...");

        GuildVoiceJoinEvent joinEvent = new GuildVoiceJoinEvent(e.getJDA(), e.getResponseNumber(), e.getMember());
        events.voiceJoinEvent.voiceMoveJoin(joinEvent);

        GuildVoiceLeaveEvent leaveEvent = new GuildVoiceLeaveEvent(e.getJDA(), e.getResponseNumber(), e.getMember(), e.getChannelLeft());
        events.voiceLeaveEvent.voiceLeave(leaveEvent);
    }



}
