package hu.stepintomeetups.listeners;

import com.vdurmont.emoji.EmojiParser;
import hu.stepintomeetups.action.AvailableCommandsAction;
import hu.stepintomeetups.action.RoleAssociationAction;
import hu.stepintomeetups.action.SkillsAction;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Listener which listens to the 'commands', 'list-skills', 'i-know' and 'i-dont-know' commands.
 */
@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class SkillCenterListener implements MessageCreateListener {

    private static final String COMMANDS = "commands";
    private static final String SKILLS_COMMAND = "list-skills";
    private static final String I_KNOW_COMMAND = "i-know";
    private static final String I_DONT_KNOW_COMMAND = "i-dont-know";

    private final BotConfiguration botConfiguration;
    private final RoleAssociationAction roleAssociationAction;
    private final SkillsAction skillsAction;
    private final AvailableCommandsAction availableCommandsAction;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();
        message.getUserAuthor()
                .ifPresent(user -> {
                    if (content.startsWith(getPrefixedCommand(COMMANDS))) {
                        availableCommandsAction.sendAllCommandsToUser(user);
                        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                    } else if (content.startsWith(getPrefixedCommand(SKILLS_COMMAND))) {
                        skillsAction.sendKnownSkillsToUser(user);
                        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                    } else if (content.startsWith(getPrefixedCommand(I_KNOW_COMMAND))) {
                        String rawSkills = content.substring(getPrefixedCommand(I_KNOW_COMMAND).length());
                        String[] skills = rawSkills.split(",");
                        roleAssociationAction.addRolesToUser(user, Arrays.stream(skills).map(String::toLowerCase).map(String::trim).collect(Collectors.toList()));
                        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                    } else if (content.startsWith(getPrefixedCommand(I_DONT_KNOW_COMMAND))) {
                        String rawSkills = content.substring(getPrefixedCommand(I_DONT_KNOW_COMMAND).length());
                        String[] skills = rawSkills.split(",");
                        roleAssociationAction.removeRolesFromUser(user, Arrays.stream(skills).map(String::toLowerCase).map(String::trim).collect(Collectors.toList()));
                        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
                    }
                });
    }

    private String getPrefixedCommand(String command) {
        String commandPrefix = botConfiguration.commandPrefix();
        return commandPrefix != null && !commandPrefix.isBlank() ? "/" + commandPrefix + "-" + command : "/" + command;
    }

}
