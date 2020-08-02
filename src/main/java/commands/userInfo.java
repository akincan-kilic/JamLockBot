package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class userInfo extends Command
{
    public userInfo()
    {
        this.guildOnly = true;
        this.name = "userInfo";
        this.aliases = new String[]{"user", "userinfo"};
        this.help = "Gives information about the user that's mentioned after the command.";
    }

    protected void execute(CommandEvent commandEvent)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        User user = commandEvent.getMessage().getMentionedUsers().get(0);
        //Member member = commandEvent.getGuild().getMembersByName(user.getName(), true).get(0);
        Member member = commandEvent.getGuild().getMemberById(user.getId());

        EmbedBuilder userEmbed = new EmbedBuilder();
        userEmbed.addField("User name: ", user.getName(), true);
        userEmbed.setThumbnail(user.getAvatarUrl());
        userEmbed.addField("NAME: ", user.getName(), true);
        userEmbed.addField("Online Status: ", member.getOnlineStatus().toString(), true);
        userEmbed.setFooter("This request was made at " + formatter.format(date) + " [UTC+03:00]");
        try
        {
            userEmbed.addField("Nickname: ", member.getNickname(), true);
        }
        catch (Exception e)
        {
            commandEvent.getChannel().sendMessage("Getting the nickname failed...").queue();
        }

        commandEvent.getChannel().sendMessage(userEmbed.build()).queue();
    }
}
