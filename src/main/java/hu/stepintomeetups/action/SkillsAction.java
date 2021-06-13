package hu.stepintomeetups.action;

import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class SkillsAction {

    private final BotConfiguration botConfiguration;

    /**
     * @param user
     */
    public void sendKnownSkillsToUser(User user) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Step Into Meetup szerverén jelenleg elérhető skillek")
                .addField("Instrukció", "A skillek magadhoz rendeléséhez használd a /i-know <skill_neve> parancsot")
                .addField("Instrukció", "Ha egyszerre többet szeretnél magadhoz rendelni akkor a /i-know <skill_neve_1>,<skill_neve_2> és így tovább vessző segítségével elválasztva")
                .setImage("https://avatars.githubusercontent.com/u/43297388?s=200&v=4")
                .setFooter("https://stepintomeetups.hu");
        Map<String, BotConfiguration.SkillConfig> emojis = botConfiguration.skills();
        emojis.forEach((key, skillConfig) -> embed.addInlineField(skillConfig.name(), skillConfig.getMessagizedTag()));
        user.sendMessage(embed)
                .exceptionally(throwable -> {
                    log.error("Error during sending message to user:[" + user + "]");
                    return null;
                });
    }
}
