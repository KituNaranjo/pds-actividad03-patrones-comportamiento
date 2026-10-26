import chain.ConfiguradorCadenaAprobacion;
import chain.MesaServicioAccesos;
import chain.NivelCriticidad;
import chain.ResultadoAprobacion;
import chain.SolicitudAcceso;
import command.AltaColaboradorCommand;
import command.AsignarLicenciaCommand;
import command.BloquearCuentaCommand;
import command.ConsolaAdministracion;
import command.CrearUsuarioCommand;
import command.DirectorioUsuarios;

import java.util.List;

/**
 * Demostración de ambos patrones (Client).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println();
        demoChainOfResponsibility();
        System.out.println();
        demoCommand();
        System.out.println();
    }

    private static void demoChainOfResponsibility() {
        Pantalla.titulo("PATRÓN 01", "Chain of Responsibility",
                "Aprobación escalonada de solicitudes de acceso", Pantalla.CIAN);
        MesaServicioAccesos mesa = new MesaServicioAccesos(ConfiguradorCadenaAprobacion.cadenaEstandar());

        List<SolicitudAcceso> solicitudes = List.of(
                new SolicitudAcceso("SA-001", "mperez", "Carpeta compartida Finanzas", NivelCriticidad.BAJO, 15, "Cierre mensual"),
                new SolicitudAcceso("SA-002", "jlopez", "CRM comercial", NivelCriticidad.MEDIO, 45, "Reemplazo por vacaciones"),
                new SolicitudAcceso("SA-003", "agarcia", "Consola EDR", NivelCriticidad.ALTO, 20, "Investigación de incidente"),
                new SolicitudAcceso("SA-004", "rmora", "Servidor de producción", NivelCriticidad.CRITICO, 5, "Corrección urgente"),
                new SolicitudAcceso("SA-005", "rmora", "Servidor de producción", NivelCriticidad.CRITICO, 30, "Soporte extendido"),
                new SolicitudAcceso("SA-006", "", "VPN", NivelCriticidad.BAJO, 10, "Teletrabajo"));

        int aprobadas = 0;
        for (SolicitudAcceso s : solicitudes) {
            ResultadoAprobacion r = mesa.tramitar(s);
            if (r.aprobada()) {
                aprobadas++;
            }
            imprimirSolicitud(s, r);
        }

        System.out.println();
        Pantalla.seccion("Resumen");
        System.out.println("  " + Pantalla.estilo(aprobadas + " aprobadas", Pantalla.NEGRITA, Pantalla.VERDE)
                + Pantalla.estilo("  ·  ", Pantalla.GRIS)
                + Pantalla.estilo((solicitudes.size() - aprobadas) + " rechazadas", Pantalla.NEGRITA, Pantalla.ROJO)
                + Pantalla.estilo("  de " + solicitudes.size() + " solicitudes", Pantalla.GRIS));
    }

    private static void imprimirSolicitud(SolicitudAcceso s, ResultadoAprobacion r) {
        String nivel = s.getNivel() + " · " + s.getDiasDuracion() + " días";
        Pantalla.abrirTarjeta(s.getId(), Pantalla.estilo(nivel, Pantalla.NEGRITA, colorNivel(s.getNivel())), nivel.length());

        String solicitante = s.getSolicitante() == null || s.getSolicitante().isBlank()
                ? Pantalla.estilo("(sin solicitante)", Pantalla.ROJO)
                : s.getSolicitante();
        Pantalla.campoTarjeta("Solicitante", solicitante);
        Pantalla.campoTarjeta("Recurso", s.getRecurso());
        Pantalla.campoTarjeta("Justificación", Pantalla.estilo(s.getJustificacion(), Pantalla.TENUE));

        List<String> recorrido = r.recorrido();
        StringBuilder camino = new StringBuilder();
        for (int i = 0; i < recorrido.size(); i++) {
            boolean ultimo = i == recorrido.size() - 1;
            if (i > 0) {
                camino.append(Pantalla.estilo(" » ", Pantalla.GRIS));
            }
            camino.append(ultimo
                    ? Pantalla.estilo(recorrido.get(i), Pantalla.NEGRITA, r.aprobada() ? Pantalla.VERDE : Pantalla.ROJO)
                    : Pantalla.estilo(recorrido.get(i), Pantalla.TENUE));
        }
        Pantalla.campoTarjeta("Recorrido", camino.toString());

        String decision = r.aprobada()
                ? Pantalla.insignia("APROBADA", Pantalla.FONDO_VERDE)
                : Pantalla.insignia("RECHAZADA", Pantalla.FONDO_ROJO);
        Pantalla.campoTarjeta("Decisión", decision + " " + Pantalla.estilo("por " + r.resueltoPor(), Pantalla.NEGRITA));
        Pantalla.campoTarjeta("", Pantalla.estilo(r.motivo(), Pantalla.TENUE));
        Pantalla.cerrarTarjeta();
    }

    private static String colorNivel(NivelCriticidad nivel) {
        return switch (nivel) {
            case BAJO -> Pantalla.VERDE;
            case MEDIO -> Pantalla.CIAN;
            case ALTO -> Pantalla.AMARILLO;
            case CRITICO -> Pantalla.ROJO;
        };
    }

    private static void demoCommand() {
        Pantalla.titulo("PATRÓN 02", "Command",
                "Operaciones administrativas con deshacer, rehacer y auditoría", Pantalla.MAGENTA);
        DirectorioUsuarios directorio = new DirectorioUsuarios();
        ConsolaAdministracion consola = new ConsolaAdministracion("cnaranjo");

        String upn = "lvera@empresa.ec";
        Pantalla.seccion("Operaciones");

        paso(consola, directorio, upn, "Alta del colaborador", consola.ejecutar(new AltaColaboradorCommand(
                "Alta de colaborador " + upn, List.of(
                        new CrearUsuarioCommand(directorio, upn, "Lucía Vera"),
                        new AsignarLicenciaCommand(directorio, upn, "M365_E3"),
                        new AsignarLicenciaCommand(directorio, upn, "EDR_SENSOR")))));
        paso(consola, directorio, upn, "Bloquear la cuenta",
                consola.ejecutar(new BloquearCuentaCommand(directorio, upn, "Actividad sospechosa")));
        paso(consola, directorio, upn, "Deshacer", consola.deshacer());
        paso(consola, directorio, upn, "Rehacer", consola.rehacer());
        paso(consola, directorio, upn, "Crear cuenta duplicada",
                consola.ejecutar(new CrearUsuarioCommand(directorio, upn, "Duplicado")));
        paso(consola, directorio, upn, "Deshacer", consola.deshacer());
        paso(consola, directorio, upn, "Deshacer", consola.deshacer());

        System.out.println();
        Pantalla.seccion("Bitácora de auditoría");
        List<String> bitacora = consola.getBitacora();
        for (int i = 0; i < bitacora.size(); i++) {
            System.out.println("  " + Pantalla.estilo(String.format("%2d", i + 1), Pantalla.GRIS) + "  " + colorearBitacora(bitacora.get(i)));
        }
    }

    private static int numeroPaso = 0;

    /** Muestra el resultado de una operación de la consola y el estado de la cuenta tras ella. */
    private static void paso(ConsolaAdministracion consola, DirectorioUsuarios directorio, String upn,
                             String titulo, boolean exito) {
        numeroPaso++;
        String resultado = exito
                ? Pantalla.estilo("OK", Pantalla.NEGRITA, Pantalla.VERDE)
                : Pantalla.estilo("FALLÓ", Pantalla.NEGRITA, Pantalla.ROJO);
        System.out.println();
        System.out.println("  " + Pantalla.estilo(numeroPaso + ".", Pantalla.GRIS) + " "
                + Pantalla.estilo(Pantalla.rellenar(titulo, 26), Pantalla.NEGRITA) + resultado);

        List<String> bitacora = consola.getBitacora();
        String ultima = bitacora.isEmpty() ? "" : bitacora.get(bitacora.size() - 1);
        int flecha = ultima.indexOf(" → ");
        if (!exito && flecha >= 0) {
            System.out.println("     " + Pantalla.estilo(ultima.substring(flecha + 3), Pantalla.ROJO));
        }

        System.out.println("     " + Pantalla.estilo("cuenta ", Pantalla.GRIS) + describirCuenta(directorio, upn));
    }

    private static String describirCuenta(DirectorioUsuarios directorio, String upn) {
        return directorio.buscar(upn)
                .map(c -> Pantalla.estilo(c.getUpn(), Pantalla.NEGRITA)
                        + Pantalla.estilo(" (" + c.getNombre() + ")  ", Pantalla.GRIS)
                        + (c.isBloqueada()
                            ? Pantalla.insignia("BLOQUEADA", Pantalla.FONDO_ROJO)
                            : Pantalla.insignia("ACTIVA", Pantalla.FONDO_VERDE))
                        + "  " + Pantalla.estilo(c.getLicencias().isEmpty()
                            ? "sin licencias"
                            : String.join(", ", c.getLicencias()), Pantalla.CIAN))
                .orElse(Pantalla.estilo(upn + " no existe", Pantalla.TENUE));
    }

    /** Resalta la acción de una línea con formato {@code [operador] ACCION   descripción}. */
    private static String colorearBitacora(String linea) {
        int fin = linea.indexOf("] ");
        if (fin < 0 || linea.length() < fin + 10) {
            return linea;
        }
        String operador = linea.substring(0, fin + 1);
        String accion = linea.substring(fin + 2, fin + 10);
        String resto = linea.substring(fin + 10).stripLeading();
        String color = switch (accion.trim()) {
            case "EJECUTAR" -> Pantalla.FONDO_AZUL;
            case "DESHACER" -> Pantalla.FONDO_AMARILLO;
            case "REHACER" -> Pantalla.FONDO_VERDE;
            default -> Pantalla.FONDO_ROJO;
        };
        return Pantalla.estilo(operador, Pantalla.GRIS) + " " + Pantalla.insignia(accion, color) + " " + resto;
    }
}
