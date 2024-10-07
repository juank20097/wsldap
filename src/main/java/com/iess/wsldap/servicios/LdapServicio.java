/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.servicios;

/**
 * Clase que define los servicios para Azure Active Directory.
 * 
 * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]a
 *          </p>
 */

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para la autenticación y obtención de información de usuarios desde Azure AD.
 */
@Hidden
@Service
public class LdapServicio {

    // Variables cargadas desde application.properties
    @Value("${azure.tenant-id}")
    private String tenantId;

    @Value("${azure.cliente-id}")
    private String clienteId;

    @Value("${azure.cliente-secreto}")
    private String clienteSecreto;

    /**
     * Crea y retorna el cliente de Microsoft Graph autenticado.
     *
     * @return El cliente GraphServiceClient.
     */
    public GraphServiceClient<?> obtenerClienteGraph() {
        // Crear las credenciales del cliente utilizando los valores de configuración
        ClientSecretCredential credencialClienteSecreto = new ClientSecretCredentialBuilder()
            .clientId(clienteId)
            .clientSecret(clienteSecreto)
            .tenantId(tenantId)
            .build();

        // Crear el proveedor de autenticación usando las credenciales del cliente
        TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(
            Collections.singletonList("https://graph.microsoft.com/.default"), 
            credencialClienteSecreto
        );

        // Crear el cliente Graph con el proveedor de autenticación
        return GraphServiceClient
            .builder()
            .authenticationProvider(authProvider)
            .buildClient();
    }

    /**
     * Busca un usuario por su sAMAccountName y retorna los atributos requeridos en formato JSON.
     *
     * @param sAMAccountName El nombre de cuenta SAM (usuario).
     * @return Un mapa con los atributos del usuario en formato JSON.
     * @throws IllegalArgumentException Si el sAMAccountName es nulo o vacío.
     * @throws UserNotFoundException Si no se encuentra el usuario.
     */
    public Map<String, String> obtenerUsuarioPorSAMAccountName(String sAMAccountName) {
        // Validar que sAMAccountName no sea nulo ni vacío
        if (sAMAccountName == null || sAMAccountName.trim().isEmpty()) {
            throw new IllegalArgumentException("El sAMAccountName no puede ser nulo ni vacío.");
        }

        try {
            // Crear el cliente de Microsoft Graph
            GraphServiceClient<?> cliente = obtenerClienteGraph();

            // Realizar la búsqueda del usuario por sAMAccountName
            User usuario = cliente
                .users()
                .buildRequest()
                .filter("onPremisesSamAccountName eq '" + sAMAccountName + "'")
                .select("displayName,onPremisesSamAccountName,userPrincipalName,department,jobTitle")
                .get()
                .getCurrentPage()
                .get(0);

            // Crear un mapa para los atributos del usuario
            Map<String, String> atributosUsuario = new HashMap<>();
            atributosUsuario.put("NombresCompletos", usuario.displayName);
            atributosUsuario.put("NumeroCedula", "N/A"); // No disponible en Microsoft Graph
            atributosUsuario.put("CorreoInstitucional", usuario.userPrincipalName);
            atributosUsuario.put("Usuario", usuario.onPremisesSamAccountName); // sAMAccountName
            atributosUsuario.put("UbicacionFisica", "N/A"); // No disponible directamente
            atributosUsuario.put("Departamento", usuario.department);
            atributosUsuario.put("Cargo", usuario.jobTitle);

            return atributosUsuario;
        } catch (IndexOutOfBoundsException e) {
            throw new UserNotFoundException("Usuario no encontrado: " + sAMAccountName);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el usuario: " + e.getMessage(), e);
        }
    }
    
    public class UserNotFoundException extends RuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String message) {
            super(message);
        }
    }

}
