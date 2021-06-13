package hu.stepintomeetups.configuration;

import io.smallrye.config.ConfigMapping;

import java.util.Map;

/**
 *
 */
@ConfigMapping(prefix = "guru-bot")
public interface BotConfiguration {

    String token();

    default String commandPrefix() {
        return "";
    }

    Map<String, SkillConfig> skills();

    interface SkillConfig {
        String name();

        String emoji();

        String roleAssociation();

        default String getMessagizedTag() {
            return "<" + emoji() + ">";
        }
    }
}


