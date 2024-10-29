package sistema.filtro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import  sistema.alquiler.*;

public class FiltroBuilder {
	
	public FiltroDeSistema filtroDelSistema(String ciudad, LocalDate entrada, LocalDate salida){
		List<Filtro<Alquiler>> filtros = new ArrayList<Filtro<Alquiler>>(
				Arrays.asList(this.filtroDeCiudad(ciudad) , this.filtroPorInicio(entrada), this.filtroPorFin(salida)));
		
		return new FiltroDeSistema(filtros);
	}
	
	private Filtro<Alquiler> filtroDeCiudad(String ciudad){
		return new FiltroSimple<Alquiler>((i -> i.esDeCiudad(ciudad) ));
	}
	
	private Filtro<Alquiler> filtroPorInicio(LocalDate inicio){
		return new FiltroSimple<Alquiler>(a -> a.esDepuesDe(inicio));
	}
	
	private Filtro<Alquiler> filtroPorFin(LocalDate fin){
		return new FiltroSimple<Alquiler>(a -> a.esAntesDe(fin));
	}
	
	public Filtro<Alquiler> filtroCantHuespedes(int cant){
		return new FiltroSimple<Alquiler>(a -> a.aceptaCantidadHuespedes(cant));
	}
	
	public Filtro<Alquiler> filtroPorPrecioMinimo(double  precio){
		return new FiltroSimple<Alquiler>(a -> a.precioMinimoMenorA(precio));
	}
	
	public Filtro<Alquiler> filtroPorPrecioMayor(double precio){
		return new FiltroSimple<Alquiler>(a -> a.precioMaximoMenorA(precio));
	}
	
}
