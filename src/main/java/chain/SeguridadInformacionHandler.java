package chain;

/**
 * Participante GoF: <b>ConcreteHandler</b>.
 * <p>
 * Oficial de Seguridad de la Información: aprueba accesos de criticidad ALTA
 * hasta 30 días, condicionados a autenticación multifactor.
 */
public class SeguridadInformacionHandler extends AprobadorHandler {

    private static final int DIAS_MAXIMOS = 30;

    @Override
    protected boolean puedeResolver(SolicitudAcceso s) {
        return s.getNivel() == NivelCriticidad.ALTO && s.getDiasDuracion() <= DIAS_MAXIMOS;
    }

    @Override
    protected ResultadoAprobacion resolver(SolicitudAcceso s) {
        return ResultadoAprobacion.aprobada(getNombre(),
                "Acceso sensible aprobado con MFA obligatorio y revisión al vencimiento", s);
    }

    @Override
    public String getNombre() {
        return "Seguridad de la Información";
    }
}
