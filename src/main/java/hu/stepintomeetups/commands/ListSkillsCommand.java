package hu.stepintomeetups.commands;

import com.vdurmont.emoji.EmojiParser;
import hu.stepintomeetups.skills.action.SkillsAction;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.enterprise.context.ApplicationScoped;

/**
 * Command which is listing the configured skills.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ListSkillsCommand extends GuruBotCommand {

    private final SkillsAction skillsAction;

    @Override
    public String getCommand() {
        return "list-skills";
    }

    @Override
    public void handleMessage(CertainMessageEvent event) {
        Message message = event.getMessage();
        message.getAuthor().asUser().ifPresent(skillsAction::sendKnownSkillsToUser);
        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
    }
}
