
package fiche;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 *
 * @author Santiago González
 */
public class FicheDAO {
    private static boolean esFinFichero = false;

    private FicheDAO() {
    }

    public static boolean ff() {
        return esFinFichero;
    }

    public static FicheRegistro leer(DataInputStream data) throws IOException {
        try {
            String nombreApe = data.readUTF();
            char sexo = data.readChar();
            double salario = data.readDouble();
            short anoIngreso = data.readShort();
            byte mesIngreso = data.readByte();
            byte diaIngreso = data.readByte();
            char tipo = data.readChar();
            byte prov = data.readByte();
            return new FicheRegistro(nombreApe, sexo, salario, anoIngreso,mesIngreso, diaIngreso,
                    TipoEmpleado.fromCodigo(tipo), Provincia.fromCodigo(prov));
        } catch (EOFException eofe) {
            esFinFichero = true;
            return null;
        } catch (IllegalArgumentException iae) {
            throw new IOException("Fichero corrupto.");
        }
    }

    public static void escribir(DataOutputStream data, FicheRegistro reg) throws IOException {
        data.writeUTF(reg.getNombreApe());
        data.writeChar(reg.getSexo());
        data.writeDouble(reg.getSalario());
        data.writeShort(reg.getAnoIngreso());
        data.writeByte(reg.getMesIngreso());
        data.writeByte(reg.getDiaIngreso());
        data.writeChar(reg.getTipoEmpleado().getCodigo());
        data.writeByte(reg.getProvincia().getCodigo());
    }
}
