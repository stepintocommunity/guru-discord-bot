package hu.stepintomeetups.commands;

import com.vdurmont.emoji.EmojiParser;
import hu.stepintomeetups.action.RoleAssociationAction;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Command which is handling the skill association.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class KnownSkillCommand extends GuruBotCommand {

    private final RoleAssociationAction roleAssociationAction;

    @Override
    public String getCommand() {
        return "i-know";
    }

    @Override
    public void handleMessage(CertainMessageEvent event) {
        Message message = event.getMessage();
        message.getAuthor().asUser().ifPresent(user -> {
            String rawSkills = message.getContent().substring(getPrefixedCommand().length());
            String[] skills = rawSkills.split(",");
            roleAssociationAction.addRolesToUser(user, Arrays.stream(skills).map(String::toLowerCase).map(String::trim).collect(Collectors.toList()));
            message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
        });
    }
}
