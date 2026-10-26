package command;

/**
 * Participante GoF: <b>Command</b>.
 * <p>
 * Encapsula una operación administrativa como objeto para que pueda
 * ejecutarse, registrarse en la bitácora y deshacerse de forma uniforme.
 */
public interface OperacionAdministrativa {

    void ejecutar();

    /** Revierte exactamente el efecto que produjo {@link #ejecutar()}. */
    void deshacer();

    String getDescripcion();
}
