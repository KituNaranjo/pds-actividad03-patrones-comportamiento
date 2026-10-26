package chain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Petición (Request) que viaja por la cadena de aprobadores.
 * Registra el recorrido para que la decisión final sea auditable.
 */
public class SolicitudAcceso {

    private final String id;
    private final String solicitante;
    private final String recurso;
    private final NivelCriticidad nivel;
    private final int diasDuracion;
    private final String justificacion;
    private final List<String> recorrido = new ArrayList<>();

    public SolicitudAcceso(String id, String solicitante, String recurso,
                           NivelCriticidad nivel, int diasDuracion, String justificacion) {
        this.id = Objects.requireNonNull(id, "id");
        this.solicitante = solicitante;
        this.recurso = recurso;
        this.nivel = Objects.requireNonNull(nivel, "nivel");
        this.diasDuracion = diasDuracion;
        this.justificacion = justificacion;
    }

    /** Lo invoca cada eslabón al recibir la solicitud. */
    void registrarPaso(String aprobador) {
        recorrido.add(aprobador);
    }

    public String getId() { return id; }
    public String getSolicitante() { return solicitante; }
    public String getRecurso() { return recurso; }
    public NivelCriticidad getNivel() { return nivel; }
    public int getDiasDuracion() { return diasDuracion; }
    public String getJustificacion() { return justificacion; }

    public List<String> getRecorrido() {
        return Collections.unmodifiableList(recorrido);
    }

    @Override
    public String toString() {
        return id + " [" + solicitante + " → " + recurso + ", " + nivel + ", " + diasDuracion + " días]";
    }
}
