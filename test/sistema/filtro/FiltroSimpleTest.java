package sistema.filtro;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiltroSimpleTest {

	private Filtro<Integer> filtro;

	@BeforeEach
	void setUp() throws Exception {
		this.filtro = new FiltroSimple<Integer>((n -> n % 2 == 0));
	}

	@Test
	void testFiltra4Resultado() {
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3));

		assertEquals(1, this.filtro.filtrarLista(list).size());
	}

}
