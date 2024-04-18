package org.keycloak.authentication.authenticators.conditional;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ConditionalUserAttributeValue implements ConditionalAuthenticator {

    static final ConditionalUserAttributeValue SINGLETON = new ConditionalUserAttributeValue();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        // Retrieve configuration
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();
        String attributeName = config.get(ConditionalUserAttributeValueFactory.CONF_ATTRIBUTE_NAME);
        String attributeValue = config.get(ConditionalUserAttributeValueFactory.CONF_ATTRIBUTE_EXPECTED_VALUE);
        boolean includeGroupAttributes = Boolean.parseBoolean(config.get(ConditionalUserAttributeValueFactory.CONF_INCLUDE_GROUP_ATTRIBUTES));
        boolean negateOutput = Boolean.parseBoolean(config.get(ConditionalUserAttributeValueFactory.CONF_NOT));
        boolean regexOutput = Boolean.parseBoolean(config.get(ConditionalUserAttributeValueFactory.REGEX));

        UserModel user = context.getUser();
        if (user == null) {
            throw new AuthenticationFlowException("Cannot find user for obtaining particular user attributes. Authenticator: " + ConditionalUserAttributeValueFactory.PROVIDER_ID, AuthenticationFlowError.UNKNOWN_USER);
        }

        if ("krbPasswordExpiration".equals(attributeName)) {
            String userDateStr = user.getFirstAttribute(attributeName);
            if (userDateStr == null) {
                throw new AuthenticationFlowException("User does not have the required attribute: " + attributeName, AuthenticationFlowError.INVALID_USER);
            }

            LocalDateTime userDate = LocalDateTime.parse(userDateStr, DateTimeFormatter.ofPattern("yyyyMMddHHmmssX"));
            LocalDateTime currentDate = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

            boolean result = currentDate.isBefore(userDate);

            return negateOutput != result;
        } else {
            boolean result = user.getAttributeStream(attributeName).anyMatch(attr -> regexOutput ? Pattern.compile(attributeValue).matcher(attr).matches() : Objects.equals(attr, attributeValue));
            if (!result && includeGroupAttributes) {
                result = KeycloakModelUtils.resolveAttribute(user, attributeName, true).stream().anyMatch(attr -> regexOutput ? Pattern.compile(attributeValue).matcher(attr).matches() : Objects.equals(attr, attributeValue));
            }
            return negateOutput != result;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Not used
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // Not used
    }

    @Override
    public void close() {
        // Does nothing
    }
}
