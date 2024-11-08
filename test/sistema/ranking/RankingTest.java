package sistema.ranking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Inmueble;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class RankingTest {

	private Reserva reserva;
    private Rankeable rankeable;
    private Usuario ranker;

    @BeforeEach
    public void setUp() {
    	ranker = mock(Usuario.class);
		rankeable = mock(Inmueble.class);
		reserva = mock(Reserva.class);
    }

    @Test
    public void testCrearRankingValido() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(5, new Categoria("Limpieza"), "Buen servicio", reserva, rankeable, ranker);
        assertEquals(5, ranking.getValoracion());
        assertEquals(new Categoria("Limpieza"), ranking.getCategoria());
        assertEquals("Buen servicio", ranking.getComentario());
    }

    @Test
    public void testValoracionInvalidaMuyBaja() {
        assertThrows(RangoValoracionInvalidoException.class, () -> {
            new Ranking(0, new Categoria("Limpieza"), "Comentario", reserva, rankeable, ranker);
        });
    }

    @Test
    public void testValoracionInvalidaMuyAlta() {
        assertThrows(RangoValoracionInvalidoException.class, () -> {
            new Ranking(6, new Categoria("Servicio"), "Comentario", reserva, rankeable, ranker);
        });
    }
}
