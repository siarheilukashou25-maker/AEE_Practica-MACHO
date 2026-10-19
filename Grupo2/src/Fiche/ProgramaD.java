package Fiche;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class ProgramaD {
    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    
    public static void main(String[] args) {
        File fichero = new File(NOMBRE_FICHERO);
        
        if (!fichero.exists()) {
            System.out.println("El fichero " + NOMBRE_FICHERO + " no existe.");
            return;
        }
        
        System.out.println("EMPLEADO(S) CON EL SUELDO SUPERIOR");
        
        GregorianCalendar hoy = new GregorianCalendar();
        int anoActual = hoy.get(Calendar.YEAR);

        double maxSueldo = -1.0;
        
        DataInputStream dis1 = null;
        try {
            dis1 = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));
            boolean eof = false;
            
            while (!eof) {
                try {
                    FicheRegistro reg = FicheDAO.leer(dis1);
                    double sueldoActual = calcularSueldo(reg, anoActual);
                    
                    if (sueldoActual > maxSueldo) {
                        maxSueldo = sueldoActual;
                    }
                } catch (EOFException eofe) {
                    eof = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S en la primera pasada del fichero: " + e.getMessage());
        } finally {
            if (dis1 != null) {
                try {
                    dis1.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el flujo en la primera pasada.");
                }
            }
        }
        
        if (maxSueldo < 0) {
            System.out.println("El fichero está vacío o no se pudieron procesar datos.");
            return;
        }
        
        DataInputStream dis2 = null;
        try {
            dis2 = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));
            boolean eof = false;
            
            System.out.printf("Sueldo máximo encontrado: %.2f€\n", maxSueldo);
            System.out.println("Empleado(s) con el sueldo máximo:");
            System.out.println("----------------------------------------------------");
            
            while (!eof) {
                try {
                    FicheRegistro reg = FicheDAO.leer(dis2);
                    double sueldoActual = calcularSueldo(reg, anoActual);
                    
                    if (Math.abs(sueldoActual - maxSueldo) < 0.001) {
                        System.out.println("-> Nombre: " + reg.getNombreApe()
                                + " | Sueldo Total: " + String.format("%.2f", sueldoActual) + "€"
                                + " | Provincia: " + reg.getProvincia().getNombre()
                                + " | Tipo: " + reg.getTipoEmpleado().getDescripcion());
                    }
                } catch (EOFException eofe) {
                    eof = true;
                }
            }
            System.out.println("----------------------------------------------------");

        } catch (IOException e) {
            System.err.println("Error de E/S en la segunda pasada del fichero: " + e.getMessage());
        } finally {
            if (dis2 != null) {
                try {
                    dis2.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el flujo en la segunda pasada.");
                }
            }
        }
    }
    
    public static double calcularSueldo(FicheRegistro reg, int anoActual) {
        double salarioBase = reg.getSalario();
        
        int antiguedad = anoActual - reg.getAnoIngreso();
        int trienios = antiguedad / 3;
        double compTrienios = trienios * 24.0;
        
        double compDestino = 0.0;
        Provincia prov = reg.getProvincia();
        if (prov == Provincia.ALMERIA || prov == Provincia.HUELVA || prov == Provincia.JAEN) {
            compDestino = salarioBase * 0.10;
        }
        
        double compPorLaCara = 0.0;
        if (reg.getSexo() == 'H' || reg.getSexo() == 'h') {
            compPorLaCara = 120.0;
        }
        
        return salarioBase + compTrienios + compDestino + compPorLaCara;
    }
}
