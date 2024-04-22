Issue:

When using Keycloak in conjunction with FreeIPA (where users are managed in FreeIPA and Keycloak is configured with "User federation" using LDAP), if a user's password expires in FreeIPA, that user can still log in to Keycloak with the expired password.

Solution:

1 Need to map the value of krbPasswordExpiration from FreeIPA.  
1.1 We go to User federation and select our previously configured LDAP

![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/98d059a9-5565-4e44-a894-a9de9be5c909)

1.2 We navigate to the 'Mappers' tab and click the 'Add mapper' button."
![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/be23589e-e334-4826-b704-a6df500e5485)

1.2.1 In the "Name" field, enter the name of this mapper. To avoid confusion, let's name it 'krbPasswordExpiration'.  
1.2.2 Choose 'user-attribute-ldap-mapper' in the Mapper typescript.  
1.2.3 Name the User Model Attribute 'krbPasswordExpiration' as well.  
1.2.4 In the "LDAP Attributes" field, input 'krbPasswordExpiration'. This parameter can be viewed on FreeIPA by executing the following commands from the server console:  
[root@freeIPA /]# kinit user_name  
[root@freeIPA /]# ipa user-find --login user_name --all --raw  
1.2.5 Click 'Save'.

![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/68e8b91a-7484-4a7a-9528-8e3b41ef7f48)  
1.43 We navigate to the 'Settings' tab and choose 'Sync all users' from the Action dropdown.
