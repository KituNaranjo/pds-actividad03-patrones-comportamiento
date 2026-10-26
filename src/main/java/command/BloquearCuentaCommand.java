package command;

/**
 * Participante GoF: <b>ConcreteCommand</b>.
 * <p>
 * Guarda el estado previo de la cuenta para que deshacer restaure
 * exactamente lo que había, no un valor fijo.
 */
public class BloquearCuentaCommand implements OperacionAdministrativa {

    private final DirectorioUsuarios directorio;
    private final String upn;
    private final String motivo;
    private boolean estabaBloqueada;

    public BloquearCuentaCommand(DirectorioUsuarios directorio, String upn, String motivo) {
        this.directorio = directorio;
        this.upn = upn;
        this.motivo = motivo;
    }

    @Override
    public void ejecutar() {
        estabaBloqueada = directorio.estaBloqueada(upn);
        directorio.bloquearCuenta(upn);
    }

    @Override
    public void deshacer() {
        if (!estabaBloqueada) {
            directorio.desbloquearCuenta(upn);
        }
    }

    @Override
    public String getDescripcion() {
        return "Bloquear cuenta " + upn + " (" + motivo + ")";
    }
}
