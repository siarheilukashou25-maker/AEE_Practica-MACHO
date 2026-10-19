package Fiche;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class FicheDAO {

    private static boolean esFinFichero = false;

    public static boolean ff() {
        return esFinFichero;
    }

    /**
     * Lee un registro. Si se alcanza el fin de fichero, devuelve null
     * y ff() pasa a ser true.
     */
    public static FicheRegistro leer(DataInputStream data) throws IOException {
        FicheRegistro reg = null;
        try {
            FicheRegistro leido = new FicheRegistro();
            leido.setNombreApe(data.readUTF());
            leido.setSexo(data.readChar());
            leido.setSalario(data.readDouble());
            leido.setAnoIngreso(data.readShort());
            leido.setMesIngreso(data.readByte());
            leido.setDiaIngreso(data.readByte());
            leido.setTipoEmpleado(TipoEmpleado.fromCodigo(data.readChar()));
            leido.setProvincia(Provincia.fromCodigo(data.readByte()));
            reg = leido;
            esFinFichero = false;
        } catch (EOFException e) {
            esFinFichero = true;
        }
        return reg;
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
