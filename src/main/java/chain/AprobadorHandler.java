package chain;

/**
 * Participante GoF: <b>Handler</b>.
 * <p>
 * Define la interfaz común para atender solicitudes y mantiene la referencia
 * al sucesor. Cada aprobador concreto decide si puede resolver la solicitud;
 * si no puede, la reenvía al siguiente sin que el cliente lo sepa.
 */
public abstract class AprobadorHandler {

    private AprobadorHandler siguiente;

    /**
     * Enlaza el sucesor y lo devuelve para encadenar de forma fluida:
     * {@code a.setSiguiente(b).setSiguiente(c)}.
     */
    public AprobadorHandler setSiguiente(AprobadorHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /**
     * Atiende la solicitud o la delega al sucesor.
     * Es {@code final} para que todos los eslabones respeten el mismo protocolo de paso.
     */
    public final ResultadoAprobacion procesar(SolicitudAcceso solicitud) {
        solicitud.registrarPaso(getNombre());
        if (puedeResolver(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return ResultadoAprobacion.rechazada(getNombre(),
                "Ningún aprobador de la cadena tiene autoridad para esta solicitud", solicitud);
    }

    /** Indica si este eslabón tiene autoridad sobre la solicitud. */
    protected abstract boolean puedeResolver(SolicitudAcceso solicitud);

    /** Emite la decisión (aprobar o rechazar) cuando este eslabón tiene autoridad. */
    protected abstract ResultadoAprobacion resolver(SolicitudAcceso solicitud);

    public abstract String getNombre();
}
