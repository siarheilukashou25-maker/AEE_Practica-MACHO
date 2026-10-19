
package fiche;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 *
 * @author Santiago González
 */
public class ProgramaA {
    private static final String CARPETA = "Ficheros";
    private static final String NOMBRE_FICHERO = "FICHE.DAT";
    private static final int MAX_NOMBRE = 30;

    public static void main(String[] args) {
        File carpeta = new File(CARPETA);
        if (!carpeta.isDirectory()) {
            System.out.println("No existe la carpeta '" + CARPETA + "' en la raíz del proyecto.");
            return;
        }
        File fichero = new File(carpeta, NOMBRE_FICHERO);

        DataOutputStream salida = null;
        try {
            boolean anadir = false;
            if (fichero.exists()) {
                anadir = Teclado.leerSiNo("FICHE.DAT ya existe. ¿Añadir al final?" + " (N = borrar y empezar de cero)");
            }
            salida = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fichero, anadir)));

            int total = 0;
            do {
                FicheRegistro reg = pedirRegistro();
                FicheDAO.escribir(salida, reg);
                total++;
                System.out.println("  Guardado: " + reg + "\n");
            } while (Teclado.leerSiNo("¿Introducir otro empleado?"));

            System.out.println("Empleados guardados en esta sesión: " + total);
        } catch (IOException ioe) {
            System.out.println("Error de entrada/salida.");
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar el fichero: " + ex.getMessage());
                }
            }
        }
    }

    private static FicheRegistro pedirRegistro() throws IOException {
        System.out.println("NUEVO EMPLEADO");
        String nombre = Teclado.leerCadena("Nombre y apellidos: ", MAX_NOMBRE);
        char sexo = Teclado.leerChar("Sexo (M/H): ", "MH");
        double salario = Teclado.leerDouble("Salario (0 - 9999.99): ", 0, 9999.99);

        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        int anio = Teclado.leerEntero("Año de ingreso (1950-" + anioActual + "): ", 1950, anioActual);
        int mes = Teclado.leerEntero("Mes de ingreso (1-12): ", 1, 12);
        int dia = Teclado.leerEntero("Día de ingreso (1-" + diasDelMes(anio, mes) + "): ", 1, diasDelMes(anio, mes));

        char tipo = Teclado.leerChar("Tipo de empleado (C=A comisión, F=Fijo, D=A domicilio): ", "CFD");

        System.out.println("Provincias:");
        for (Provincia p : Provincia.values()) {
            System.out.println("  " + p.getCodigo() + " - " + p.getNombre());
        }
        int prov = Teclado.leerEntero("Provincia (1-8): ", 1, 8);

        return new FicheRegistro(nombre, sexo, salario, (short) anio,
                (byte) mes, (byte) dia,
                TipoEmpleado.fromCodigo(tipo), Provincia.fromCodigo((byte) prov));
    }

    private static int diasDelMes(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(anio, mes - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
