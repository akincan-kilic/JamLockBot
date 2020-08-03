package commands;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

public class CommandBuilder
{
    public static CommandClient getCommands()
    {
        CommandClientBuilder builder = new CommandClientBuilder();

        builder.setOwnerId("716604324381196290");
        builder.setPrefix("j!");
        builder.setHelpWord("help");
        builder.addCommand(new Setup());
        builder.addCommand(new Lock());
        builder.addCommand(new Unlock());

        return builder.build();
    }
}
