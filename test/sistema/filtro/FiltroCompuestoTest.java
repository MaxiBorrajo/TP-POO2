package sistema.filtro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FiltroCompuestoTest {
	
	private FiltroCompuesto<Integer> filtro;
	private List<Integer> lista;
	@BeforeEach
	void setUp() {
		this.lista = Arrays.asList(1,2,3);
		this.filtro = new FiltroCompuesto<Integer>();
		
	}
	
	@Test
	void unFiltroCompuestoSinFiltrosDevuelveMismaLista() {
		assertEquals(3, this.filtro.filtrarLista(this.lista).size());
		assertTrue(this.lista.equals(this.filtro.filtrarLista(this.lista)));
	}
	
	@Test
	void aUnFiltroCOmpuestoSeLePuedeAgregarFiltros() {
		Filtro<Integer> f = mock(Filtro.class);
		when(f.cumpleCondicion(1)).thenReturn(true);
		when(f.cumpleCondicion(2)).thenReturn(true);
		when(f.cumpleCondicion(3)).thenReturn(false);
		
		this.filtro.agregarFiltro(f);
		
		assertEquals(2, this.filtro.filtrarLista(this.lista).size());
		
	}
	
	@Test 
	void unFiltroCompuestoSePuedeCrearYaConListaDeFiltros(){
		Filtro<Integer> f = mock(Filtro.class);
		when(f.cumpleCondicion(1)).thenReturn(true);
		when(f.cumpleCondicion(2)).thenReturn(true);
		when(f.cumpleCondicion(3)).thenReturn(false);
		
		Filtro<Integer> g = mock(Filtro.class);
		when(g.cumpleCondicion(1)).thenReturn(false);
		when(g.cumpleCondicion(2)).thenReturn(true);
		when(g.cumpleCondicion(3)).thenReturn(false);
		
		List <Filtro<Integer>> xs = Arrays.asList(f,g);
		
		FiltroCompuesto<Integer> filtroPrueba = new FiltroCompuesto<Integer>(xs);
		
		
		assertEquals(1, filtroPrueba.filtrarLista(this.lista).size());
		
	}
}
