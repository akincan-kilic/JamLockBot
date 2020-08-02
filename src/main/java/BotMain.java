import listeners.userListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotMain
{
    public static void main(String[]args) throws Exception
    {
        JDA jda = new JDABuilder("NzE2NjA0MzI0MzgxMTk2Mjkw.XtON8Q.-tPJmyYzNPfO-pt79Y5MF4y7tlI").build();
        jda.addEventListener(commands.commandBuilder.getCommands());
        jda.addEventListener(new userListener());

    }
}
