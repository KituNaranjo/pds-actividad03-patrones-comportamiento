package chain;

import java.util.Objects;

/**
 * Participante GoF: <b>Client</b>.
 * <p>
 * Recibe las solicitudes de acceso y las entrega al primer eslabón.
 * No conoce cuántos aprobadores existen ni quién terminará resolviendo.
 */
public class MesaServicioAccesos {

    private final AprobadorHandler cadena;

    public MesaServicioAccesos(AprobadorHandler cadena) {
        this.cadena = Objects.requireNonNull(cadena, "cadena");
    }

    public ResultadoAprobacion tramitar(SolicitudAcceso solicitud) {
        return cadena.procesar(solicitud);
    }
}
