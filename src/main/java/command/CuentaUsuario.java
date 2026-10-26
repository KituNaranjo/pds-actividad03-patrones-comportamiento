package command;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Cuenta de un colaborador dentro del directorio corporativo.
 */
public class CuentaUsuario {

    private final String upn;
    private final String nombre;
    private boolean bloqueada;
    private final Set<String> licencias = new LinkedHashSet<>();

    public CuentaUsuario(String upn, String nombre) {
        this.upn = upn;
        this.nombre = nombre;
    }

    public String getUpn() { return upn; }
    public String getNombre() { return nombre; }
    public boolean isBloqueada() { return bloqueada; }

    void setBloqueada(boolean bloqueada) { this.bloqueada = bloqueada; }

    boolean agregarLicencia(String sku) { return licencias.add(sku); }
    boolean quitarLicencia(String sku) { return licencias.remove(sku); }

    public Set<String> getLicencias() {
        return Collections.unmodifiableSet(licencias);
    }

    @Override
    public String toString() {
        return upn + " (" + nombre + ")" + (bloqueada ? " [BLOQUEADA]" : "") + " licencias=" + licencias;
    }
}
