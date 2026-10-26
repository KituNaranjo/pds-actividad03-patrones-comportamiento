package chain;

/**
 * Participante GoF: <b>ConcreteHandler</b>.
 * <p>
 * Primer eslabón: rechaza de inmediato las solicitudes incompletas o fuera de política,
 * para que ningún aprobador humano pierda tiempo con ellas.
 */
public class ValidacionSolicitudHandler extends AprobadorHandler {

    private final int diasMaximos;

    public ValidacionSolicitudHandler(int diasMaximos) {
        this.diasMaximos = diasMaximos;
    }

    @Override
    protected boolean puedeResolver(SolicitudAcceso s) {
        return motivoInvalidez(s) != null;
    }

    @Override
    protected ResultadoAprobacion resolver(SolicitudAcceso s) {
        return ResultadoAprobacion.rechazada(getNombre(), motivoInvalidez(s), s);
    }

    private String motivoInvalidez(SolicitudAcceso s) {
        if (estaVacio(s.getSolicitante())) {
            return "La solicitud no identifica al solicitante";
        }
        if (estaVacio(s.getRecurso())) {
            return "La solicitud no indica el recurso";
        }
        if (estaVacio(s.getJustificacion())) {
            return "La solicitud no tiene justificación de negocio";
        }
        if (s.getDiasDuracion() <= 0 || s.getDiasDuracion() > diasMaximos) {
            return "La duración debe estar entre 1 y " + diasMaximos + " días";
        }
        return null;
    }

    private static boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    @Override
    public String getNombre() {
        return "Validación automática";
    }
}
