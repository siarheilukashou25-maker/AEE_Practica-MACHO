package Fiche;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

/**
 *
 * @author LukashouSiarhei
 * @author Santiago Gonzalez
 */
public class ProgramaD {
    private static final String NOMBRE_FICHERO = "Ficheros" + File.separator + "FICHE.DAT";
    private static final double PRECIO_TRIENIO = 24;
    private static final double PORCENTAJE_DESTINO = 0.10;
    private static final double COMPLEMENTO_POR_LA_CARA = 120;
    
    public static void main(String[] args) {
        File fichero = new File(NOMBRE_FICHERO);
        
        if (!fichero.exists()) {
            System.out.println("El fichero " + NOMBRE_FICHERO + " no existe.");
        } else {
            System.out.println("EMPLEADO(S) CON EL SUELDO SUPERIOR");
        
            GregorianCalendar hoy = new GregorianCalendar();
        
            double maxSueldo = -1.0;
        
            DataInputStream dis1 = null;
            try {
                dis1 = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));
                FicheRegistro reg = FicheDAO.leer(dis1);
            
                while (!FicheDAO.ff() && reg != null) {
                    double sueldoActual = calcularSueldo(reg, hoy);
                
                    if (sueldoActual > maxSueldo) {
                        maxSueldo = sueldoActual;
                    }
                    reg = FicheDAO.leer(dis1);
                }
            } catch (IOException e) {
                System.err.println("Error de E/S en la primera pasada del fichero: " + e.getMessage());
            } finally {
                if (dis1 != null) {
                    try {
                        dis1.close();
                    } catch (IOException e) {
                        System.err.println("Error al cerrar el flujo en la primera pasada: " + e.getMessage());
                    }
                }
            }

            if (maxSueldo < 0) {
                System.out.println("El fichero está vacío o no se pudieron procesar datos.");
            } else {
                DataInputStream dis2 = null;
                try {
                    dis2 = new DataInputStream(new BufferedInputStream(new FileInputStream(fichero)));
            
                    System.out.printf("Sueldo máximo encontrado: %.2f€\n", maxSueldo);
                    System.out.println("Empleado(s) con el sueldo máximo:");
                    System.out.println("----------------------------------------------------");
            
                    FicheRegistro reg = FicheDAO.leer(dis2);
                    while (!FicheDAO.ff() && reg != null) {
                        double sueldoActual = calcularSueldo(reg, hoy);
                
                        if (Math.abs(sueldoActual - maxSueldo) < 0.001) {
                            System.out.println("-> Nombre: " + reg.getNombreApe()
                                    + " | Sueldo Total: " + String.format("%.2f", sueldoActual) + "€"
                                    + " | Provincia: " + reg.getProvincia().getNombre()
                                    + " | Tipo: " + reg.getTipoEmpleado().getDescripcion());
                        }
                        reg = FicheDAO.leer(dis2);
                    }
                    System.out.println("----------------------------------------------------");
            
                } catch (IOException e) {
                    System.err.println("Error de E/S en la segunda pasada del fichero: " + e.getMessage());
                } finally {
                    if (dis2 != null) {
                        try {
                            dis2.close();
                        } catch (IOException e) {
                            System.err.println("Error al cerrar el flujo en la segunda pasada: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
    
    private static double calcularSueldo(FicheRegistro reg, GregorianCalendar hoy) {
        double complementos = calcularTrienios(reg, hoy) * PRECIO_TRIENIO;
        
        Provincia prov = reg.getProvincia();
        if (prov == Provincia.JAEN || prov == Provincia.HUELVA || prov == Provincia.ALMERIA) {
            complementos += reg.getSalario() * PORCENTAJE_DESTINO;
        }
        
        if (reg.getSexo() == 'H') {
            complementos += COMPLEMENTO_POR_LA_CARA;
        }
        
        return reg.getSalario() + complementos;
    }
    
    private static int calcularTrienios(FicheRegistro reg, GregorianCalendar hoy) {
        GregorianCalendar ingreso = new GregorianCalendar(reg.getAnoIngreso(), reg.getMesIngreso() - 1, reg.getDiaIngreso());
        
        int anos = hoy.get(GregorianCalendar.YEAR) - ingreso.get(GregorianCalendar.YEAR);
        
        ingreso.add(GregorianCalendar.YEAR, anos);
        
        if (ingreso.after(hoy)) {
            anos--;
        }
        
        int trienios = 0;
        
        if (anos > 0) {
            trienios = anos / 3;
        }
        
        return trienios;
    }
}
