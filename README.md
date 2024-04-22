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
1.3 We navigate to the 'Settings' tab and choose 'Sync all users' from the Action dropdown.

2 Upload the JAR file to the server.    
2.1 Download the keycloak-password-expiration-check-v23.0.3.jar https://github.com/NeveykoD/keycloak-password-expiration-check/releases/tag/keyclaok   
2.2 Upload it to /opt/keycloak/providers/ and ensure that the owner is keycloak:keycloak.  
2.3 We go to the directory /opt/keycloak/bin/ and run the command ./kc.sh build.    
2.4 Restart the service: systemctl restart keycloak.  

3 Authentication flow creation.  
3.1 In Keycloak, we navigate to Authentication and either duplicate the current flow or create a new one.  
![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/9e137bbe-48f5-4dbe-8c06-e3789c91381f)  
3.2 In "Condition - user attribute," we input the attribute krbPasswordExpiration.  
![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/f1cfda8e-3f28-4fc6-86f0-fc69ff396fa5)  
3.3 In "Deny access," we input the error message: "Your IPA password has expired, please change your password."  
![image](https://github.com/NeveykoD/keycloak-password-expiration-check/assets/109217257/d02559a8-8808-4f14-a402-612b9b689dec)  


