package sistema.filtro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import  sistema.alquiler.*;

public class FiltroBuilder {
	
	public FiltroCompuesto<Alquiler> filtroDelSistema(String ciudad, LocalDate entrada, LocalDate salida){
		List<Filtro<Alquiler>> filtros = new ArrayList<Filtro<Alquiler>>(
				Arrays.asList(this.filtroDeCiudad(ciudad) , this.filtroPorPeriodo(entrada, salida)));
		
		return new FiltroCompuesto<Alquiler>(filtros);
	}
	
	public FiltroCompuesto<Alquiler> filtroDelSistema(String ciudad, LocalDate entrada, LocalDate salida, double precioMinimo, double precioMaximo) {
		List<Filtro<Alquiler>> filtros = new ArrayList<Filtro<Alquiler>>(
				Arrays.asList(this.filtroDeCiudad(ciudad) , this.filtroPorPeriodo(entrada, salida) , 
						this.filtroPrecioPorEstePeriodo(precioMinimo,precioMaximo,entrada,salida) ));
		
		return new FiltroCompuesto<Alquiler>(filtros);
	}
	
	
	

	
	
	
	
	
	
	private Filtro<Alquiler> filtroPrecioPorEstePeriodo(double precioMinimo, double precioMaximo, LocalDate entrada,
			LocalDate salida) {
		// TODO Auto-generated method stub
		return new FiltroSimple<Alquiler>((a -> a.cumplePrecioEnPeriodo(precioMinimo, precioMaximo, entrada, salida)));
	}

	

	private Filtro<Alquiler> filtroDeCiudad(String ciudad){
		return new FiltroSimple<Alquiler>((a -> a.esDeCiudad(ciudad) ));
	}
	
	private Filtro<Alquiler> filtroPorPeriodo(LocalDate entrada, LocalDate salida){
		return new FiltroSimple<Alquiler>((a -> a.perteneceAAlgunPeriodo(entrada,salida)));
	}
	
	public Filtro<Alquiler> filtroCantHuespedes(int cant){
		return new FiltroSimple<Alquiler>(a -> a.aceptaCantidadHuespedes(cant));
	}
	

	
}
