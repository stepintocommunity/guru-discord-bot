package hu.stepintomeetups;

import hu.stepintomeetups.configuration.BotConfiguration;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.source.yaml.YamlConfigSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class WTF {

    public static void main(String[] args) throws IOException {
        SmallRyeConfig config = new SmallRyeConfigBuilder()
                .withSources(new YamlConfigSource(WTF.class.getResource("/application.properties")))
                .withMapping(BotConfiguration.class,"guru-bot")
                .build();

        final List<String> propertyNames = StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                .collect(toList());

        BotConfiguration value = config.getConfigMapping(BotConfiguration.class);
        System.out.println(value);
        System.out.println(propertyNames);

    }

}
