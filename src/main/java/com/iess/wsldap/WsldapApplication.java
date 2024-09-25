/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap;

/**
 * Clase que define las entidad a generar en la BDD
 * 
 * 
 * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * La clase principal que inicia los servicios para LDAP.
 * Esta clase utiliza la anotación @SpringBootApplication para habilitar la configuración automática
 * y el inicio de Spring Boot.
 */

@SpringBootApplication
public class WsldapApplication {

	 /**
     * El método principal que inicia la aplicación Spring Boot.
     * Presenta en los logs la URL de la documentación en Swagger.
     *
     * @param args Los argumentos de la línea de comandos (si los hay).
     */
	public static void main(String[] args) {
		SpringApplication.run(WsldapApplication.class, args);
		System.out.println("--------- Documentación SWAGGER -----------");
		System.out.println("http://[ip_servidor]:8080/swagger-ui/index.html");
		System.out.println("-------------------------------------------");
	}

}
