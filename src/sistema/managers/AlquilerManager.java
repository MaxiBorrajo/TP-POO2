package sistema.managers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.exceptions.AlquilerNoRegistradoException;
import sistema.exceptions.InmuebleConAlquilerYaExiste;
import sistema.filtro.FiltroDeSistema;

public class AlquilerManager {
	private List<Alquiler> alquileres;

	public AlquilerManager() {
		this.alquileres = new ArrayList<Alquiler>();
	}
	
	public void validarAlquiler (Alquiler alquiler) throws AlquilerNoRegistradoException {
		if (!this.existeAlquiler(alquiler)) {
			throw new AlquilerNoRegistradoException();
		}
	}
	
	public boolean existeAlquiler(Alquiler alquiler) {
		return this.alquileres.contains(alquiler);
	}
	public Alquiler darDeAltaAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault) throws InmuebleConAlquilerYaExiste {
		boolean inmuebleYaTieneAlquiler = this.alquileres.stream()
				.anyMatch(alquiler -> alquiler.getInmueble().equals(inmueble));
		
		if (inmuebleYaTieneAlquiler) {
			throw new InmuebleConAlquilerYaExiste("El inmueble ya tiene un alquiler asociado.");
		}
		Alquiler alquiler = new Alquiler(inmueble, checkIn, checkOut, precioDefault);
		this.alquileres.add(alquiler);
		return alquiler;
	}

	public Alquiler obtenerInformacionAlquiler(Alquiler alquiler) {
		return alquiler;
	}

	public List<Alquiler> getAlquileres() {
		return this.alquileres;
	}
	
	public List<Alquiler> filtrarAlquiler(FiltroDeSistema filtro){
		return filtro.filtrarLista(this.alquileres);
	}

//	
//}
//
//public List<Alquiler> filtrarAlquileres(Filtro filtro){
//	this.validarFiltro(filtro);
//	return filtro.filtrar(this.alquileres.stream().filter(x->x.estaDisponible()));
//}
//
//public void validarFiltro(Filtro filtro) {
//	if(!filtro.esFiltroValido()) {
//		throw new Error("Filtro no valido");
//	}
//}
}
