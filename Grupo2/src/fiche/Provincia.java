
package fiche;

/**
 *
 * @author Santiago González
 */
public enum Provincia {
    ALMERIA((byte) 1, "Almería"),
    CADIZ((byte) 2, "Cádiz"),
    CORDOBA((byte) 3, "Córdoba"),
    GRANADA((byte) 4, "Granada"),
    HUELVA((byte) 5, "Huelva"),
    JAEN((byte) 6, "Jaén"),
    MALAGA((byte) 7, "Málaga"),
    SEVILLA((byte) 8, "Sevilla");
    
    private final byte codigo;
    private final String nombre;
    
    private Provincia(byte codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
    
    public byte getCodigo() {
        return codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public static Provincia fromCodigo(byte codigo) {
        for (Provincia p : values()) {
            if (p.codigo == codigo) {
                return p;
            }
        }
        throw new IllegalArgumentException("Provincia no válida: " + codigo);
    }
}
