/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

import java.util.regex.Pattern;

/**
 *
 * @author Tincho
 */
public abstract class NumberToLetterConverter {

    private static final String[] UNIDADES = {"", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE "};
    private static final String[] DECENAS = {"DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS ",
        "DIECISIETE ", "DIECIOCHO ", "DIECINUEVE ", "VEINTE ", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ",
        "OCHENTA ", "NOVENTA "};
    private static final String[] CENTENAS = {"", "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
        "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};

    public static String convertNumberToLetter(int numero) {
        String literal;

        String snumero = String.valueOf(numero);

        if (Pattern.matches("\\d{1,9}", snumero)) {
            if (Integer.parseInt(snumero) == 0) {
                literal = "CERO ";
            } else if (Integer.parseInt(snumero) > 999999) {
                literal = getMillones(snumero);
            } else if (Integer.parseInt(snumero) > 999) {
                literal = getMiles(snumero);
            } else if (Integer.parseInt(snumero) > 99) {
                literal = getCentenas(snumero);
            } else if (Integer.parseInt(snumero) > 9) {
                literal = getDecenas(snumero);
            } else {
                literal = getUnidades(snumero);
            }

            return literal;
        } else {
            return null;
        }
    }
    
    public static String convertNumberToLetter(Double numero) {
        String literal;
        String parte_decimal;

        String snumero = String.valueOf(numero);
        snumero = snumero.replace(".", ",");
        if (!snumero.contains(",")) {
            snumero = snumero + ",00";
        }
        if (Pattern.matches("\\d{1,9},\\d{1,2}", snumero)) {
            String Num[] = snumero.split(",");
            parte_decimal = Num[1] + "/100 BOLIVIANOS";
            if (Integer.parseInt(Num[0]) == 0) {
                literal = "CERO ";
            } else if (Integer.parseInt(Num[0]) > 999999) {
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {
                literal = getDecenas(Num[0]);
            } else {
                literal = getUnidades(Num[0]);
            }

            return (literal + parte_decimal);
        } else {
            return null;
        }
    }

    private static String getUnidades(String numero) {
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private static String getDecenas(String num) {
        int n = Integer.parseInt(num);
        if (n < 10) {
            return getUnidades(num);
        } else if (n > 19) {
            String u = getUnidades(num);
            if (u.equals("")) {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "Y " + u;
            }
        } else {
            return DECENAS[n - 10];
        }
    }

    private static String getCentenas(String num) {
        if (Integer.parseInt(num) > 99) {
            if (Integer.parseInt(num) == 100) {
                return " CIEN ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private static String getMiles(String numero) {
        String c = numero.substring(numero.length() - 3);
        String m = numero.substring(0, numero.length() - 3);
        String n;
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "MIL " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private static String getMillones(String numero) {

        String miles = numero.substring(numero.length() - 6);
        String millon = numero.substring(0, numero.length() - 6);
        String n;
        if (millon.length() > 1) {
            n = getCentenas(millon) + "MILONES ";
        } else {
            n = getUnidades(millon) + "MILLON ";
        }
        return n + getMiles(miles);
    }
}
