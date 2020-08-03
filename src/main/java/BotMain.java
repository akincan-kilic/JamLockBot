import commands.CommandBuilder;
import listeners.UserListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Scanner;

public class BotMain
{
    public static void main(String[]args) throws Exception
    {
        Scanner botTokenInput = new Scanner(System.in);
        System.out.print("Enter the bot token: ");
        final String BOT_TOKEN = botTokenInput.nextLine();

        JDA jda = JDABuilder.createDefault(BOT_TOKEN).build();
        jda.addEventListener(CommandBuilder.getCommands());
        jda.addEventListener(new UserListener());
    }
}
