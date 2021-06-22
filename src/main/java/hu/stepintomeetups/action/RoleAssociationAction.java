package hu.stepintomeetups.action;

import hu.stepintomeetups.ContentProvider;
import hu.stepintomeetups.configuration.BotConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/**
 * Action class which handles adding and removing roles from users.
 */
@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RoleAssociationAction {

    private final DiscordApi discordApi;
    private final BotConfiguration botConfiguration;
    private final ContentProvider contentProvider;

    /**
     * Adds a role to the user if the incoming emoji is known and associated with any role.
     *
     * @param emoji reacted emoji.
     * @param user  user who reacted.
     */
    public void assignEmojiBasedRoleToUser(Emoji emoji, User user) {
        getRoleByEmoji(emoji).ifPresentOrElse(
                skillConfig -> addRoleToUser(user, skillConfig.roleAssociation()),
                () -> log.warn("No role-association found with incoming emoji:[{}]", emoji));
    }

    /**
     * Removes a role from the user if the incoming emoji is known and associated with any role.
     *
     * @param emoji reacted emoji.
     * @param user  user who reacted.
     */
    public void removeEmojiBasedRoleToUser(Emoji emoji, User user) {
        getRoleByEmoji(emoji).ifPresentOrElse(
                skillConfig -> removeRoleFromUser(user, skillConfig.roleAssociation()),
                () -> log.warn("No role-association found with incoming emoji:[{}]", emoji));
    }

    /**
     * Add given role to the given user.
     * <p>
     * If the role is not configured an error message will be sent to the user.
     */
    public void addRoleToUser(User user, String roleName) {
        discordApi.getRolesByNameIgnoreCase(roleName)
                .stream()
                .findFirst()
                .ifPresentOrElse(role -> user.addRole(role, "User has marked this language as known, so the role is being assigned.")
                                .exceptionally(throwable -> {
                                    sendMessageToCustomerAboutFailedRoleAssignment(user, role);
                                    return null;
                                }),
                        () -> sendMessageToCustomerAboutFailedRoleAssignment(user, roleName));
    }

    /**
     * Add multiple roles to the given user.
     * <p>
     * If the role is not configured an error message will be sent to the user.
     */
    public void addRolesToUser(User user, List<String> roles) {
        roles.forEach(role -> {
            if (botConfiguration.skills().containsKey(role)) {
                addRoleToUser(user, role);
            } else {
                sendMessageToCustomerAboutUnknownRole(user, role);
            }
        });
    }

    /**
     * Remove multiple roles from the given user.
     * <p>
     * If the role is not configured an error message will be sent to the user.
     */
    public void removeRolesFromUser(User user, List<String> roles) {
        roles.forEach(role -> {
            if (botConfiguration.skills().containsKey(role)) {
                removeRoleFromUser(user, role);
            } else {
                sendMessageToCustomerAboutUnknownRole(user, role);
            }
        });
    }

    /**
     * Remove a role from the given user.
     * <p>
     * If the role is not configured an error message will be sent to the user.
     */
    public void removeRoleFromUser(User user, String roleName) {
        discordApi.getRolesByNameIgnoreCase(roleName)
                .stream()
                .findFirst()
                .ifPresentOrElse(role -> user.removeRole(role, "User has marked this language as unknown, so the role is being removed.")
                                .exceptionally(throwable -> {
                                    sendMessageToCustomerAboutFailedRoleDeletion(user, role);
                                    return null;
                                }),
                        () -> sendMessageToCustomerAboutFailedRoleDeletion(user, roleName));
    }

    private void sendMessageToCustomerAboutFailedRoleAssignment(User user, Object role) {
        String message = contentProvider.getMessageByKey("failed-role-association", role);
        user.sendMessage(message)
                .exceptionally(throwable -> {
                    log.warn("Error during sending message to user:[{}]", user);
                    return null;
                });
    }

    private void sendMessageToCustomerAboutFailedRoleDeletion(User user, Object role) {
        String message = contentProvider.getMessageByKey("failed-role-deletion", role);
        user.sendMessage(message)
                .exceptionally(throwable -> {
                    log.warn("Error during sending message to user:[{}]", user);
                    return null;
                });
    }

    private void sendMessageToCustomerAboutUnknownRole(User user, Object role) {
        String message = contentProvider.getMessageByKey("unknown-role", role);
        user.sendMessage(message)
                .exceptionally(throwable -> {
                    log.warn("Error during sending message to user:[{}]", user);
                    return null;
                });
    }

    private Optional<BotConfiguration.SkillConfig> getRoleByEmoji(Emoji emoji) {
        return emoji.asKnownCustomEmoji()
                .map(customEmoji -> botConfiguration.skills().get(customEmoji.getName()));
    }


}
