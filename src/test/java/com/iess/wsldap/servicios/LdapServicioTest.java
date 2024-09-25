/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.servicios;

/**
 * Clase que define las pruebas de servicios con un LDAP embebido.   
 * 
 * 
  * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Clase que configura la conexión con un LDAP emebebido que ser usara para pruebas de los servicios. 
 */
@SpringBootTest
@ActiveProfiles("test")
public class LdapServicioTest {

	@Autowired
	private LdapServicio ldapServicio;

	/**
	 * Prueba el servicio buscarPorUsuario con un LDAP embebido.
	 *
	 */
	@Test
	public void testBuscarPorCorreo() {
		String correo = "jairo.martinez@example.com";
		System.out.println("Ejecutando testBuscarPorCorreo...");

		Map<String, Object> resultado = ldapServicio.buscarPorCorreo(correo);
		assertNotNull(resultado, "El resultado no debe ser nulo.");
		assertEquals(correo, resultado.get("mail"), "El correo no coincide.");
		assertEquals("Jairo Martinez", resultado.get("cn"), "El nombre no coincide.");
		assertEquals("Martinez", resultado.get("sn"), "El apellido no coincide.");
		assertEquals("jairo.martinez", resultado.get("uid"), "El UID no coincide.");

		System.out.println("Resultado de la búsqueda por correo:\n" + resultado);
		System.out.println("TEST buscarPorCorreo COMPLETADO exitosamente.");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Prueba el servicio buscarPorCorreo con un LDAP embebido.
	 *
	 */
	@Test
	public void testBuscarPorUsuario() {
		String usuario = "john.doe"; // UID del usuario en el LDIF
		System.out.println("Ejecutando testBuscarPorUsuario...");

		Map<String, Object> resultado = ldapServicio.buscarPorUsuario(usuario);
		assertNotNull(resultado, "El resultado no debe ser nulo.");
		assertEquals(usuario, resultado.get("uid"), "El UID no coincide.");
		assertEquals("john.doe@example.com", resultado.get("mail"), "El correo no coincide.");

		System.out.println("Resultado de la búsqueda por usuario:\n" + resultado);
		System.out.println("TEST buscarPorUsuario COMPLETADO exitosamente.");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Prueba el servicio cambiarContrasenaPorCorreo con un LDAP embebido.
	 *
	 */
	@Test
	public void testCambiarContrasenaPorCorreo() {
		String correo = "john.doe@example.com"; // Correo del usuario en el LDIF
		String nuevaContrasena = "nuevaContraseña";
		
		Map<String, String> prueba= new HashMap<>();
		prueba.put("Mensaje", "Contraseña ligada al correo " + correo + " ha sido actualizada.");
		
		System.out.println("Ejecutando testCambiarContrasenaPorCorreo...");
		Map<String, String> resultado = ldapServicio.cambiarContrasenaPorCorreo(correo, nuevaContrasena);
		assertEquals(resultado,prueba);

		System.out.println("Contraseña cambiada para el correo: " + correo);
		System.out.println("TEST cambiarContrasenaPorCorreo COMPLETADO exitosamente.");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Prueba el servicio cambiarContrasenaPorUsuario con un LDAP embebido.
	 *
	 */
	@Test
	public void testCambiarContrasenaPorUsuario() {
		String usuario = "john.doe"; // UID del usuario en el LDIF
		String nuevaContrasena = "nuevaContraseña";

		Map<String, String> prueba= new HashMap<>();
		prueba.put("Mensaje","Contraseña del usuario " + usuario + " ha sido actualizada.");
		
		System.out.println("Ejecutando testCambiarContrasenaPorUsuario...");
		Map<String, String> resultado = ldapServicio.cambiarContrasenaPorUsuario(usuario, nuevaContrasena);
		assertEquals(resultado,prueba);

		System.out.println("Contraseña cambiada para el usuario: " + usuario);
		System.out.println("TEST cambiarContrasenaPorUsuario COMPLETADO exitosamente.");
		System.out.println("-------------------------------------------------");
	}
}