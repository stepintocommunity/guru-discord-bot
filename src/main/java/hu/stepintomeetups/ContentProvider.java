package hu.stepintomeetups;

import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * Content provider which resolves and creates {@link EmbedBuilder} instances from the bot configuration messages.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ContentProvider {

    private final BotConfiguration botConfiguration;

    /**
     * Finds and creates an {@link EmbedBuilder} object based on the incoming message key.
     */
    public EmbedBuilder getByContentKey(String key) {
        return convertTo(botConfiguration.embedMessages().get(key));
    }

    /**
     * Finds and creates an {@link EmbedBuilder} object based on the incoming message key.
     */
    public String getMessageByKey(String key, Object... args) {
        String template = botConfiguration.messages().get(key);
        return MessageFormat.format(template, args);
    }

    private EmbedBuilder convertTo(BotConfiguration.EmbedMessage embedMessage) {
        return Optional.of(new EmbedBuilder())
                .map(embedBuilder -> mapToEmbedBuilder(embedMessage, embedBuilder))
                .orElseThrow();
    }

    private EmbedBuilder mapToEmbedBuilder(BotConfiguration.EmbedMessage embedMessage, EmbedBuilder embedBuilder) {
        embedMessage.author().ifPresent(embedMessageAuthor -> embedBuilder.setAuthor(embedMessageAuthor.name(), embedMessageAuthor.url(), embedMessageAuthor.iconUrl()));
        embedBuilder.setTitle(embedMessage.title());
        embedMessage.description().ifPresent(embedBuilder::setDescription);
        embedMessage.image().ifPresent(embedBuilder::setImage);
        embedBuilder.setFooter(embedMessage.footer());
        for (BotConfiguration.EmbedMessageField embedMessageField : embedMessage.fields()) {
            embedBuilder.addField(embedMessageField.name(), embedMessageField.message());
        }
        return embedBuilder;
    }

}
