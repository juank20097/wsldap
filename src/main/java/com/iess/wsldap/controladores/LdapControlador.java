/**
 * Copyrigth 2024 INSTITUTO ECUATORIANO DE SEGURIDAD SOCIAL - ECUADOR
 * Todos los derechos reservados
 */
package com.iess.wsldap.controladores;

import com.iess.wsldap.servicios.LdapServicio;
import com.iess.wsldap.servicios.LdapServicio.UserNotFoundException;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar las operaciones relacionadas con los servicios LDAP.
 * 
 * @author  jestevez
 * @version $Revision: 1.0.0 $ 
 *          <p>
 *          [$Author: jestevez $, Date: 25 sep 2024 $]
 *          </p>
 */
@Tag(name = "Azure AD")
@RestController
@RequestMapping("/ldap")
public class LdapControlador {

    // Inyección de dependencias del servicio LDAP
    @Autowired
    private LdapServicio ldapServicio;

    /**
     * Endpoint para obtener información de un usuario desde Azure AD utilizando el sAMAccountName.
     *
     * @param sAMAccountName El nombre de cuenta SAM del usuario.
     * @return La información del usuario en formato JSON o un mensaje de error.
     */
    @Operation(summary = "Obtener información de usuario por sAMAccountName")
    @GetMapping("/usuario/{sAMAccountName}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable("sAMAccountName") String sAMAccountName) {
        try {
            // Obtener la información del usuario desde el servicio
            Map<String, String> usuario = ldapServicio.obtenerUsuarioPorSAMAccountName(sAMAccountName);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Manejar parámetros inválidos
            return new ResponseEntity<>("El sAMAccountName no puede ser nulo ni vacío.", HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            // Manejar el caso en que el usuario no fue encontrado
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Manejar errores generales
            return new ResponseEntity<>("Ocurrió un error al obtener el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint de prueba para verificar el estado del servicio LDAP.
     *
     * @return Un mensaje de confirmación si el servicio está operativo.
     */
    @Operation(summary = "Probar la conexión con Azure AD")
    @GetMapping("/ping")
    public ResponseEntity<String> probarServicio() {
        try {
            // Intentar obtener una lista de usuarios para verificar la conexión
            GraphServiceClient<?> cliente = ldapServicio.obtenerClienteGraph();

            // Obtener una lista de usuarios (limitamos a 1 para eficiencia)
            UserCollectionPage usuarios = cliente.users()
                .buildRequest()
                .top(1) // Limitar a 1 usuario
                .get();

            // Verificar si se obtuvo algún usuario
            if (!usuarios.getCurrentPage().isEmpty()) {
                User usuario = usuarios.getCurrentPage().get(0);
                return new ResponseEntity<>("Conexión exitosa con Azure AD. Usuario encontrado: " + usuario.displayName, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Conexión exitosa con Azure AD, pero no se encontraron usuarios.", HttpStatus.OK);
            }
        } catch (Exception e) {
            // Si ocurre algún error, la conexión no es exitosa
            return new ResponseEntity<>("Error al conectar con Azure AD: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
