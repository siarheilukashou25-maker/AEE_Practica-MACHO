
package fiche;

/**
 *
 * @author Santiago González
 */
public enum TipoEmpleado {
    C('C', "A comisión"),
    F('F', "Fijos"),
    D('D', "A domicilio");
    
    private final char codigo;
    private final String descripcion;
    
    private TipoEmpleado(char codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    public char getCodigo() {
        return codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public static TipoEmpleado fromCodigo(char codigo) {
        for (TipoEmpleado t : values()) {
            if (t.codigo == codigo) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de empleado no válido: " + codigo);
    }
}
