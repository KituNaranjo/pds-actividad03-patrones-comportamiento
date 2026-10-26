package command;

import java.util.ArrayList;
import java.util.List;

/**
 * Participante GoF: <b>ConcreteCommand</b> compuesto (macro-comando).
 * <p>
 * Agrupa los pasos del alta de un colaborador. Se ejecuta como una unidad:
 * si un paso falla, revierte los que ya se ejecutaron; al deshacer,
 * revierte todos en orden inverso.
 */
public class AltaColaboradorCommand implements OperacionAdministrativa {

    private final String descripcion;
    private final List<OperacionAdministrativa> pasos;
    private final List<OperacionAdministrativa> ejecutados = new ArrayList<>();

    public AltaColaboradorCommand(String descripcion, List<OperacionAdministrativa> pasos) {
        this.descripcion = descripcion;
        this.pasos = List.copyOf(pasos);
    }

    @Override
    public void ejecutar() {
        ejecutados.clear();
        for (OperacionAdministrativa paso : pasos) {
            try {
                paso.ejecutar();
                ejecutados.add(paso);
            } catch (RuntimeException e) {
                deshacer();
                throw e;
            }
        }
    }

    @Override
    public void deshacer() {
        for (int i = ejecutados.size() - 1; i >= 0; i--) {
            ejecutados.get(i).deshacer();
        }
        ejecutados.clear();
    }

    @Override
    public String getDescripcion() {
        return descripcion + " (" + pasos.size() + " pasos)";
    }
}
