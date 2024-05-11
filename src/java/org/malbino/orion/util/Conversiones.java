/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.malbino.orion.util;

/**
 *
 * @author malbino
 */
public class Conversiones {

    public static float mmToPtos(float mm) {
        float f = 0;

        f = (mm / 25.4f) * 72f;

        return f;
    }
}
