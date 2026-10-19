package Fiche;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class ProgramaB {

    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    private static final int TAMANO_PAGINA = 10;
    private static final int ANOS_MINIMOS = 10;

    public static void main(String[] args) {
        File fichero = new File(NOMBRE_FICHERO);

        if (!fichero.exists()) {
            System.out.println("El fichero " + NOMBRE_FICHERO + " no existe.");
        } else {
            BufferedReader teclado = null;
            DataInputStream dis = null;

            int totalEmpleados = 0;
            int empleadosVeteranos = 0;
            int contadorPagina = 0;

            GregorianCalendar hoy = new GregorianCalendar();

            try {
                teclado = new BufferedReader(new InputStreamReader(System.in));
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));

                System.out.println("LISTADO PAGINADO DE EMPLEADOS (>= " + ANOS_MINIMOS + " AÑOS)");

                FicheRegistro reg = FicheDAO.leer(dis);
                while (!FicheDAO.ff()) {
                    totalEmpleados++;

                    int antiguedad = calcularAnos(reg, hoy);

                    if (antiguedad >= ANOS_MINIMOS) {
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
                            System.out.println();
                            System.out.println("Pulse INTRO para ver la siguiente página");
                            teclado.readLine();
                            contadorPagina = 0;
                            System.out.println();
                        }
                    }
                    reg = FicheDAO.leer(dis);
                }

                System.out.println("----------------------------------------------------");
                if (totalEmpleados > 0) {
                    double porcentaje = (empleadosVeteranos * 100.0) / totalEmpleados;
                    System.out.println("Total de empleados analizados: " + totalEmpleados);
                    System.out.println("Empleados con " + ANOS_MINIMOS + " o más años: " + empleadosVeteranos);
                    System.out.printf("Porcentaje total: %.2f%%%n", porcentaje);
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
                if (teclado != null) {
                    try {
                        teclado.close();
                    } catch (IOException e) {
                        System.err.println("Error al cerrar el teclado: " + e.getMessage());
                    }
                }
            }
        }
    }

    private static int calcularAnos(FicheRegistro reg, GregorianCalendar hoy) {
        GregorianCalendar ingreso = new GregorianCalendar(reg.getAnoIngreso(), reg.getMesIngreso() - 1, reg.getDiaIngreso());
        int anos = hoy.get(GregorianCalendar.YEAR) - ingreso.get(GregorianCalendar.YEAR);
        ingreso.add(GregorianCalendar.YEAR, anos);
        if (ingreso.after(hoy)) {
            anos--;
        }
        return anos;
    }
}
