Issue:
When using Keycloak in conjunction with FreeIPA (where users are managed in FreeIPA and Keycloak is configured with "User federation" using LDAP), if a user's password expires in FreeIPA, that user can still log in to Keycloak with the expired password.

Solution:
