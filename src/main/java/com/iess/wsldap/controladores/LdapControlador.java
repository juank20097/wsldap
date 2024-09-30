/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.controladores;

/**
 * Clase que define los servicios de uso en los web services
 * 
 * 
 * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iess.wsldap.servicios.LdapServicio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para gestionar las operaciones relacionadas con los servicios LDAP.
 */

@Tag(name = "LDAP")
@RestController
@RequestMapping("/ldap")
public class LdapControlador {

	@Autowired
	private LdapServicio ldapServicio;

	/**
	 * Devuelve información relacionada a un usuario dentro de LDAP.
	 *
	 * @return Información sobre el usuario buscado.
	 */
	@Operation(description = "Este servicio nos devuelve la información relacionada al usuario buscado", parameters = {
			@Parameter(name = "usuario", example = "jane", required = true) }, responses = {
					@ApiResponse(responseCode = "200", description = "Muestra la información en formato JSON", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"dn\": \"{distinguishedName}\", \"cn\": \"Pruebas PrestadoresExternos\", \"facsimileTelephoneNumber\": \"Dato no disponible\", \"userPrincipalName\": \"Pruebas.pexternos@iess.gob.ec\", \"sAMAccountName\": \"pruebas.pexternos\" , \"physicalDeliveryOfficeName\": \"Dato no obtenido\" , \"department\": \"DNTI\" , \"tittle\": \"Dato no obtenido\" }"))),
					@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@GetMapping("/usuario/{usuario}")
	public Map<String, Object> buscarPorUsuario(@PathVariable("usuario") String usuario) {
		return ldapServicio.buscarPorUsuario(usuario);
	}

	/**
	 * Devuelve información relacionada a un correo dentro de LDAP.
	 *
	 * @return Información sobre el correo buscado.
	 */
	@Operation(description = "Este servicio nos devulve la informacion relacionada al correo buscado", parameters = {
			@Parameter(name = "correo", example = "jane.smith@example.com", required = true) }, responses = {
					@ApiResponse(responseCode = "200", description = "Muestra la informacion en formato JSON", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"dn\": \"{distinguishedName}\", \"cn\": \"Pruebas PrestadoresExternos\", \"facsimileTelephoneNumber\": \"Dato no disponible\", \"userPrincipalName\": \"Pruebas.pexternos@iess.gob.ec\", \"sAMAccountName\": \"pruebas.pexternos\" , \"physicalDeliveryOfficeName\": \"Dato no obtenido\" , \"department\": \"DNTI\" , \"tittle\": \"Dato no obtenido\" }"))),
					@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@GetMapping("/email/{correo}")
	public Map<String, Object> buscarPorCorreo(@PathVariable("correo") String correo) {
		return ldapServicio.buscarPorCorreo(correo);
	}

	/**
	 * Devuelve información relacionada a un nombre completo dentro de LDAP.
	 *
	 * @return Información sobre el nombre completo buscado.
	 */
	@Operation(description = "Este servicio devuelve la informacion realacionada al nombre proporcionado", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON el nombre completo", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"nombreCompleto\": \"Pruebas PrestadoresExternos\"}"))), responses = {
			@ApiResponse(responseCode = "200", description = "Muestra la informacion en formato JSON", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"dn\": \"{distinguishedName}\", \"cn\": \"Pruebas PrestadoresExternos\", \"facsimileTelephoneNumber\": \"Dato no disponible\", \"userPrincipalName\": \"Pruebas.pexternos@iess.gob.ec\", \"sAMAccountName\": \"pruebas.pexternos\" , \"physicalDeliveryOfficeName\": \"Dato no obtenido\" , \"department\": \"DNTI\" , \"tittle\": \"Dato no obtenido\" }"))),
			@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@PostMapping("/nombre/completo")
	public Map<String, Object> buscarPorNombreCompleto(@RequestBody Map<String, String> datosSolicitud) {
		String nombreCompleto = datosSolicitud.get("nombreCompleto");
		return ldapServicio.buscarPorNombreCompleto(nombreCompleto);
	}

	
	/**
	 * Cambia la contraseña ligada a un usuario dentro de LDAP.
	 *
	 * @param datosSolicitud Usuario y nueva contraseñá.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	@Operation(description = "Este servicio nos permite cambiar la contraseña ligada al usuario proporcionado", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON con el usuario y la nueva contraseña", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"usuario\": \"pruebas.pexternos\", \"nuevaContrasena\": \"Password123456\"}"))), responses = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"mensaje\": \"Contraseña ligada al usuario jane ha sido actualizada.\" }"))),
			@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@PostMapping("/usuario")
	public Map<String, String> cambiarContrasenaPorUsuario(@RequestBody Map<String, String> datosSolicitud) {
		String usuario = datosSolicitud.get("usuario");
		String nuevaContrasena = datosSolicitud.get("nuevaContrasena");
		return ldapServicio.cambiarContrasenaPorUsuario(usuario, nuevaContrasena);
	}

	/**
	 * Cambia la contraseña ligada a un correo dentro de LDAP.
	 *
	 * @param datosSolicitud Correo y nueva contraseñá.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	@Operation(description = "Este servicio nos permite cambiar la contraseña ligada al correo proporcionado", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON con el correo y la nueva contraseña", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"correo\": \"pruebas.pexternos@iess.gob.ec\", \"nuevaContrasena\": \"Password123456\"}"))), responses = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"mensaje\": \"Contraseña ligada al correo jane.smith@example.com ha sido actualizada.\" }"))),
			@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@PostMapping("/email")
	public Map<String, String> cambiarContrasenaPorCorreo(@RequestBody Map<String, String> datosSolicitud) {
		String correo = datosSolicitud.get("correo");
		String nuevaContrasena = datosSolicitud.get("nuevaContrasena");
		return ldapServicio.cambiarContrasenaPorCorreo(correo, nuevaContrasena);
	}
	
	/**
	 * Cambia la contraseña ligada a un nombre completo dentro de LDAP.
	 *
	 * @param datosSolicitud Nombre completo y nueva contraseñá.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	@Operation(description = "Este servicio nos permite cambiar la contraseña ligada al usario proporcionado", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON con el correo y la nueva contraseña", required = true, content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"nombreCompleto\": \"Pruebas PrestadoresExternos\", \"nuevaContrasena\": \"Password123456\"}"))), responses = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = "{ \"mensaje\": \"Contraseña ligada a Pruebas PrestadoresExternos ha sido actualizada.\" }"))),
			@ApiResponse(responseCode = "500", description = "Error de conexión", content = @Content()) })
	@PostMapping("/nombreCompleto")
	public Map<String, String> cambiarContrasenaNombreCompleto(@RequestBody Map<String, String> datosSolicitud) {
		String nombreCompleto = datosSolicitud.get("nombreCompleto");
		String nuevaContrasena = datosSolicitud.get("nuevaContrasena");
		return ldapServicio.cambiarContrasenaPorCorreo(nombreCompleto, nuevaContrasena);
	}
}
