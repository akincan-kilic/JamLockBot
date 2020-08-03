package commands;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandBuilder
{
    public static CommandClient getCommands()
    {
        Logger logger = LoggerFactory.getLogger("TEST");
        logger.info("Inside the command builder.");
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
