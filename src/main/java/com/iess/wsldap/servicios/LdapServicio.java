/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Clase que define los servicios REST para LDAP
 * 
 * 
 * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */

import java.util.HashMap;

import java.util.Map;

import javax.naming.directory.BasicAttribute;

import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.Hidden;



/**
 * Servicio para los usuarios dentro del LDAP.
 */
@Hidden
@Service
public class LdapServicio {

	@Autowired
	private LdapTemplate ldapPlantilla;

	/**
	 * Obtiene la informacion relaacionada a un usuario dentro de LDAP.
	 *
	 * @param usuario Usuario buscado.
	 * @return JSON con la información del usuario buscado.
	 */
	public Map<String, Object> buscarPorUsuario(String usuario) {
		try {
			return ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("uid").is(usuario),
					(ContextMapper<Map<String, Object>>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;

						Map<String, Object> datosUsuario = new HashMap<>();
						datosUsuario.put("dn", context.getDn().toString());
						datosUsuario.put("cn", obtenerAtributo(context, "cn"));
						datosUsuario.put("sn", obtenerAtributo(context, "sn"));
						datosUsuario.put("uid", obtenerAtributo(context, "uid"));
						datosUsuario.put("mail", obtenerAtributo(context, "mail"));

						return datosUsuario;
					});

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("mensaje", "El usuario " + usuario + " no existe.");
			return error;
		}
	}

	/**
	 * Obtiene la informacion relaacionada a un correo dentro de LDAP.
	 *
	 * @param correo Correo buscado.
	 * @return JSON con la información del correo buscado.
	 */
	public Map<String, Object> buscarPorCorreo(String correo) {
		try {
			return ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("mail").is(correo),
					(ContextMapper<Map<String, Object>>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;

						Map<String, Object> datosUsuario = new HashMap<>();
						datosUsuario.put("dn", context.getDn().toString());
						datosUsuario.put("cn", obtenerAtributo(context, "cn"));
						datosUsuario.put("sn", obtenerAtributo(context, "sn"));
						datosUsuario.put("uid", obtenerAtributo(context, "uid"));
						datosUsuario.put("mail", obtenerAtributo(context, "mail"));

						return datosUsuario;
					});

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("mensaje", "El usuario con el correo " + correo + " no existe.");
			return error;
		}
	}

	/**
	 * Actualiza la contraseña ligada a un usuario dentro de LDAP.
	 *
	 * @param usuario         Usuario al que se le cambiara la contraseña.
	 * @param nuevaContraseña Nueva contraseña para actualizar.
	 * @return JSON con el resultado de la operación.
	 */
	public Map<String, String> cambiarContrasenaPorUsuario(String usuario, String nuevaContrasena) {
		Map<String, String> respuesta = new HashMap<>();
		try {
			String DnUsuario = ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("uid").is(usuario),
					(ContextMapper<String>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;
						return context.getDn().toString();
					});
			ldapPlantilla.modifyAttributes(DnUsuario,
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("userPassword", nuevaContrasena)) });

			respuesta.put("mensaje", "Contraseña del usuario " + usuario + " ha sido actualizada.");
			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.put("mensaje", "Error al intentar actualizar contraseña:" + e.getMessage());
			return respuesta;
		}
	}

	/**
	 * Actualiza la contraseña ligada a un correo dentro de LDAP.
	 *
	 * @param correo          Correo al que se le cambiara la contraseña.
	 * @param nuevaContraseña Nueva contraseña para actualizar.
	 * @return JSON con el resultado de la operación.
	 */
	public Map<String, String> cambiarContrasenaPorCorreo(String correo, String nuevaContrasena) {
		Map<String, String> respuesta = new HashMap<>();
		try {
			String DnUsuario = ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("mail").is(correo),
					(ContextMapper<String>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;
						return context.getDn().toString();
					});

			ldapPlantilla.modifyAttributes(DnUsuario,
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("userPassword", nuevaContrasena)) });

			respuesta.put("mensaje", "Contraseña ligada al correo " + correo + " ha sido actualizada.");

			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.put("mensaje", "Error al intentar actualizar contraseña:" + e.getMessage());
			return respuesta;
		}
	}

	/**
	 * Obtiene los datos de manera segura de un DN dentro de LDAP.
	 *
	 * @param context  Ubicación del DN buscado.
	 * @param atributo Atributo a obtener dato.
	 * @return Información del atributo buscado.
	 */
	private String obtenerAtributo(DirContextAdapter context, String atributo) {
		String valorDelAtributo = context.getStringAttribute(atributo);
		return valorDelAtributo != null ? valorDelAtributo : "Dato no disponible";
	}

}
