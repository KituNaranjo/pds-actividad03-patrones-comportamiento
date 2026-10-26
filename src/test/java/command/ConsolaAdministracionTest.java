package command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolaAdministracionTest {

    private static final String UPN = "lvera@empresa.ec";

    private DirectorioUsuarios directorio;
    private ConsolaAdministracion consola;

    @BeforeEach
    void setUp() {
        directorio = new DirectorioUsuarios();
        consola = new ConsolaAdministracion("tester");
    }

    @Test
    @DisplayName("Ejecutar y deshacer la creación de una cuenta")
    void crearYDeshacer() {
        assertTrue(consola.ejecutar(new CrearUsuarioCommand(directorio, UPN, "Lucía Vera")));
        assertTrue(directorio.existe(UPN));
        assertTrue(consola.deshacer());
        assertFalse(directorio.existe(UPN));
        assertTrue(consola.puedeRehacer());
    }

    @Test
    @DisplayName("Rehacer vuelve a aplicar la operación deshecha")
    void rehacer() {
        consola.ejecutar(new CrearUsuarioCommand(directorio, UPN, "Lucía Vera"));
        consola.deshacer();
        assertTrue(consola.rehacer());
        assertTrue(directorio.existe(UPN));
        assertFalse(consola.puedeRehacer());
    }

    @Test
    @DisplayName("Una nueva operación invalida la pila de rehacer")
    void nuevaOperacionLimpiaRehacer() {
        consola.ejecutar(new CrearUsuarioCommand(directorio, UPN, "Lucía Vera"));
        consola.ejecutar(new BloquearCuentaCommand(directorio, UPN, "Prueba"));
        consola.deshacer();
        consola.ejecutar(new AsignarLicenciaCommand(directorio, UPN, "M365_E3"));
        assertFalse(consola.puedeRehacer());
    }

    @Test
    @DisplayName("Deshacer el bloqueo restaura el estado previo, no un valor fijo")
    void bloqueoRestauraEstadoPrevio() {
        directorio.crearUsuario(UPN, "Lucía Vera");
        directorio.bloquearCuenta(UPN);

        consola.ejecutar(new BloquearCuentaCommand(directorio, UPN, "Duplicado"));
        consola.deshacer();
        assertTrue(directorio.estaBloqueada(UPN), "La cuenta ya estaba bloqueada antes del comando");
    }

    @Test
    @DisplayName("Deshacer una licencia que ya existía no la revoca")
    void licenciaPreexistenteNoSeRevoca() {
        directorio.crearUsuario(UPN, "Lucía Vera");
        directorio.asignarLicencia(UPN, "M365_E3");

        consola.ejecutar(new AsignarLicenciaCommand(directorio, UPN, "M365_E3"));
        consola.deshacer();
        assertTrue(directorio.buscar(UPN).orElseThrow().getLicencias().contains("M365_E3"));
    }

    @Test
    @DisplayName("El alta de colaborador se ejecuta y se deshace como una unidad")
    void macroComando() {
        consola.ejecutar(new AltaColaboradorCommand("Alta", List.of(
                new CrearUsuarioCommand(directorio, UPN, "Lucía Vera"),
                new AsignarLicenciaCommand(directorio, UPN, "M365_E3"),
                new AsignarLicenciaCommand(directorio, UPN, "EDR_SENSOR"))));
        assertEquals(2, directorio.buscar(UPN).orElseThrow().getLicencias().size());

        consola.deshacer();
        assertFalse(directorio.existe(UPN));
    }

    @Test
    @DisplayName("Si un paso del alta falla, se revierten los pasos ya ejecutados")
    void macroComandoRevierteAnteFallo() {
        boolean ok = consola.ejecutar(new AltaColaboradorCommand("Alta", List.of(
                new CrearUsuarioCommand(directorio, UPN, "Lucía Vera"),
                new AsignarLicenciaCommand(directorio, "otro@empresa.ec", "M365_E3"))));
        assertFalse(ok);
        assertFalse(directorio.existe(UPN));
        assertFalse(consola.puedeDeshacer());
    }

    @Test
    @DisplayName("Una operación fallida queda en la bitácora y no entra al historial")
    void operacionFallidaSeAudita() {
        consola.ejecutar(new CrearUsuarioCommand(directorio, UPN, "Lucía Vera"));
        assertFalse(consola.ejecutar(new CrearUsuarioCommand(directorio, UPN, "Duplicado")));

        List<String> bitacora = consola.getBitacora();
        assertEquals(2, bitacora.size());
        assertTrue(bitacora.get(1).contains("ERROR"));
        consola.deshacer();
        assertFalse(consola.puedeDeshacer());
    }

    @Test
    @DisplayName("Deshacer o rehacer sin historial no hace nada")
    void sinHistorial() {
        assertFalse(consola.deshacer());
        assertFalse(consola.rehacer());
        assertTrue(consola.getBitacora().isEmpty());
    }
}
