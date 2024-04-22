Issue:

When using Keycloak in conjunction with FreeIPA (where users are managed in FreeIPA and Keycloak is configured with "User federation" using LDAP), if a user's password expires in FreeIPA, that user can still log in to Keycloak with the expired password.

Solution:
Need to map the value of krbPasswordExpiration from FreeIPA. 
We go to User federation and select our previously configured LDAP
![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/98d059a9-5565-4e44-a894-a9de9be5c909)
