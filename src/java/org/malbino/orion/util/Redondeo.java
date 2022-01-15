/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Tincho
 */
public class Redondeo {

    public static Double redondear_HALFEVEN(double numero, int numero_decimales) {
        BigDecimal bigDecimal = new BigDecimal(numero);
        BigDecimal bigDecimalRedondeado = bigDecimal.setScale(numero_decimales, RoundingMode.HALF_EVEN);

        return bigDecimalRedondeado.doubleValue();
    }

    public static Double redondear_HALFUP(double numero, int numero_decimales) {
        BigDecimal bigDecimal = new BigDecimal(numero);
        BigDecimal bigDecimalRedondeado = bigDecimal.setScale(numero_decimales, RoundingMode.HALF_UP);

        return bigDecimalRedondeado.doubleValue();
    }

    public static Double redondear_DOWN(double numero, int numero_decimales) {
        BigDecimal bigDecimal = new BigDecimal(numero);
        BigDecimal bigDecimalRedondeado = bigDecimal.setScale(numero_decimales, RoundingMode.DOWN);

        return bigDecimalRedondeado.doubleValue();
    }

    public static Double redondear_UP(double numero, int numero_decimales) {
        BigDecimal bigDecimal = new BigDecimal(numero);
        BigDecimal bigDecimalRedondeado = bigDecimal.setScale(numero_decimales, RoundingMode.UP);

        return bigDecimalRedondeado.doubleValue();
    }

    public static String formatear_0p00(double numero) {
        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(numero);
    }

    public static String formatear_0000(int numero) {
        DecimalFormat df = new DecimalFormat("0000");

        return df.format(numero);
    }

    public static String mostrar_0p00(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String formatear_csm(double numero) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("###,##0.00", dfs);

        return df.format(numero);
    }

    public static String formatear_ssm(double numero) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", dfs);

        return df.format(numero);
    }
}
