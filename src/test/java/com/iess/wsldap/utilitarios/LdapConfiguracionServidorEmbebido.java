/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.utilitarios;

/**
 * Clase de prueba que define la conexión con un LDAP embebido.   
 * 
 * 
  * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * Clase que configura la conexión con un LDAP emebebido que ser usara para pruebas de los servicios. 
 */
@Configuration
@ActiveProfiles("test")
public class LdapConfiguracionServidorEmbebido {

	@Bean
	public InMemoryDirectoryServer servidorLdap() throws Exception {

		InMemoryDirectoryServerConfig configuracionServidor = new InMemoryDirectoryServerConfig("dc=example,dc=com");

		InMemoryListenerConfig configuracionEscucha = new InMemoryListenerConfig("default",
				InetAddress.getByName("localhost"), 65065, null, null, null);
		configuracionServidor.setListenerConfigs(configuracionEscucha);

		InMemoryDirectoryServer servidor = new InMemoryDirectoryServer(configuracionServidor);

		try (InputStream entradaLdif = getClass().getClassLoader().getResourceAsStream("sample-data.ldif")) {
			if (entradaLdif == null) {
				throw new IllegalArgumentException("El archivo sample-data.ldif no fue encontrado en el classpath");
			}

			File archivoTemporalLdif = File.createTempFile("sample-data", ".ldif");
			archivoTemporalLdif.deleteOnExit();

			try (FileOutputStream flujoSalida = new FileOutputStream(archivoTemporalLdif)) {
				byte[] buffer = new byte[1024];
				int bytesLeidos;
				while ((bytesLeidos = entradaLdif.read(buffer)) != -1) {
					flujoSalida.write(buffer, 0, bytesLeidos);
				}
			}

			servidor.importFromLDIF(true, archivoTemporalLdif.getAbsolutePath());
		}

		servidor.startListening();
		int puertoAsignado = servidor.getListenPort("default");
		System.out.println("Servidor LDAP escuchando en el puerto: " + puertoAsignado);
		return servidor;
	}

	@Bean
	public LdapContextSource fuenteContextoLdap() {
		LdapContextSource fuenteContexto = new LdapContextSource();
		fuenteContexto.setUrl("ldap://localhost:65065");
		return fuenteContexto;
	}

	@Bean
	public LdapTemplate plantillaLdap() {
		return new LdapTemplate(fuenteContextoLdap());
	}
}