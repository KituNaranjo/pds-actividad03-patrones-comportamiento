package command;

/**
 * Participante GoF: <b>ConcreteCommand</b>.
 * <p>
 * Recuerda si la licencia realmente se agregó: si el usuario ya la tenía,
 * deshacer no debe quitársela.
 */
public class AsignarLicenciaCommand implements OperacionAdministrativa {

    private final DirectorioUsuarios directorio;
    private final String upn;
    private final String sku;
    private boolean seAsigno;

    public AsignarLicenciaCommand(DirectorioUsuarios directorio, String upn, String sku) {
        this.directorio = directorio;
        this.upn = upn;
        this.sku = sku;
    }

    @Override
    public void ejecutar() {
        seAsigno = directorio.asignarLicencia(upn, sku);
    }

    @Override
    public void deshacer() {
        if (seAsigno) {
            directorio.revocarLicencia(upn, sku);
            seAsigno = false;
        }
    }

    @Override
    public String getDescripcion() {
        return "Asignar licencia " + sku + " a " + upn;
    }
}
