package sistema.filtro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.alquiler.Alquiler;

public class FiltroDeSistemaTest {
	private FiltroDeSistema filtro;
	private Alquiler a1;
	private Alquiler a2;
	private Alquiler a3;

	@BeforeEach
	void setUp() {
		this.a1 = mock(Alquiler.class);
		this.a2 = mock(Alquiler.class);
		this.a3 = mock(Alquiler.class);
		when(a1.esDeCiudad("Quilmes")).thenReturn(true);
		when(a2.esDeCiudad("Quilmes")).thenReturn(false);
		when(a3.esDeCiudad("Quilmes")).thenReturn(true);

		
		when(a1.puedeCrearReserva(LocalDate.of(2024, 6, 2),LocalDate.of(2024, 6, 5))).thenReturn(true);


		when(a3.puedeCrearReserva(LocalDate.of(2024, 6, 2),LocalDate.of(2024, 6, 5))).thenReturn(false);

		

		this.filtro = new FiltroDeSistema("Quilmes", LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 5));
	}

	@Test
	void seFiltraPorObligatorios() {

		assertEquals(1, this.filtro.filtrarLista(Arrays.asList(a1, a2, a3)).size());

	}

	@Test

	void seAgregaFIltroDePrecio() {
		this.filtro.agregarFiltroPorPrecio(100d, 200d);
		when(a1.cumplePrecioEnPeriodo(100d, 200d, LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 5))).thenReturn(true);
		when(a2.cumplePrecioEnPeriodo(100d, 200d, LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 5))).thenReturn(true);

		assertEquals(1, this.filtro.filtrarLista(Arrays.asList(a1, a2, a3)).size());

	}

	@Test

	void seAgregaFiltroDeHuepedes() {
		this.filtro.agregarFiltroPorHuespedes(5);
		when(a1.aceptaCantidadHuespedes(5)).thenReturn(true);
		when(a3.aceptaCantidadHuespedes(5)).thenReturn(false);

		assertEquals(1, this.filtro.filtrarLista(Arrays.asList(a1, a2, a3)).size());

	}
	
	@Test

	void seAgreganTodosLosFiltrosOpcionaless() {
		this.filtro.agregarFiltroPorHuespedes(5);
		this.filtro.agregarFiltroPorPrecio(100d, 200d);
		when(a1.cumplePrecioEnPeriodo(100d, 200d, LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 5))).thenReturn(true);
		when(a2.cumplePrecioEnPeriodo(100d, 200d, LocalDate.of(2024, 6, 2), LocalDate.of(2024, 6, 5))).thenReturn(true);

		when(a1.aceptaCantidadHuespedes(5)).thenReturn(true);
		when(a3.aceptaCantidadHuespedes(5)).thenReturn(false);

		assertEquals(1, this.filtro.filtrarLista(Arrays.asList(a1, a2, a3)).size());

	}

}
