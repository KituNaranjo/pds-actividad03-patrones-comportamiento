package chain;

import java.util.List;

/**
 * Decisión final que devuelve la cadena: quién resolvió, por qué
 * y por qué eslabones pasó la solicitud.
 */
public record ResultadoAprobacion(boolean aprobada, String resueltoPor, String motivo, List<String> recorrido) {

    public static ResultadoAprobacion aprobada(String resueltoPor, String motivo, SolicitudAcceso s) {
        return new ResultadoAprobacion(true, resueltoPor, motivo, List.copyOf(s.getRecorrido()));
    }

    public static ResultadoAprobacion rechazada(String resueltoPor, String motivo, SolicitudAcceso s) {
        return new ResultadoAprobacion(false, resueltoPor, motivo, List.copyOf(s.getRecorrido()));
    }

    @Override
    public String toString() {
        return (aprobada ? "APROBADA" : "RECHAZADA") + " por " + resueltoPor + " — " + motivo
                + " | recorrido: " + String.join(" → ", recorrido);
    }
}
