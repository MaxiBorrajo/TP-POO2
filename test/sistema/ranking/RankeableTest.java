package sistema.ranking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.time.LocalDate;
import java.util.List;

public class RankeableTest {
    private Rankeable rankeable;
    private Usuario mockUsuario;
    private Reserva mockReserva;

    @BeforeEach
    public void setUp() {
        rankeable = new RankeableTestClass();
        mockUsuario = mock(Usuario.class);
        mockReserva = mock(Reserva.class);
    }

    @Test
    public void testGetValoraciones() throws RangoValoracionInvalidoException, ServicioNoTerminadoException, ValoracionInvalidaException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(true);

        Ranking ranking = new Ranking(4, new Categoria("Servicio"), "Muy buen servicio");
        rankeable.añadirValoracion(ranking, mockUsuario, mockReserva);

        List<Ranking> valoraciones = rankeable.getValoraciones();
        assertEquals(1, valoraciones.size());
        assertEquals("Muy buen servicio", valoraciones.get(0).getComentario());
    }

    @Test
    public void testGetValoracionesPorCategoria() throws RangoValoracionInvalidoException, ServicioNoTerminadoException, ValoracionInvalidaException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(true);

        rankeable.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Excelente limpieza"), mockUsuario, mockReserva);
        rankeable.añadirValoracion(new Ranking(4, new Categoria("Servicio"), "Buen servicio"), mockUsuario, mockReserva);

        List<Ranking> valoracionesLimpieza = rankeable.getValoracionesPorCategoria(new Categoria("Limpieza"));
        assertEquals(1, valoracionesLimpieza.size());
        assertEquals("Excelente limpieza", valoracionesLimpieza.get(0).getComentario());
    }

    @Test
    public void testGetPromedioValoraciones() throws RangoValoracionInvalidoException, ServicioNoTerminadoException, ValoracionInvalidaException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(true);

        rankeable.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Excelente limpieza"), mockUsuario, mockReserva);
        rankeable.añadirValoracion(new Ranking(3, new Categoria("Servicio"), "Servicio regular"), mockUsuario, mockReserva);

        assertEquals(4.0, rankeable.getPromedioValoraciones());
    }

    @Test
    public void testGetPromedioValoracionesPorCategoria() throws RangoValoracionInvalidoException, ServicioNoTerminadoException, ValoracionInvalidaException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(true);

        rankeable.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Muy limpio"), mockUsuario, mockReserva);
        rankeable.añadirValoracion(new Ranking(4, new Categoria("Limpieza"), "Buena limpieza"), mockUsuario, mockReserva);

        assertEquals(4.5, rankeable.getPromedioValoracionesPorCategoria(new Categoria("Limpieza")));
    }

    @Test
    public void testGetComentarios() throws RangoValoracionInvalidoException, ServicioNoTerminadoException, ValoracionInvalidaException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(true);

        rankeable.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Comentario 1"), mockUsuario, mockReserva);
        rankeable.añadirValoracion(new Ranking(4, new Categoria("Servicio"), "Comentario 2"), mockUsuario, mockReserva);

        List<String> comentarios = rankeable.getComentarios();
        assertEquals(2, comentarios.size());
        assertTrue(comentarios.contains("Comentario 1"));
        assertTrue(comentarios.contains("Comentario 2"));
    }

    @Test
    public void testAñadirValoracionSinServicioTerminado() throws RangoValoracionInvalidoException {
        when(mockReserva.fechaPosteriorAFinal(any(LocalDate.class))).thenReturn(false);

        Ranking ranking = new Ranking(5, new Categoria("Servicio"), "No permitido");
        assertThrows(ServicioNoTerminadoException.class, () -> {
            rankeable.añadirValoracion(ranking, mockUsuario, mockReserva);
        });
    }
}
