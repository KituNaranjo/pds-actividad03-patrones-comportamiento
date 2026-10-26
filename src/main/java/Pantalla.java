/**
 * Utilidades de presentación para la demo en consola: colores ANSI, títulos
 * enmarcados, tarjetas y etiquetas. Solo usa caracteres disponibles también en
 * las páginas de código de la consola de Windows (cp437/cp850).
 * <p>
 * Los colores se desactivan definiendo la variable de entorno {@code NO_COLOR}.
 */
final class Pantalla {

    static final int ANCHO = 78;

    private static final boolean COLOR = System.getenv("NO_COLOR") == null;

    static final String RESET = "\u001B[0m";
    static final String NEGRITA = "\u001B[1m";
    static final String TENUE = "\u001B[2m";
    static final String ROJO = "\u001B[31m";
    static final String VERDE = "\u001B[32m";
    static final String AMARILLO = "\u001B[33m";
    static final String AZUL = "\u001B[34m";
    static final String MAGENTA = "\u001B[35m";
    static final String CIAN = "\u001B[36m";
    static final String GRIS = "\u001B[90m";
    static final String FONDO_VERDE = "\u001B[42;30m";
    static final String FONDO_ROJO = "\u001B[41;97m";
    static final String FONDO_AZUL = "\u001B[44;97m";
    static final String FONDO_AMARILLO = "\u001B[43;30m";

    private Pantalla() {
    }

    static String estilo(String texto, String... estilos) {
        if (!COLOR || estilos.length == 0) {
            return texto;
        }
        return String.join("", estilos) + texto + RESET;
    }

    static void titulo(String etiqueta, String patron, String subtitulo, String color) {
        String borde = "═".repeat(ANCHO - 2);
        System.out.println(estilo("╔" + borde + "╗", color));
        System.out.println(estilo("║", color) + estilo(rellenar("  " + etiqueta + " · " + patron.toUpperCase(), ANCHO - 2), NEGRITA, color) + estilo("║", color));
        System.out.println(estilo("║", color) + estilo(rellenar("  " + subtitulo, ANCHO - 2), TENUE) + estilo("║", color));
        System.out.println(estilo("╚" + borde + "╝", color));
        System.out.println();
    }

    static void seccion(String texto) {
        String cabecera = "── " + texto + " ";
        System.out.println(estilo(cabecera, NEGRITA) + estilo("─".repeat(Math.max(0, ANCHO - cabecera.length())), GRIS));
    }

    static void abrirTarjeta(String titulo, String detalle, int largoDetalle) {
        String inicio = "┌─ " + titulo + " ";
        int relleno = Math.max(1, ANCHO - inicio.length() - largoDetalle - 1);
        System.out.println(estilo("┌─ ", GRIS) + estilo(titulo, NEGRITA) + " "
                + estilo("─".repeat(relleno), GRIS) + " " + detalle);
    }

    static void lineaTarjeta(String contenido) {
        System.out.println(estilo("│ ", GRIS) + contenido);
    }

    static void campoTarjeta(String etiqueta, String valor) {
        lineaTarjeta(estilo(rellenar(etiqueta, 14), GRIS) + valor);
    }

    static void cerrarTarjeta() {
        System.out.println(estilo("└" + "─".repeat(ANCHO - 1), GRIS));
    }

    /** Etiqueta con fondo de color, p. ej. {@code  APROBADA }. */
    static String insignia(String texto, String fondo) {
        return estilo(" " + texto + " ", NEGRITA, fondo);
    }

    static String rellenar(String texto, int ancho) {
        return texto.length() >= ancho ? texto : texto + " ".repeat(ancho - texto.length());
    }
}
