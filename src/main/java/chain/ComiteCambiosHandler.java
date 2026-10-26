package chain;

/**
 * Participante GoF: <b>ConcreteHandler</b> (último eslabón).
 * <p>
 * Comité de cambios: resuelve todo lo que los eslabones anteriores no pudieron.
 * Solo concede accesos de emergencia de hasta 7 días; el resto se rechaza.
 */
public class ComiteCambiosHandler extends AprobadorHandler {

    private static final int DIAS_EMERGENCIA = 7;

    @Override
    protected boolean puedeResolver(SolicitudAcceso s) {
        return true;
    }

    @Override
    protected ResultadoAprobacion resolver(SolicitudAcceso s) {
        if (s.getDiasDuracion() <= DIAS_EMERGENCIA) {
            return ResultadoAprobacion.aprobada(getNombre(),
                    "Acceso temporal de emergencia (máx. " + DIAS_EMERGENCIA + " días) con sesión grabada", s);
        }
        return ResultadoAprobacion.rechazada(getNombre(),
                "Excede la duración permitida para su criticidad; requiere un cambio planificado", s);
    }

    @Override
    public String getNombre() {
        return "Comité de cambios";
    }
}
