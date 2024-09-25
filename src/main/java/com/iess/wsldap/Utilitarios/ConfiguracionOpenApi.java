/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.Utilitarios;

/**
 * Clase que define la configuración para Swagger.
 * 
 * 
  * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.ExternalDocumentation;

/**
 * Clase que configura los parametros de cabezera de la docuemntacion de Swagger - Open Api.
 */
@Configuration
public class ConfiguracionOpenApi {

	@Value("${nombre.api}")
	private String nombreApi;

	@Value("${descripcion.api}")
	private String descripcionApi;

	@Value("${equipo.desarrollo.url.api}")
	private String equipoDesarrolloUrl;

	@Value("${repositorio.url.api}")
	private String repositorioUrl;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title(this.nombreApi).version("1.0").description(descripcionApi)
						.contact(new Contact().name("Equipo de desarrollo").url(equipoDesarrolloUrl)))
				.externalDocs(new ExternalDocumentation().description("Repositorio GitHub").url(repositorioUrl));
	}

}
