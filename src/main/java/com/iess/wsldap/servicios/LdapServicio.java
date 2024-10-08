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
import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;

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
     * Busca un usuario por su displayName y retorna los atributos requeridos en formato JSON.
     *
     * @param displayName El nombre de cuenta SAM (usuario).
     * @return Un mapa con los atributos del usuario en formato JSON.
     * @throws IllegalArgumentException Si el displayName es nulo o vacío.
     * @throws UserNotFoundException Si no se encuentra el usuario.
     */
    public User obtenerUsuarioPorDisplayName(String displayName) {
        // Validar que displayName no sea nulo ni vacío
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("El displayName no puede ser nulo ni vacío.");
        }

        try {
            // Crear el cliente de Microsoft Graph
            GraphServiceClient<?> cliente = obtenerClienteGraph();

            // Realizar la búsqueda del usuario por sAMAccountName
            User usuario = cliente
                .users()
                .buildRequest()
                .filter("displayName eq '" + displayName + "'")
                .get()
                .getCurrentPage()
                .get(0);

            return usuario;
        } catch (IndexOutOfBoundsException e) {
            throw new UserNotFoundException("Usuario no encontrado: " + displayName);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el usuario: " + e.getMessage(), e);
        }
    }
    
    /**
     * Método para cambiar la contraseña del usuario.
     *
     * @param userId El ID del usuario (userPrincipalName o id en Azure AD).
     * @param nuevaContraseña La nueva contraseña para el usuario.
     */
    public void cambiarContraseña(String userId, String nuevaContraseña) {
        try {
            // Crear el PasswordProfile con la nueva contraseña
            PasswordProfile passwordProfile = new PasswordProfile();
            passwordProfile.password = nuevaContraseña;
            passwordProfile.forceChangePasswordNextSignIn = false;  // Opcional: Forzar cambio en el próximo inicio de sesión

            // Crear un objeto User con el nuevo PasswordProfile
            User user = new User();
            user.passwordProfile = passwordProfile;

            // Crear el cliente de Microsoft Graph
            GraphServiceClient<?> cliente = obtenerClienteGraph();

            // Actualizar el usuario con la nueva contraseña
            cliente
                .users(userId)
                .buildRequest()
                .patch(user);

            System.out.println("Contraseña cambiada con éxito para el usuario con ID: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar la contraseña: " + e.getMessage(), e);
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
