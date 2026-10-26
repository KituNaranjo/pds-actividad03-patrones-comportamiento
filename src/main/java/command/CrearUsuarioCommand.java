package command;

/**
 * Participante GoF: <b>ConcreteCommand</b>.
 */
public class CrearUsuarioCommand implements OperacionAdministrativa {

    private final DirectorioUsuarios directorio;
    private final String upn;
    private final String nombre;

    public CrearUsuarioCommand(DirectorioUsuarios directorio, String upn, String nombre) {
        this.directorio = directorio;
        this.upn = upn;
        this.nombre = nombre;
    }

    @Override
    public void ejecutar() {
        directorio.crearUsuario(upn, nombre);
    }

    @Override
    public void deshacer() {
        directorio.eliminarUsuario(upn);
    }

    @Override
    public String getDescripcion() {
        return "Crear cuenta " + upn;
    }
}
