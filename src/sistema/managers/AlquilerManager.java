package sistema.managers;

import java.time.LocalTime;
import java.util.List;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.exceptions.InmuebleConAlquilerYaExiste;

public class AlquilerManager {
	private List<Alquiler> alquileres;
	private int siguienteId;

	public void darDeAltaAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault) throws InmuebleConAlquilerYaExiste {
		boolean inmuebleYaTieneAlquiler = this.alquileres.stream()
				.anyMatch(alquiler -> alquiler.getInmueble().equals(inmueble));

		if (inmuebleYaTieneAlquiler) {
			throw new InmuebleConAlquilerYaExiste("El inmueble ya tiene un alquiler asociado.");
		}
		Alquiler alquiler = new Alquiler(inmueble, checkIn, checkOut, precioDefault, siguienteId);
		this.alquileres.add(alquiler);
		this.siguienteId += 1;
	}

	public Alquiler obtenerInformacionAlquiler(Alquiler alquiler) {
		return alquiler;
	}

	public List<Alquiler> getAlquileres() {
		return this.alquileres;
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
