package hu.stepintomeetups.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
@ConfigMapping(prefix = "guru-bot")
public interface BotConfiguration {

    boolean enabled();

    String token();

    default String commandPrefix() {
        return "";
    }

    Map<String, SkillConfig> skills();

    List<BotCommand> commands();

    Map<String, EmbedMessage> messages();

    interface SkillConfig {
        String name();

        String emoji();

        String roleAssociation();

        default String getMessagizedTag() {
            return "<" + emoji() + ">";
        }
    }

    interface BotCommand {
        String command();

        String description();
    }

    interface EmbedMessage {

        Optional<EmbedMessageAuthor> author();

        String title();

        Optional<String> description();

        Optional<String> image();

        List<EmbedMessageField> fields();

        String footer();
    }

    interface EmbedMessageField {
        String name();

        String message();
    }

    interface EmbedMessageAuthor {
        String name();

        String url();

        String iconUrl();
    }
}


