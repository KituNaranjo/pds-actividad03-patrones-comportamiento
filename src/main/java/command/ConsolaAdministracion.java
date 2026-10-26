package command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Participante GoF: <b>Invoker</b>.
 * <p>
 * Ejecuta comandos sin conocer qué hacen. Mantiene el historial para
 * deshacer/rehacer y la bitácora de auditoría de todo lo que se ejecutó.
 */
public class ConsolaAdministracion {

    private final String operador;
    private final Deque<OperacionAdministrativa> historial = new ArrayDeque<>();
    private final Deque<OperacionAdministrativa> rehacer = new ArrayDeque<>();
    private final List<String> bitacora = new ArrayList<>();

    public ConsolaAdministracion(String operador) {
        this.operador = operador;
    }

    /** @return {@code true} si la operación se ejecutó; {@code false} si falló. */
    public boolean ejecutar(OperacionAdministrativa operacion) {
        try {
            operacion.ejecutar();
        } catch (RuntimeException e) {
            registrar("ERROR", operacion, e.getMessage());
            return false;
        }
        historial.push(operacion);
        rehacer.clear();
        registrar("EJECUTAR", operacion, null);
        return true;
    }

    public boolean deshacer() {
        if (historial.isEmpty()) {
            return false;
        }
        OperacionAdministrativa operacion = historial.pop();
        operacion.deshacer();
        rehacer.push(operacion);
        registrar("DESHACER", operacion, null);
        return true;
    }

    public boolean rehacer() {
        if (rehacer.isEmpty()) {
            return false;
        }
        OperacionAdministrativa operacion = rehacer.pop();
        operacion.ejecutar();
        historial.push(operacion);
        registrar("REHACER", operacion, null);
        return true;
    }

    public boolean puedeDeshacer() {
        return !historial.isEmpty();
    }

    public boolean puedeRehacer() {
        return !rehacer.isEmpty();
    }

    public List<String> getBitacora() {
        return Collections.unmodifiableList(bitacora);
    }

    private void registrar(String accion, OperacionAdministrativa operacion, String detalle) {
        String linea = String.format("[%s] %-8s %s", operador, accion, operacion.getDescripcion());
        bitacora.add(detalle == null ? linea : linea + " → " + detalle);
    }
}
