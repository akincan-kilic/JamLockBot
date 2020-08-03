import commands.CommandBuilder;
import listeners.UserListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotMain
{
    public static void main(String[]args) throws Exception
    {
        final String BOT_TOKEN = "";

        JDA jda = JDABuilder.createDefault(BOT_TOKEN).build();
        jda.addEventListener(CommandBuilder.getCommands());
        jda.addEventListener(new UserListener());
    }
}
