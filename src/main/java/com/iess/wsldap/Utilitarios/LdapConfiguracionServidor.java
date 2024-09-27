/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.Utilitarios;

import javax.naming.ldap.LdapContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;

/**
 * Clase que configura la conexión con un LDAP externo.
 * 
 * @author jestevez
 * @version 1.0.0
 */
@Configuration
public class LdapConfiguracionServidor {

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    @Value("${spring.ldap.base}")
    private String ldapBase;

    @Value("${spring.ldap.username}")
    private String ldapUserDn;

    @Value("${spring.ldap.password}")
    private String ldapPassword;

    @Bean
    public LdapContextSource contextoDelServidor() {
        LdapContextSource contextoServidor = new LdapContextSource();
        contextoServidor.setUrl(ldapUrl);
        contextoServidor.setBase(ldapBase);
        contextoServidor.setUserDn(ldapUserDn);
        contextoServidor.setPassword(ldapPassword);
        contextoServidor.afterPropertiesSet();

        // Verificamos la conexión al LDAP antes de devolver el contexto
        verificarConexion(contextoServidor);
        return contextoServidor;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextoDelServidor());
    }

    /**
     * Método para verificar la conexión al servidor LDAP.
     *
     * @param contextoServidor El contexto del servidor LDAP a verificar.
     */
    private void verificarConexion(LdapContextSource contextoServidor) {
        LdapContext context = null;
        try {
            context = (LdapContext) contextoServidor.getContext(ldapUserDn, ldapPassword);
            System.out.println("Conexión exitosa al servidor LDAP.");
        } catch (Exception e) {
            System.err.println("Error al conectar con el servidor LDAP: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar con el servidor LDAP", e);
        } finally {
            LdapUtils.closeContext(context);
        }
    }
}
