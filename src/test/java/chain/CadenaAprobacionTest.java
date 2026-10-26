package chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CadenaAprobacionTest {

    private MesaServicioAccesos mesa;

    @BeforeEach
    void setUp() {
        mesa = new MesaServicioAccesos(ConfiguradorCadenaAprobacion.cadenaEstandar());
    }

    private static SolicitudAcceso solicitud(NivelCriticidad nivel, int dias) {
        return new SolicitudAcceso("SA-T", "usuario", "Recurso", nivel, dias, "Justificación de negocio");
    }

    @Test
    @DisplayName("Una solicitud sin justificación se rechaza en el primer eslabón")
    void rechazaSolicitudIncompleta() {
        SolicitudAcceso s = new SolicitudAcceso("SA-1", "usuario", "VPN", NivelCriticidad.BAJO, 10, "  ");
        ResultadoAprobacion r = mesa.tramitar(s);
        assertFalse(r.aprobada());
        assertEquals("Validación automática", r.resueltoPor());
        assertEquals(List.of("Validación automática"), r.recorrido());
    }

    @Test
    @DisplayName("Una duración fuera de política se rechaza en la validación")
    void rechazaDuracionFueraDePolitica() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.BAJO, 120));
        assertFalse(r.aprobada());
        assertEquals("Validación automática", r.resueltoPor());
    }

    @Test
    @DisplayName("Acceso BAJO de corta duración lo aprueba Soporte N1")
    void bajoLoApruebaSoporte() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.BAJO, 15));
        assertTrue(r.aprobada());
        assertEquals("Soporte N1", r.resueltoPor());
    }

    @Test
    @DisplayName("Acceso BAJO de larga duración escala al Jefe de área")
    void bajoLargoEscalaAJefe() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.BAJO, 45));
        assertTrue(r.aprobada());
        assertEquals("Jefe de área", r.resueltoPor());
        assertEquals(List.of("Validación automática", "Soporte N1", "Jefe de área"), r.recorrido());
    }

    @Test
    @DisplayName("Acceso MEDIO lo aprueba el Jefe de área")
    void medioLoApruebaJefe() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.MEDIO, 30));
        assertTrue(r.aprobada());
        assertEquals("Jefe de área", r.resueltoPor());
    }

    @Test
    @DisplayName("Acceso ALTO lo aprueba Seguridad de la Información")
    void altoLoApruebaSeguridad() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.ALTO, 20));
        assertTrue(r.aprobada());
        assertEquals("Seguridad de la Información", r.resueltoPor());
        assertEquals(4, r.recorrido().size());
    }

    @Test
    @DisplayName("Acceso CRÍTICO de emergencia lo aprueba el Comité")
    void criticoCortoLoApruebaComite() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.CRITICO, 5));
        assertTrue(r.aprobada());
        assertEquals("Comité de cambios", r.resueltoPor());
        assertEquals(5, r.recorrido().size());
    }

    @Test
    @DisplayName("Acceso CRÍTICO prolongado lo rechaza el Comité")
    void criticoLargoLoRechazaComite() {
        ResultadoAprobacion r = mesa.tramitar(solicitud(NivelCriticidad.CRITICO, 30));
        assertFalse(r.aprobada());
        assertEquals("Comité de cambios", r.resueltoPor());
    }

    @Test
    @DisplayName("Si ningún eslabón puede resolver, la cadena rechaza al final")
    void cadenaSinResolutorRechaza() {
        AprobadorHandler cadenaCorta = new SoporteNivel1Handler();
        cadenaCorta.setSiguiente(new JefeAreaHandler());
        ResultadoAprobacion r = new MesaServicioAccesos(cadenaCorta).tramitar(solicitud(NivelCriticidad.ALTO, 10));
        assertFalse(r.aprobada());
        assertEquals("Jefe de área", r.resueltoPor());
    }

    @Test
    @DisplayName("Agregar un eslabón no exige cambiar a los existentes ni al cliente")
    void nuevoEslabonSeIntegraSinCambios() {
        AprobadorHandler propietarioDatos = new AprobadorHandler() {
            @Override
            protected boolean puedeResolver(SolicitudAcceso s) {
                return s.getRecurso().contains("Datos de clientes");
            }

            @Override
            protected ResultadoAprobacion resolver(SolicitudAcceso s) {
                return ResultadoAprobacion.rechazada(getNombre(), "Requiere acuerdo de confidencialidad", s);
            }

            @Override
            public String getNombre() {
                return "Propietario de datos";
            }
        };
        AprobadorHandler cadena = new ValidacionSolicitudHandler(90);
        cadena.setSiguiente(propietarioDatos).setSiguiente(new SoporteNivel1Handler());

        SolicitudAcceso s = new SolicitudAcceso("SA-9", "usuario", "Datos de clientes", NivelCriticidad.BAJO, 5, "Reporte");
        ResultadoAprobacion r = new MesaServicioAccesos(cadena).tramitar(s);
        assertFalse(r.aprobada());
        assertEquals("Propietario de datos", r.resueltoPor());
    }
}
