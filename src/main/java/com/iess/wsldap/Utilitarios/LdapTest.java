package com.iess.wsldap.Utilitarios;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class LdapTest {
    public static void main(String[] args) {
    	 try {
             // Cargar el KeyStore desde el archivo cacerts
             KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
             try (InputStream keyStoreStream = new FileInputStream("C:\\Program Files\\Java\\jdk-17\\lib\\security\\cacerts")) {
                 keyStore.load(keyStoreStream, "changeit".toCharArray());
             }

             // Inicializar el TrustManagerFactory con el keystore cargado
             TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
             tmf.init(keyStore);

             // Inicializar SSLContext con el TrustManager
             SSLContext sc = SSLContext.getInstance("TLS");
             sc.init(null, tmf.getTrustManagers(), null);

             // Obtener el SSLSocketFactory del SSLContext inicializado
             SSLSocketFactory factory = sc.getSocketFactory();

             // Crear un socket SSL
             SSLSocket socket = (SSLSocket) factory.createSocket("192.168.11.50", 636);

             // Realizar el handshake SSL
             socket.startHandshake();

             System.out.println("Conexión LDAP exitosa");
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}
