package chain;

/**
 * Arma la cadena de aprobación vigente. Es el único lugar que conoce el orden
 * de los eslabones: cambiar la política de la organización significa cambiar
 * este ensamblaje, no la lógica de los aprobadores ni la del cliente.
 */
public final class ConfiguradorCadenaAprobacion {

    public static final int DIAS_MAXIMOS_POLITICA = 90;

    private ConfiguradorCadenaAprobacion() {
    }

    public static AprobadorHandler cadenaEstandar() {
        AprobadorHandler inicio = new ValidacionSolicitudHandler(DIAS_MAXIMOS_POLITICA);
        inicio.setSiguiente(new SoporteNivel1Handler())
              .setSiguiente(new JefeAreaHandler())
              .setSiguiente(new SeguridadInformacionHandler())
              .setSiguiente(new ComiteCambiosHandler());
        return inicio;
    }
}
