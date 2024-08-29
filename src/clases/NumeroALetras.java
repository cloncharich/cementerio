/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Claudio Loncharich
 */
public class NumeroALetras {
    
     private static final String[] UNIDADES = {
            "", "UN", "DOS", "TRES", "CUATRO", "CINCO",
            "SEIS", "SIETE", "OCHO", "NUEVE"
    };

    private static final String[] DECENAS = {
            "", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA",
            "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
    };

    private static final String[] CENTENAS = {
            "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS",
            "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
    };

    public static String convertir(long monto) {
        if (monto == 0) {
            return "CERO GUARANÍES";
        }

        String montoEnLetras = convertirNumero(monto);
        return montoEnLetras + " GUARANÍES";
    }

    private static String convertirNumero(long numero) {
        if (numero == 0) {
            return "";
        }

        if (numero < 10) {
            return UNIDADES[(int) numero];
        } else if (numero < 20) {
            return DECENAS[(int) numero / 10] + (numero % 10 == 0 ? "" : " Y " + UNIDADES[(int) numero % 10]);
        } else if (numero < 100) {
            return DECENAS[(int) numero / 10] + (numero % 10 == 0 ? "" : " Y " + UNIDADES[(int) numero % 10]);
        } else if (numero < 1000) {
            return CENTENAS[(int) numero / 100] + (numero % 100 == 0 ? "" : " " + convertirNumero(numero % 100));
        } else if (numero < 1000000) { // Menos de un millón
            return (numero / 1000 == 1 ? "MIL" : convertirNumero(numero / 1000) + " MIL")
                    + (numero % 1000 == 0 ? "" : " " + convertirNumero(numero % 1000));
        } else if (numero < 1000000000000L) { // Menos de un billón
            return (numero / 1000000 == 1 ? "UN MILLÓN" : convertirNumero(numero / 1000000) + " MILLONES")
                    + (numero % 1000000 == 0 ? "" : " " + convertirNumero(numero % 1000000));
        } else {
            return "Número demasiado grande";
        }
    }

    
}