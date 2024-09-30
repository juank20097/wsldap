/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.servicios;

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
	 * Obtiene la informacion relacionada a un usuario dentro de LDAP.
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
						datosUsuario.put("cn", obtenerAtributo(context, "cn")); // Nombres completos
						datosUsuario.put("facsimileTelephoneNumber", obtenerAtributo(context, "facsimileTelephoneNumber")); // Numero de cedula
						datosUsuario.put("userPrincipalName", obtenerAtributo(context, "userPrincipalName")); // Correo institucional
						datosUsuario.put("sAMAccountName", obtenerAtributo(context, "sAMAccountName")); // Usuario (correo @iess.gob.ec)
						datosUsuario.put("physicalDeliveryOfficeName", obtenerAtributo(context, "physicalDeliveryOfficeName")); // Ubicacion fisica
						datosUsuario.put("department", obtenerAtributo(context, "department")); // Departamento
						datosUsuario.put("title", obtenerAtributo(context, "title")); // Cargo

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
	 * Obtiene la informacion relacionada a un correo dentro de LDAP.
	 *
	 * @param correo Correo buscado.
	 * @return JSON con la información del correo buscado.
	 */
	public Map<String, Object> buscarPorCorreo(String correo) {
		try {
			return ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("userPrincipalName").is(correo),
					(ContextMapper<Map<String, Object>>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;

						Map<String, Object> datosUsuario = new HashMap<>();
						datosUsuario.put("dn", context.getDn().toString());
						datosUsuario.put("cn", obtenerAtributo(context, "cn")); // Nombres completos
						datosUsuario.put("facsimileTelephoneNumber", obtenerAtributo(context, "facsimileTelephoneNumber")); // Numero de cedula
						datosUsuario.put("userPrincipalName", obtenerAtributo(context, "userPrincipalName")); // Correo institucional
						datosUsuario.put("sAMAccountName", obtenerAtributo(context, "sAMAccountName")); // Usuario (correo @iess.gob.ec)
						datosUsuario.put("physicalDeliveryOfficeName", obtenerAtributo(context, "physicalDeliveryOfficeName")); // Ubicacion fisica
						datosUsuario.put("department", obtenerAtributo(context, "department")); // Departamento
						datosUsuario.put("title", obtenerAtributo(context, "title")); // Cargo

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
	 * Obtiene la informacion relacionada a un correo dentro de LDAP.
	 *
	 * @param correo Correo buscado.
	 * @return JSON con la información del correo buscado.
	 */
	public Map<String, Object> buscarPorNombreCompleto(String nombreCompleto) {
		try {
			return ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("cn").is(nombreCompleto),
					(ContextMapper<Map<String, Object>>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;

						Map<String, Object> datosUsuario = new HashMap<>();
						datosUsuario.put("dn", context.getDn().toString());
						datosUsuario.put("cn", obtenerAtributo(context, "cn")); // Nombres completos
						datosUsuario.put("facsimileTelephoneNumber", obtenerAtributo(context, "facsimileTelephoneNumber")); // Numero de cedula
						datosUsuario.put("userPrincipalName", obtenerAtributo(context, "userPrincipalName")); // Correo institucional
						datosUsuario.put("sAMAccountName", obtenerAtributo(context, "sAMAccountName")); // Usuario (correo @iess.gob.ec)
						datosUsuario.put("physicalDeliveryOfficeName", obtenerAtributo(context, "physicalDeliveryOfficeName")); // Ubicacion fisica
						datosUsuario.put("department", obtenerAtributo(context, "department")); // Departamento
						datosUsuario.put("title", obtenerAtributo(context, "title")); // Cargo

						return datosUsuario;
					});

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("mensaje", "El usuario " + nombreCompleto + " no existe.");
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
			String DnUsuario = ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("sAMAccountName").is(usuario),
					(ContextMapper<String>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;
						return context.getDn().toString();
					});
			ldapPlantilla.modifyAttributes(DnUsuario,
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("unicodePwd", nuevaContrasena)) });

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
			String DnUsuario = ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("userPrincipalName").is(correo),
					(ContextMapper<String>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;
						return context.getDn().toString();
					});

			ldapPlantilla.modifyAttributes(DnUsuario,
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("unicodePwd", nuevaContrasena)) });

			respuesta.put("mensaje", "Contraseña ligada al correo " + correo + " ha sido actualizada.");

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
	public Map<String, String> cambiarContrasenaPorNombreCompleto(String nombreCompleto, String nuevaContrasena) {
		Map<String, String> respuesta = new HashMap<>();
		try {
			String DnUsuario = ldapPlantilla.searchForObject(LdapQueryBuilder.query().where("cn").is(nombreCompleto),
					(ContextMapper<String>) contexto -> {
						DirContextAdapter context = (DirContextAdapter) contexto;
						return context.getDn().toString();
					});

			ldapPlantilla.modifyAttributes(DnUsuario,
					new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
							new BasicAttribute("unicodePwd", nuevaContrasena)) });

			respuesta.put("mensaje", "Contraseña ligada a " + nombreCompleto + " ha sido actualizada.");

			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.put("mensaje", "Error al intentar actualizar contraseña:" + e.getMessage());
			return respuesta;
		}
	}
	
	/**
	 * Obtiene los datos de manera segura
	 *
	 * @param context  Ubicación del atributo buscado.
	 * @param atributo Atributo a obtener dato.
	 * @return Información del atributo buscado.
	 */
    public String obtenerAtributo(DirContextAdapter context, String atributo) {
        String valor = context.getStringAttribute(atributo);
        return (valor != null && !valor.isEmpty()) ? valor : "atributo no encontrado";
    }

}
