package chain;

/**
 * Participante GoF: <b>ConcreteHandler</b>.
 * <p>
 * Mesa de servicio (Soporte N1): aprueba accesos de criticidad BAJA y corta duración.
 */
public class SoporteNivel1Handler extends AprobadorHandler {

    private static final int DIAS_MAXIMOS = 30;

    @Override
    protected boolean puedeResolver(SolicitudAcceso s) {
        return s.getNivel() == NivelCriticidad.BAJO && s.getDiasDuracion() <= DIAS_MAXIMOS;
    }

    @Override
    protected ResultadoAprobacion resolver(SolicitudAcceso s) {
        return ResultadoAprobacion.aprobada(getNombre(),
                "Acceso de criticidad baja dentro de la política estándar", s);
    }

    @Override
    public String getNombre() {
        return "Soporte N1";
    }
}
