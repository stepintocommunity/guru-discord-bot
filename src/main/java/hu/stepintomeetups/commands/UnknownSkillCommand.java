package hu.stepintomeetups.commands;

import com.vdurmont.emoji.EmojiParser;
import hu.stepintomeetups.skills.action.RoleAssociationAction;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Command which is handling the skill removal.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class UnknownSkillCommand extends GuruBotCommand {

    private final RoleAssociationAction roleAssociationAction;

    @Override
    public String getCommand() {
        return "i-dont-know";
    }

    @Override
    public void handleMessage(CertainMessageEvent event) {
        Message message = event.getMessage();
        message.getAuthor().asUser().ifPresent(user -> {
            String rawSkills = message.getContent().substring(getPrefixedCommand().length());
            String[] skills = rawSkills.split(",");
            roleAssociationAction.removeRolesFromUser(user, Arrays.stream(skills).map(String::toLowerCase).map(String::trim).collect(Collectors.toList()));
            message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
        });
    }
}
