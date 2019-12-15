/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

/**
 *
 * @author malbino
 */
public class Generador {

    public static final String NUMEROS = "0123456789";
    public static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";

    public static final int TAM_PWD = 8;
    public static final int TAM_COD = 5;
    public static final int TAM_PIN = 4;

    public static String generarContrasena() {
        String pwd = "";

        String key = NUMEROS + MAYUSCULAS;
        for (int i = 0; i < TAM_PWD; i++) {
            pwd += key.charAt((int) (Math.random() * key.length()));
        }

        return pwd;
    }

    public static String generarCodigo() {
        String cod = "";

        String key = NUMEROS + MAYUSCULAS;
        for (int i = 0; i < TAM_COD; i++) {
            cod += key.charAt((int) (Math.random() * key.length()));
        }

        return cod;
    }

    public static String generarPIN() {
        String cod = "";

        String key = NUMEROS;
        for (int i = 0; i < TAM_PIN; i++) {
            cod += key.charAt((int) (Math.random() * key.length()));
        }

        return cod;
    }
}
