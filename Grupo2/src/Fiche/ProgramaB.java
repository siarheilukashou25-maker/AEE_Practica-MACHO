package Fiche;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago González
 */
public class ProgramaB {

    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    private static final int TAMANO_PAGINA = 10;

    public static void main(String[] args) {
        BufferedReader teclado = null;
        DataInputStream dis = null;

        File fichero = new File(NOMBRE_FICHERO);

        if (!fichero.exists()) {
            System.out.println("El fichero " + NOMBRE_FICHERO + " no existe.");
            return;
        }

        System.out.println("LISTADO PAGINADO DE EMPLEADOS (>= 10 AÑOS)");

        int totalEmpleados = 0;
        int empleadosVeteranos = 0;
        int contadorPagina = 0;

        int annoActual = Calendar.getInstance().get(Calendar.YEAR);

        try {
            teclado = new BufferedReader(new InputStreamReader(System.in));
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));

            boolean eof = false;

            
            FicheRegistro reg = FicheDAO.leer(dis);
            totalEmpleados++;

            int antiguedad = annoActual - reg.getAnoIngreso();

            if (antiguedad >= 10) {
                empleadosVeteranos++;
                contadorPagina++;

                System.out.println("-> " + reg.getNombreApe()
                        + " | Sexo: " + reg.getSexo()
                        + " | Salario: " + reg.getSalario() + "€"
                        + " | Ingreso: " + reg.getDiaIngreso() + "/" + reg.getMesIngreso() + "/" + reg.getAnoIngreso()
                        + " | Antigüedad: " + antiguedad + " años"
                        + " | Tipo: " + reg.getTipoEmpleado().getDescripcion()
                        + " | Provincia: " + reg.getProvincia().getNombre());

                if (contadorPagina == TAMANO_PAGINA) {
                    System.out.println("\nPulse INTRO para ver la siguiente página");
                    teclado.readLine();
                    contadorPagina = 0;
                    System.out.println();
                }
            }

            System.out.println("----------------------------------------------------");
            if (totalEmpleados > 0) {
                double porcentaje = (empleadosVeteranos * 100.0) / totalEmpleados;
                System.out.println("Total de empleados analizados: " + totalEmpleados);
                System.out.println("Empleados con 10 o más años: " + empleadosVeteranos);
                System.out.printf("Porcentaje total: %.2f%%\n", porcentaje);
            } else {
                System.out.println("Aviso: El fichero está vacío, no hay trabajadores registrados.");
            }

        } catch (IOException e) {
            System.err.println("Error de E/S al leer el fichero: " + e.getMessage());
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
        }
    }
}
