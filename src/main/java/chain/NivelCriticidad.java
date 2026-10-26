package chain;

/**
 * Nivel de criticidad del recurso al que se solicita acceso.
 * Determina qué eslabón de la cadena tiene autoridad para aprobar.
 */
public enum NivelCriticidad {
    /** Recursos de uso general: carpetas compartidas, impresoras, listas de distribución. */
    BAJO,
    /** Recursos del área: aplicaciones departamentales, sitios de SharePoint del equipo. */
    MEDIO,
    /** Recursos sensibles: consolas de administración, datos de clientes. */
    ALTO,
    /** Recursos críticos: producción, cuentas privilegiadas, infraestructura de seguridad. */
    CRITICO
}
