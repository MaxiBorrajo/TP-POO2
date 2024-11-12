package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.exceptions.ReservaNoTerminadaException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

import java.util.List;

public class RankingManagerTest {
	private RankingManager rankingManager;
	private Usuario usuario;
	private Rankeable rankeable;
	private Reserva reserva;

	@BeforeEach
	public void setUp() {
		rankingManager = new RankingManager();
		usuario = mock(Usuario.class);
		rankeable = mock(Inmueble.class);
		reserva = mock(Reserva.class);

		when(reserva.estaFinalizada()).thenReturn(true);
		when(rankeable.mePuedeValorar(usuario)).thenReturn(true);
	}

	@Test
	public void testAñadirValoracion()
			throws RangoValoracionInvalidoException, ValoracionInvalidaException, ReservaNoTerminadaException {
		Ranking ranking = new Ranking(4, new Categoria("Servicio"), "Muy buen servicio", reserva, rankeable, usuario);
		rankingManager.añadirValoracion(ranking);
		assertEquals(1, rankingManager.getValoraciones(rankeable).size());
	}

	@Test
	public void testNoSePuedeAñadirValoracionPorPermisoInvalido() throws RangoValoracionInvalidoException {
		when(rankeable.mePuedeValorar(usuario)).thenReturn(false);

		Ranking ranking = new Ranking(4, new Categoria("Servicio"), "Muy buen servicio", reserva, rankeable, usuario);

		assertThrows(ValoracionInvalidaException.class, () -> rankingManager.añadirValoracion(ranking));
	}

	@Test
	public void testGetValoracionesPorCategoria()
			throws RangoValoracionInvalidoException, ValoracionInvalidaException, ReservaNoTerminadaException {
		rankingManager.añadirValoracion(
				new Ranking(5, new Categoria("Limpieza"), "Limpio y ordenado", reserva, rankeable, usuario));
		rankingManager.añadirValoracion(
				new Ranking(3, new Categoria("Servicio"), "Servicio promedio", reserva, rankeable, usuario));
		rankingManager
				.añadirValoracion(new Ranking(1, new Categoria("Limpieza"), "Mugroso", reserva, rankeable, usuario));
		assertEquals(2, rankingManager.getValoracionesPorCategoria(rankeable, new Categoria("Limpieza")).size());
	}

	@Test
	public void testCalcularPromedioValoraciones()
			throws RangoValoracionInvalidoException, ValoracionInvalidaException, ReservaNoTerminadaException {
		rankingManager.añadirValoracion(
				new Ranking(1, new Categoria("Limpieza"), "Limpio y ordenado", reserva, rankeable, usuario));
		rankingManager.añadirValoracion(
				new Ranking(3, new Categoria("Servicio"), "Servicio promedio", reserva, rankeable, usuario));
		assertEquals(2.0, rankingManager.getPromedioValoraciones(rankeable));
	}

	@Test
	public void testCalcularPromedioPorCategoria()
			throws RangoValoracionInvalidoException, ValoracionInvalidaException, ReservaNoTerminadaException {
		rankingManager.añadirValoracion(
				new Ranking(5, new Categoria("Limpieza"), "Excelente limpieza", reserva, rankeable, usuario));
		rankingManager.añadirValoracion(
				new Ranking(4, new Categoria("Limpieza"), "Buena limpieza", reserva, rankeable, usuario));
		assertEquals(4.5, rankingManager.getPromedioValoracionesPorCategoria(rankeable, new Categoria("Limpieza")));
	}

	@Test
	public void testGetComentarios()
			throws RangoValoracionInvalidoException, ValoracionInvalidaException, ReservaNoTerminadaException {
		rankingManager
				.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Muy limpio", reserva, rankeable, usuario));
		rankingManager.añadirValoracion(
				new Ranking(4, new Categoria("Servicio"), "Buen servicio", reserva, rankeable, usuario));
		List<String> comentarios = rankingManager.getComentarios(rankeable);
		assertEquals(2, comentarios.size());
		assertTrue(comentarios.contains("Muy limpio"));
		assertTrue(comentarios.contains("Buen servicio"));
	}
	 @Test
	    public void testAñadirValoracionThrowsReservaNoTerminadaException() {
		 	when(reserva.estaFinalizada()).thenReturn(false);
	        Ranking rankingMock = mock(Ranking.class);
	        when(rankingMock.getReserva()).thenReturn(reserva);
	        assertThrows(ReservaNoTerminadaException.class, () -> {
	            rankingManager.añadirValoracion(rankingMock);
	        });

	        verify(reserva).estaFinalizada();
	    }
}
