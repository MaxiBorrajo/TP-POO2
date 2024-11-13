package sistema.filtro;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import sistema.alquiler.Alquiler;

//La funcion de este clase es para obligar los tipos anden en el sistem para el filtro obligatorio
public class FiltroDeSistema {

	private FiltroCompuesto<Alquiler> filtroSistema;
	private LocalDate entrada;
	private LocalDate salida;

	public FiltroDeSistema(String ciudad, LocalDate entrada, LocalDate salida) {
		this.entrada = entrada;
		this.salida = salida;
		this.filtroSistema = new FiltroCompuesto<Alquiler>();
		this.filtroSistema.agregarFiltro(this.filtroPorCiudad(ciudad));
		this.filtroSistema.agregarFiltro(this.filtroCompuestoFechas(entrada, salida));
	}

	private Filtro<Alquiler> filtroPorCiudad(String ciudad) {
		return new FiltroSimple<Alquiler>((a -> a.esDeCiudad(ciudad)));
	}

	private Filtro<Alquiler> filtroCompuestoFechas(LocalDate entrada, LocalDate salida) {
		
		return new FiltroSimple<Alquiler>(a -> a.puedeCrearReserva(entrada, salida));
	}

	private void agregarFiltro(Filtro<Alquiler> filtro) {
		this.filtroSistema.agregarFiltro(filtro);
	}

	public void agregarFiltroPorPrecio(double min, double max) {
		this.agregarFiltro(
				new FiltroSimple<Alquiler>(a -> a.cumplePrecioEnPeriodo(min, max, this.entrada, this.salida)));
	}

	public void agregarFiltroPorHuespedes(int cant) {
		this.agregarFiltro(new FiltroSimple<Alquiler>(a -> a.aceptaCantidadHuespedes(cant)));
	}

	public List<Alquiler> filtrarLista(List<Alquiler> lista) {
		return this.filtroSistema.filtrarLista(lista);
	}

}
