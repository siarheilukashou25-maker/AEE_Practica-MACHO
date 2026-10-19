package Fiche;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Crea el fichero FICHE.DAT y lo llena por teclado.
 * Cada registro se escribe en cuanto se introduce: en memoria solo
 * hay un registro a la vez.
 */
public class CrearFiche {

    private static final String NOMBRE_FICHERO = "FICHE.DAT";
    private static final int MAX_NOMBRE = 50;
    private static final int ANO_MINIMO = 1900;
    private static final double SALARIO_MAXIMO = 999999.99;

    // Letras (incluidas las acentuadas y la ñ), separadas por un espacio, guion o apóstrofo
    private static final String PATRON_NOMBRE = "\\p{L}+([ '\\-]\\p{L}+)*";
    // Hasta 6 cifras enteras y opcionalmente 1 o 2 decimales con punto o coma
    private static final String PATRON_SALARIO = "\\d{1,6}([.,]\\d{1,2})?";
    private static final String PATRON_ENTERO = "\\d{1,9}";

    private static final BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
    private static boolean finEntrada = false;

    public static void main(String[] args) {
        DataOutputStream dos = null;
        long contador = 0;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(NOMBRE_FICHERO)));
            boolean otro = pedirSiNo("¿Desea introducir un registro? (S/N): ");
            while (otro) {
                FicheRegistro reg = pedirRegistro();
                if (!finEntrada) {
                    FicheDAO.escribir(dos, reg);
                    contador++;
                }
                otro = !finEntrada && pedirSiNo("¿Otro registro? (S/N): ");
            }
            if (finEntrada) {
                System.out.println();
                System.out.println("Entrada de teclado cerrada.");
            }
            System.out.println("Fichero " + NOMBRE_FICHERO + " creado con " + contador + " registro(s).");
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Lee una línea del teclado. Si la entrada se ha cerrado, activa
     * finEntrada y devuelve una cadena vacía.
     */
    private static String leerLinea() throws IOException {
        String linea = teclado.readLine();
        if (linea == null) {
            finEntrada = true;
            linea = "";
        }
        return linea.trim();
    }

    private static FicheRegistro pedirRegistro() throws IOException {
        FicheRegistro reg = new FicheRegistro();
        System.out.println("--- Nuevo registro ---");
        reg.setNombreApe(pedirNombre());
        reg.setSexo(pedirSexo());
        reg.setSalario(pedirSalario());
        pedirFechaIngreso(reg);
        reg.setTipoEmpleado(pedirTipoEmpleado());
        reg.setProvincia(pedirProvincia());
        return reg;
    }

    // ---------- Nombre ----------

    private static String pedirNombre() throws IOException {
        System.out.print("Nombre y apellidos (máx. " + MAX_NOMBRE + " caracteres): ");
        String nombre = leerNombre();
        while (!finEntrada && !nombre.matches(PATRON_NOMBRE)) {
            System.out.print("  Solo letras, espacios, guiones y apóstrofos. Nombre y apellidos: ");
            nombre = leerNombre();
        }
        return nombre;
    }

    /** Lee el nombre, unifica los espacios y lo limita a MAX_NOMBRE caracteres. */
    private static String leerNombre() throws IOException {
        StringBuffer sb = new StringBuffer(leerLinea().replaceAll("\\s+", " "));
        if (sb.length() > MAX_NOMBRE) {
            sb.setLength(MAX_NOMBRE);
            System.out.println("  Recortado a " + MAX_NOMBRE + " caracteres: " + sb);
        }
        return sb.toString().trim();
    }

    // ---------- Sexo ----------

    private static char pedirSexo() throws IOException {
        System.out.print("Sexo (H/M): ");
        String linea = leerLinea().toUpperCase();
        while (!finEntrada && !linea.equals("H") && !linea.equals("M")) {
            System.out.print("  Introduzca H o M: ");
            linea = leerLinea().toUpperCase();
        }
        char sexo = ' ';
        if (!finEntrada) {
            sexo = linea.charAt(0);
        }
        return sexo;
    }

    // ---------- Salario ----------

    private static double pedirSalario() throws IOException {
        System.out.print("Salario (0,01 - " + SALARIO_MAXIMO + "): ");
        String linea = leerLinea();
        while (!finEntrada && !esSalarioValido(linea)) {
            System.out.print("  Salario no válido (ejemplo: 1250,50). Salario: ");
            linea = leerLinea();
        }
        double salario = 0;
        if (!finEntrada) {
            salario = Double.parseDouble(linea.replace(',', '.'));
        }
        return salario;
    }

    private static boolean esSalarioValido(String linea) {
        return linea.matches(PATRON_SALARIO)
                && Double.parseDouble(linea.replace(',', '.')) > 0;
    }

    // ---------- Fecha de ingreso ----------

    private static void pedirFechaIngreso(FicheRegistro reg) throws IOException {
        GregorianCalendar hoy = new GregorianCalendar();
        System.out.println("Fecha de ingreso:");
        GregorianCalendar fecha = pedirFecha(hoy.get(Calendar.YEAR));
        while (!finEntrada && fecha.after(hoy)) {
            System.out.println("  La fecha de ingreso no puede ser posterior a hoy. Fecha de ingreso:");
            fecha = pedirFecha(hoy.get(Calendar.YEAR));
        }
        reg.setAnoIngreso((short) fecha.get(Calendar.YEAR));
        reg.setMesIngreso((byte) (fecha.get(Calendar.MONTH) + 1));
        reg.setDiaIngreso((byte) fecha.get(Calendar.DAY_OF_MONTH));
    }

    /** Pide año, mes y día; el día se limita al número de días reales del mes. */
    private static GregorianCalendar pedirFecha(int anoActual) throws IOException {
        int ano = pedirEntero("  Año (" + ANO_MINIMO + "-" + anoActual + "): ", ANO_MINIMO, anoActual);
        int mes = pedirEntero("  Mes (1-12): ", 1, 12);
        GregorianCalendar fecha = new GregorianCalendar(ano, mes - 1, 1);
        int maxDia = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dia = pedirEntero("  Día (1-" + maxDia + "): ", 1, maxDia);
        fecha.set(Calendar.DAY_OF_MONTH, dia);
        return fecha;
    }

    // ---------- Enteros ----------

    private static int pedirEntero(String mensaje, int min, int max) throws IOException {
        System.out.print(mensaje);
        String linea = leerLinea();
        while (!finEntrada && !esEnteroEnRango(linea, min, max)) {
            System.out.print("  Debe ser un número entero entre " + min + " y " + max + ". " + mensaje.trim() + " ");
            linea = leerLinea();
        }
        int valor = 0;
        if (!finEntrada) {
            valor = Integer.parseInt(linea);
        }
        return valor;
    }

    private static boolean esEnteroEnRango(String linea, int min, int max) {
        return linea.matches(PATRON_ENTERO)
                && Integer.parseInt(linea) >= min
                && Integer.parseInt(linea) <= max;
    }

    // ---------- Tipo de empleado ----------

    private static TipoEmpleado pedirTipoEmpleado() throws IOException {
        StringBuilder menu = new StringBuilder("Tipo de empleado (");
        TipoEmpleado[] valores = TipoEmpleado.values();
        for (int i = 0; i < valores.length; i++) {
            menu.append(valores[i].getCodigo()).append('=').append(valores[i].getDescripcion());
            if (i < valores.length - 1) {
                menu.append(", ");
            }
        }
        menu.append("): ");
        System.out.print(menu);
        String linea = leerLinea().toUpperCase();
        while (!finEntrada && !esTipoValido(linea)) {
            System.out.print("  Código no válido. " + menu);
            linea = leerLinea().toUpperCase();
        }
        TipoEmpleado tipo = null;
        if (!finEntrada) {
            tipo = TipoEmpleado.fromCodigo(linea.charAt(0));
        }
        return tipo;
    }

    private static boolean esTipoValido(String linea) {
        boolean valido = false;
        if (linea.length() == 1) {
            try {
                TipoEmpleado.fromCodigo(linea.charAt(0));
                valido = true;
            } catch (IllegalArgumentException e) {
                valido = false;
            }
        }
        return valido;
    }

    // ---------- Provincia ----------

    private static Provincia pedirProvincia() throws IOException {
        System.out.println("Provincias:");
        for (Provincia p : Provincia.values()) {
            System.out.println("  " + p.getCodigo() + " - " + p.getNombre());
        }
        System.out.print("Código de provincia: ");
        String linea = leerLinea();
        while (!finEntrada && !esProvinciaValida(linea)) {
            System.out.print("  Código no válido. Código de provincia: ");
            linea = leerLinea();
        }
        Provincia prov = null;
        if (!finEntrada) {
            prov = Provincia.fromCodigo(Byte.parseByte(linea));
        }
        return prov;
    }

    private static boolean esProvinciaValida(String linea) {
        boolean valida = false;
        if (esEnteroEnRango(linea, 0, Byte.MAX_VALUE)) {
            try {
                Provincia.fromCodigo(Byte.parseByte(linea));
                valida = true;
            } catch (IllegalArgumentException e) {
                valida = false;
            }
        }
        return valida;
    }

    // ---------- S/N ----------

    private static boolean pedirSiNo(String mensaje) throws IOException {
        System.out.print(mensaje);
        String respuesta = leerLinea().toUpperCase();
        while (!finEntrada && !respuesta.equals("S") && !respuesta.equals("N")) {
            System.out.print("  Introduzca S o N. " + mensaje);
            respuesta = leerLinea().toUpperCase();
        }
        return respuesta.equals("S");
    }
}
