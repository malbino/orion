/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.pfsense.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.malbino.orion.entities.Usuario;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbino
 */
public class CopiarUsuario implements Runnable {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CopiarUsuario.class);

    String webservice;
    String user;
    String password;
    Usuario usuario;

    public CopiarUsuario(String webservice, String user, String password, Usuario usuario) {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (String hostname, SSLSession session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (GeneralSecurityException e) {
        }

        this.webservice = webservice;
        this.user = user;
        this.password = password;
        this.usuario = usuario;
    }

    public Boolean createUser() {
        Boolean b = Boolean.FALSE;

        try {
            URL url = new URL(webservice);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String auth = user + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            conn.setRequestProperty("Authorization", authHeaderValue);

            JSONObject usuarioJSON = new JSONObject();
            usuarioJSON.put("authorizedkeys", "");
            JSONArray privilegios = new JSONArray();
            privilegios.put("user-services-captiveportal-login");
            usuarioJSON.put("priv", privilegios);
            usuarioJSON.put("descr", usuario.toString());
            usuarioJSON.put("disabled", false);
            usuarioJSON.put("password", usuario.getContrasenaSinEncriptar());
            usuarioJSON.put("username", usuario.getUsuario());

            OutputStream os = conn.getOutputStream();
            byte[] input = usuarioJSON.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            conn.disconnect();

            JSONObject responseJSON = new JSONObject(response.toString());
            log.info(responseJSON.toString());

            b = Boolean.TRUE;
        } catch (IOException | JSONException e) {
            log.error(e.toString());
        }

        return b;
    }

    public Boolean updateUser() {
        Boolean b = Boolean.FALSE;

        try {
            URL url = new URL(webservice);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String auth = user + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            conn.setRequestProperty("Authorization", authHeaderValue);

            JSONObject usuarioJSON = new JSONObject();
            usuarioJSON.put("authorizedkeys", "");
            JSONArray privilegios = new JSONArray();
            privilegios.put("user-services-captiveportal-login");
            usuarioJSON.put("priv", privilegios);
            usuarioJSON.put("descr", usuario.toString());
            usuarioJSON.put("disabled", false);
            usuarioJSON.put("password", usuario.getContrasenaSinEncriptar());
            usuarioJSON.put("username", usuario.getUsuario());

            OutputStream os = conn.getOutputStream();
            byte[] input = usuarioJSON.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            conn.disconnect();

            JSONObject responseJSON = new JSONObject(response.toString());
            log.info(responseJSON.toString());

            b = Boolean.TRUE;
        } catch (IOException | JSONException e) {
            log.error(e.toString());
        }

        return b;
    }

    public void copiarUsuario() {
        if (!updateUser()) {
            createUser();
        }
    }

    @Override
    public void run() {
        copiarUsuario();
    }

}
