import listeners.userListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotMain
{
    public static void main(String[]args) throws Exception
    {
        JDA jda = new JDABuilder("<BOT TOKEN>").build();
        jda.addEventListener(commands.commandBuilder.getCommands());
        jda.addEventListener(new userListener());

    }
}
