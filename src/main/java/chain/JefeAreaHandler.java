package chain;

/**
 * Participante GoF: <b>ConcreteHandler</b>.
 * <p>
 * Jefe de área: aprueba accesos de criticidad BAJA o MEDIA hasta 60 días
 * (incluye los accesos bajos que exceden lo que puede aprobar Soporte N1).
 */
public class JefeAreaHandler extends AprobadorHandler {

    private static final int DIAS_MAXIMOS = 60;

    @Override
    protected boolean puedeResolver(SolicitudAcceso s) {
        return s.getNivel().compareTo(NivelCriticidad.MEDIO) <= 0 && s.getDiasDuracion() <= DIAS_MAXIMOS;
    }

    @Override
    protected ResultadoAprobacion resolver(SolicitudAcceso s) {
        return ResultadoAprobacion.aprobada(getNombre(),
                "Acceso del área aprobado por el responsable funcional", s);
    }

    @Override
    public String getNombre() {
        return "Jefe de área";
    }
}
