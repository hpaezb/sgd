package pe.gob.onpe.autentica.util;

import java.util.Date;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.NameNotFoundException;
/**
 * @author hpaez
 */
/* Hermes 07/10/19 LDAP  */
public class LDAPTest {

    public LDAPTest() {
    }
       
    public static class LDAP{
        static String ATTRIBUTE_FOR_USER = "sAMAccountName";
        public Attributes authenticateUser(String username, String pass, String _domain, String host, String dn){

            String returnedAtts[] ={ "sn", "givenName", "mail", "sAMAccountName", "displayName", "userWorkstations", "memberOf" , "accountExpires", "pwdLastSet", "userAccountControl"};
            String searchFilter = "(&(objectClass=user)(" + ATTRIBUTE_FOR_USER + "=" + username + "))";
            SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchBase = dn;

            Hashtable environment = new Hashtable();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, Constantes.INITIAL_CONTEXT_FACTORY);
            environment.put(Context.PROVIDER_URL, "ldap://" + host + ":389");
            environment.put(Context.SECURITY_AUTHENTICATION, Constantes.SECURITY_AUTHENTICATION);
            environment.put(Context.SECURITY_PRINCIPAL, username + "@" + _domain);
            environment.put(Context.SECURITY_CREDENTIALS, pass);
            environment.put(Context.REFERRAL, "follow");

            LdapContext ctxGC = null;
            Attributes attrs = null;
            NamingEnumeration answer = null;
            System.out.println("Entorno--> " + environment);
            try{
                ctxGC = new InitialLdapContext(environment, null);
                answer = ctxGC.search(searchBase, searchFilter, searchCtls);
                if (answer.hasMoreElements()){
                    while (answer.hasMoreElements()){
                        SearchResult sr = (SearchResult)answer.next();
                        attrs = sr.getAttributes();
                        String usuarioGrupo = (String) attrs.get("sAMAccountName").get();
                        System.out.println("attrs 1: " + attrs);
                        if (attrs != null){
                            if (usuarioGrupo.equalsIgnoreCase(username)) {
                                try {
                                    long fileTime = (Long.parseLong(attrs.get("pwdLastSet").get().toString()) / 10000L) - + 11644473600000L;
                                    Date inputDate = new Date(fileTime);
                                    System.out.println("   name(GC): " + attrs.get("givenName").get() + " " + attrs.get("sn").get());
                                    System.out.println("   accountExpires(GC): " + attrs.get("accountExpires").get());
                                    System.out.println("   userAccountControl(GC): " + attrs.get("userAccountControl").get());                                    
                                    System.out.println("   pwdLastSet 1(GC): " + attrs.get("pwdLastSet").get());
//                                    System.out.println("   mail(GC): " + attrs.get("mail").get());
//                                    System.out.println("   accountExpires(GC): " + attrs.get("accountExpires").get());
//                                    System.out.println("   pwdLastSet 1(GC): " + attrs.get("pwdLastSet").get());
//                                    System.out.println("   pwdLastSet 2(GC): " + inputDate);
                                } catch (NullPointerException e){
                                    System.err.println("Problem listing attributes from Global Catalog--> " + e);
                                }                            
                                return attrs; 
                            }
                        }else{
                            System.out.println("User has no attributes");
                        }
                    }
                }else{
                    System.out.println("Search retrieve nothing");
                }                    
            } catch (NameNotFoundException e) { // The base context was not found.
               // Just clean up and exit.
               System.out.println("Mensaje Error 1--> "+e.getMessage());
            } catch (NamingException e) {
               System.out.println("Mensaje Error 2--> "+decodeMessage(e.getMessage()));
               throw new RuntimeException(e);
            } finally {
                if (answer != null) {
                    try {
                       answer.close();
                    } catch (Exception e) {
                       // Never mind this.
                    }
                }
                if (ctxGC != null) {
                    try {
                       ctxGC.close();
                    } catch (Exception e) {
                       // Never mind this.
                    } 
               }
            }                    
            return attrs;
        }
    }

    public static void main(String[] args) throws Exception{
       String homePath = System.getenv("USERNAME");
        System.out.println("USUARIO--> "+homePath.toUpperCase());        
        String username="HPAEZ";
        String password = "Diana_190713";
        LDAP ldap = new LDAP();
       
        Attributes att = ldap.authenticateUser(username, password, "dpperu.local", 
                Constantes.DOMINIO_AD, "DC=vipdp,DC=dpperu,DC=local");
        if (att == null){
              System.out.println("Sorry your use is invalid or password incorrect");
        }else{
              String s = att.get("givenName").toString();
              String nickName = att.get("sAMAccountName").toString();
              String nameCompleto = att.get("displayName").toString();
              System.out.println("GIVEN NAME= " + s);
              System.out.println("sAMAccountName= " + nickName);
              System.out.println("displayName= " + nameCompleto);
        }    
    }
   
    static public String decodeMessage(String message) {
        if (message.contains("data 525")) {
            message = "Usuario no encontrado.";
        } else if (message.contains("data 52e")) {
            message = "Credenciales inválidas.";
        } else if (message.contains("data 530")) {
            message = "No se permite iniciar sesión en este momento.";
        } else if (message.contains("data 531")) {
            message = "No se permite iniciar sesión desde esta estación de trabajo.";
        } else if (message.contains("data 532")) {
            message = "Contraseña caducada.";
        } else if (message.contains("data 533")) {
            message = "Cuenta deshabilitada.";
        } else if (message.contains("data 701")) {
            message = "Cuenta expiro.";
        } else if (message.contains("data 773")) {
            message = "El usuario debe restablecer la contraseña.";
        } else if (message.contains("data 775")) {
            message = "Cuenta de usuario bloqueado.";
        } else if (message.contains("error code 53 - 00002077")) {// error code 53 - 00002077 (8311L) - ERROR_DS_ILLEGAL_MOD_OPERATION: Illegal modify operation. Some aspect of the modification is not permitted.
            message = "Intento de modificación no autorizada. Algunos aspectos de la modificación no están permitidos.";
        } else if (message.contains("error code 53 - 0000052D")) {// error code 53 - 0000052D () - SvcErr: DSID-031A120C, problem 5003 (WILL_NOT_PERFORM), data 0
            message = "La contraseña no ha sido modificada. No cumple con los requisitos mínimos de seguridad o ya se ha utilizado.";
        } else if (message.contains("error code 50 - 00000005")) {// error code 50 - 00000005: SecErr: DSID-031A1190, problem 4003 (INSUFF_ACCESS_RIGHTS), data 0
            message = "El usuario no tiene suficientes derechos de acceso.";
        }
        return message;
    }       
}
/* Hermes 07/10/19 LDAP  */
