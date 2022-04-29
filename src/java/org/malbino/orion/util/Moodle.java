/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tincho
 */
public class Moodle {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Moodle.class);

    public static String[] getProperties() {
        String[] properties = new String[5];

        try {
            Properties prop = new Properties();
            prop.load(Moodle.class.getClassLoader().getResourceAsStream("moodle.properties"));

            properties[0] = prop.getProperty("webservice");
            properties[1] = prop.getProperty("login");
            properties[2] = prop.getProperty("username");
            properties[3] = prop.getProperty("password");
            properties[4] = prop.getProperty("serviceName");

            return properties;
        } catch (IOException ex) {
            log.error("Error en el archivo de propiedades de Moodle: " + ex.getMessage());
        }

        return properties;
    }
}
