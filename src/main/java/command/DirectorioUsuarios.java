package command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Participante GoF: <b>Receiver</b>.
 * <p>
 * Contiene la lógica real sobre las cuentas (crear, licenciar, bloquear).
 * No sabe nada de historiales, deshacer ni auditoría: eso lo resuelven
 * los comandos y el invocador.
 */
public class DirectorioUsuarios {

    private final Map<String, CuentaUsuario> cuentas = new LinkedHashMap<>();

    public void crearUsuario(String upn, String nombre) {
        if (cuentas.containsKey(upn)) {
            throw new IllegalStateException("La cuenta " + upn + " ya existe");
        }
        cuentas.put(upn, new CuentaUsuario(upn, nombre));
    }

    public void eliminarUsuario(String upn) {
        if (cuentas.remove(upn) == null) {
            throw new IllegalStateException("La cuenta " + upn + " no existe");
        }
    }

    /** @return {@code true} si la licencia no estaba asignada y se agregó. */
    public boolean asignarLicencia(String upn, String sku) {
        return obtener(upn).agregarLicencia(sku);
    }

    public boolean revocarLicencia(String upn, String sku) {
        return obtener(upn).quitarLicencia(sku);
    }

    public void bloquearCuenta(String upn) {
        obtener(upn).setBloqueada(true);
    }

    public void desbloquearCuenta(String upn) {
        obtener(upn).setBloqueada(false);
    }

    public boolean existe(String upn) {
        return cuentas.containsKey(upn);
    }

    public boolean estaBloqueada(String upn) {
        return obtener(upn).isBloqueada();
    }

    public Optional<CuentaUsuario> buscar(String upn) {
        return Optional.ofNullable(cuentas.get(upn));
    }

    private CuentaUsuario obtener(String upn) {
        CuentaUsuario cuenta = cuentas.get(upn);
        if (cuenta == null) {
            throw new IllegalStateException("La cuenta " + upn + " no existe");
        }
        return cuenta;
    }
}
