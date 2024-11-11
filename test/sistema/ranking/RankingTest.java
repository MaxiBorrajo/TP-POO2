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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class RankingTest {

    private Reserva reserva;
    private Rankeable rankeable;
    private Usuario ranker;
    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        ranker = mock(Usuario.class);
        rankeable = mock(Rankeable.class);
        reserva = mock(Reserva.class);
        categoria = new Categoria("Limpieza");
    }

    @Test
    public void testCrearRankingValido() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(5, categoria, "Buen servicio", reserva, rankeable, ranker);

        assertEquals(5, ranking.getValoracion());
        assertEquals(categoria, ranking.getCategoria());
        assertEquals("Buen servicio", ranking.getComentario());
        assertEquals(reserva, ranking.getReserva());
        assertEquals(rankeable, ranking.getRankeable());
        assertEquals(ranker, ranking.getRanker());
    }

    @Test
    public void testValoracionInvalidaMuyBaja() {
        assertThrows(RangoValoracionInvalidoException.class, () -> {
            new Ranking(0, categoria, "Comentario", reserva, rankeable, ranker);
        });
    }

    @Test
    public void testValoracionInvalidaMuyAlta() {
        assertThrows(RangoValoracionInvalidoException.class, () -> {
            new Ranking(6, categoria, "Comentario", reserva, rankeable, ranker);
        });
    }

    @Test
    public void testGetReserva() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(4, categoria, "Buen servicio", reserva, rankeable, ranker);
        assertEquals(reserva, ranking.getReserva());
    }

    @Test
    public void testGetRankeable() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(4, categoria, "Buen servicio", reserva, rankeable, ranker);
        assertEquals(rankeable, ranking.getRankeable());
    }

    @Test
    public void testGetRanker() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(4, categoria, "Buen servicio", reserva, rankeable, ranker);
        assertEquals(ranker, ranking.getRanker());
    }
}
