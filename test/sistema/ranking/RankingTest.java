package sistema.ranking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.ranking.Ranking;

public class RankingTest {

	@Test
	public void testCrearRankingValido() throws RangoValoracionInvalidoException {
		Ranking ranking = new Ranking(5, new Categoria("Limpieza"), "Buen servicio");
		assertEquals(5, ranking.getValoracion());
		assertEquals(new Categoria("Limpieza"), ranking.getCategoria());
		assertEquals("Buen servicio", ranking.getComentario());

	}

	@Test
	public void testValoracionInvalidaMuyBaja() {
		assertThrows(RangoValoracionInvalidoException.class, () -> {
			new Ranking(0, new Categoria("Limpieza"), "Comentario");
		});
	}

	@Test
	public void testValoracionInvalidaMuyAlta() {
		assertThrows(RangoValoracionInvalidoException.class, () -> {
			new Ranking(6, new Categoria("Servicio"), "Comentario");
		});
	}
}
