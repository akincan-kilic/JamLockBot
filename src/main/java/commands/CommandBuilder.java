package commands;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandBuilder
{
    private static final Logger log = LoggerFactory.getLogger(CommandBuilder.class);


    public static CommandClient getCommands()
    {
        log.info("Inside the command builder.");
        CommandClientBuilder builder = new CommandClientBuilder();

        builder.setOwnerId("716604324381196290");
        builder.setPrefix("p!");
        builder.setHelpWord("help");
        builder.addCommand(new Setup());
        builder.addCommand(new Lock());
        builder.addCommand(new Unlock());
        builder.addCommand(new Bitrate());

        return builder.build();
    }
}
