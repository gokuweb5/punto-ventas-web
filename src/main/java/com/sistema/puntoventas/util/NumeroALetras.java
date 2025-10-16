package com.sistema.puntoventas.util;

import java.math.BigDecimal;

public class NumeroALetras {

    private static final String[] UNIDADES = {
        "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"
    };

    private static final String[] DECENAS = {
        "", "diez", "veinte", "treinta", "cuarenta", "cincuenta", 
        "sesenta", "setenta", "ochenta", "noventa"
    };

    private static final String[] ESPECIALES = {
        "diez", "once", "doce", "trece", "catorce", "quince", 
        "dieciséis", "diecisiete", "dieciocho", "diecinueve"
    };

    private static final String[] CENTENAS = {
        "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos",
        "seiscientos", "setecientos", "ochocientos", "novecientos"
    };

    /**
     * Convierte un número BigDecimal a su representación en letras
     * Formato: "treinta y dos 25/100 dólares"
     */
    public static String convertir(BigDecimal numero) {
        if (numero == null) {
            return "cero 00/100 dólares";
        }

        // Separar parte entera y decimal
        int parteEntera = numero.intValue();
        int parteDecimal = numero.remainder(BigDecimal.ONE)
                .multiply(new BigDecimal(100))
                .intValue();

        // Convertir parte entera a letras
        String letras = convertirEntero(parteEntera);
        
        // Formatear centavos con dos dígitos
        String centavos = String.format("%02d", parteDecimal);
        
        // Retornar formato completo
        return letras + " " + centavos + "/100 dólares";
    }

    /**
     * Convierte la parte entera del número a letras
     */
    private static String convertirEntero(int numero) {
        if (numero == 0) {
            return "cero";
        }

        if (numero < 0) {
            return "menos " + convertirEntero(-numero);
        }

        String resultado = "";

        // Millones
        if (numero >= 1000000) {
            int millones = numero / 1000000;
            if (millones == 1) {
                resultado += "un millón ";
            } else {
                resultado += convertirEntero(millones) + " millones ";
            }
            numero %= 1000000;
        }

        // Miles
        if (numero >= 1000) {
            int miles = numero / 1000;
            if (miles == 1) {
                resultado += "mil ";
            } else {
                resultado += convertirEntero(miles) + " mil ";
            }
            numero %= 1000;
        }

        // Centenas, decenas y unidades
        if (numero > 0) {
            resultado += convertirCentenas(numero);
        }

        return resultado.trim();
    }

    /**
     * Convierte números menores a 1000
     */
    private static String convertirCentenas(int numero) {
        if (numero == 0) {
            return "";
        }

        String resultado = "";

        // Centenas
        int centenas = numero / 100;
        if (centenas > 0) {
            if (numero == 100) {
                return "cien";
            }
            resultado += CENTENAS[centenas] + " ";
            numero %= 100;
        }

        // Decenas y unidades
        if (numero >= 10 && numero < 20) {
            // Casos especiales (10-19)
            resultado += ESPECIALES[numero - 10];
        } else if (numero >= 20) {
            int decenas = numero / 10;
            int unidades = numero % 10;
            
            if (decenas == 2 && unidades > 0) {
                // Casos especiales para 20-29
                resultado += "veinti" + UNIDADES[unidades];
            } else {
                resultado += DECENAS[decenas];
                if (unidades > 0) {
                    resultado += " y " + UNIDADES[unidades];
                }
            }
        } else if (numero > 0) {
            // Unidades simples (1-9)
            resultado += UNIDADES[numero];
        }

        return resultado.trim();
    }

    /**
     * Método auxiliar para pruebas
     */
    public static void main(String[] args) {
        // Pruebas
        System.out.println(convertir(new BigDecimal("32.25")));  // treinta y dos 25/100 dólares
        System.out.println(convertir(new BigDecimal("145.00"))); // ciento cuarenta y cinco 00/100 dólares
        System.out.println(convertir(new BigDecimal("28.00")));  // veintiocho 00/100 dólares
        System.out.println(convertir(new BigDecimal("1000.50"))); // mil 50/100 dólares
        System.out.println(convertir(new BigDecimal("0.99")));   // cero 99/100 dólares
    }
}
