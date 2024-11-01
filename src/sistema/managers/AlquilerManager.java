package sistema.managers;
import java.util.List;
import sistema.alquiler.Alquiler;

public class AlquilerManager {
	private List<Alquiler> alquileres;
	private int siguienteId;
	
	public void darDeAltaAlquiler(Alquiler alquiler) {
		//validar
		alquiler.setId(this.siguienteId);
		this.alquileres.add(alquiler);
		this.siguienteId += 1;
	}
	
	
	public Alquiler obtenerInformacionAlquiler(Alquiler alquiler) {
		return alquiler;
	}
	public List<Alquiler> getAlquileres(){
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
