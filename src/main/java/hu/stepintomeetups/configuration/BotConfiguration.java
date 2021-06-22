package hu.stepintomeetups.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Bot configuration.
 */
@ConfigMapping(prefix = "guru-bot")
public interface BotConfiguration {

    /**
     * Determines if the bot is enabled or not.
     */
    boolean enabled();

    /**
     * Discord bot token.
     */
    String token();

    /**
     * Returns the common prefix for all command.
     * <p>
     * By default the commands are having no common prefix.
     */
    default String commandPrefix() {
        return "";
    }

    /**
     * Map of the available/configured skills on the server it is being deployed.
     */
    Map<String, SkillConfig> skills();

    /**
     * List of the commands are supported by the bot.
     */
    List<BotCommand> commands();

    /**
     * Map of the messages for the content that are being provided by the bot to the end users.
     */
    Map<String, EmbedMessage> embedMessages();

    /**
     * Map of the messages for the content that are being provided by the bot to the end users.
     */
    Map<String, String> messages();

    /**
     * Interface representing a skill config.
     * <p>
     * Each skill has to have a name, an emoji, a role association which is the name of the role on the server it is being deployed.
     */
    interface SkillConfig {
        String name();

        String emoji();

        String roleAssociation();

        default String getMessagizedTag() {
            return "<" + emoji() + ">";
        }
    }

    /**
     * Interface representing a bot command.
     * <p>
     * Each command has a command keyword and a description.
     */
    interface BotCommand {
        String command();

        String description();
    }

    /**
     * Interface representing an embed message.
     */
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


