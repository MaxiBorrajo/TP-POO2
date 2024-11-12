package sistema.managers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;
import sistema.alquiler.politicaDeCancelacion.PoliticaDeCancelacion;
import sistema.enums.FormaDePago;
import sistema.exceptions.NoExistenteException;
import sistema.exceptions.YaExistenteException;
import sistema.filtro.FiltroDeSistema;
import sistema.filtro.FiltroSimple;
import sistema.usuario.Usuario;

public class AlquilerManager {
	private List<Alquiler> alquileres;

	public AlquilerManager() {
		this.alquileres = new ArrayList<Alquiler>();
	}

	public void validarAlquiler(Alquiler alquiler) throws NoExistenteException {
		if (!this.existeAlquiler(alquiler)) {
			throw new NoExistenteException("Alquiler");
		}
	}

	public boolean existeAlquiler(Alquiler alquiler) {
		return this.alquileres.contains(alquiler);
	}

	public Alquiler darDeAltaAlquiler(Inmueble inmueble, LocalTime checkIn, LocalTime checkOut, double precioDefault,
			PoliticaDeCancelacion politicaDeCancelacion, List<FormaDePago> formasDePago)
			throws YaExistenteException {
		boolean inmuebleYaTieneAlquiler = this.alquileres.stream()
				.anyMatch(alquiler -> alquiler.getInmueble().equals(inmueble));

		if (inmuebleYaTieneAlquiler) {
			throw new YaExistenteException("Alquiler con este inmueble");
		}
 
		Alquiler alquiler = new Alquiler(inmueble, checkIn, checkOut, precioDefault, politicaDeCancelacion,
				formasDePago);
		this.alquileres.add(alquiler);
		return alquiler;
	}

	public List<Alquiler> getAlquileres() {
		return this.alquileres;
	}

	public List<Alquiler> filtrarAlquiler(FiltroDeSistema filtro) {
		return filtro.filtrarLista(this.alquileres);
	}

	public List<Alquiler> getAlquileres(Usuario propietario) {
		// TODO Auto-generated method stub
		return new FiltroSimple<Alquiler>(a -> a.getInmueble().getPropietario().equals(propietario))
				.filtrarLista(alquileres);
	}

}
