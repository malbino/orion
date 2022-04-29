/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author malbino
 */
public class Encriptador {

    public static String encriptar(String cadena) {
        return DigestUtils.sha512Hex(cadena);
    }

    public static boolean comparar(String cadena, String sha512) {
        boolean b = false;

        if ((DigestUtils.sha512Hex(cadena)).compareTo(sha512) == 0) {
            b = true;
        }

        return b;
    }

}
