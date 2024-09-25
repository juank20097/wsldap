/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.Utilitarios;

/**
 * Clase que define la configuración de conexión con un LDAP.
 * 
 * 
  * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * Clase que configura la conexión con un LDAP externo.
 */
public class LdapConfiguracionServidor {
	@Bean
	public LdapContextSource contextoDelServidor() {
		LdapContextSource contextoServidor = new LdapContextSource();
		contextoServidor.setUrl("${spring.ldap.urls}");
		contextoServidor.setBase("${spring.ldap.base}");
		contextoServidor.setUserDn("${spring.ldap.username}");
		contextoServidor.setPassword("${spring.ldap.password}");
		return contextoServidor;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextoDelServidor());
	}
}
