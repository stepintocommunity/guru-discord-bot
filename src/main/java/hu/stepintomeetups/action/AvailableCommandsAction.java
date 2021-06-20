package hu.stepintomeetups.action;

import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class AvailableCommandsAction {

    private final BotConfiguration botConfiguration;

    /**
     * @return
     */
    public List<BotCommand> getAllAvailableCommands() {
        return botConfiguration.commands()
                .stream().map(botCommand -> BotCommand.builder()
                        .command(getPrefixedCommand(botCommand.command()))
                        .description(botCommand.description())
                        .build())
                .collect(Collectors.toList());
    }

    private String getPrefixedCommand(String command) {
        String commandPrefix = botConfiguration.commandPrefix();
        return commandPrefix != null && !commandPrefix.isBlank() ? "/" + commandPrefix + "-" + command : "/" + command;
    }

}
