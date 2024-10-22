package tpIntegradorVersionMia.Sistema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tpIntegradorVersionMia.Usuario.Usuario;

public class Sistema {
	private Set<Usuario> registrados;
	private List<Alquiler> alquileres;
	
	public Sistema() {
		this.registrados = new HashSet<Usuario>();
		this.alquileres = new ArrayList<Alquiler>();
	}
	
	public void registrarUsuario(Usuario us) {
		this.registrados.add(us);
	}
	
	public boolean estaRegistrado(Usuario us) {
		return this.registrados.contains(us);
	}
	
	public void darDeAltaAlquiler(Alquiler alq) throws Exception{
		this.validacionesDeAlta(alq);
	}
	
	
	private void validacionesDeAlta(Alquiler alq) throws Exception{
		if(!this.estaRegistrado(alq.propietario)) {
			throw new Error("Propietario no registrado");
		}
		
		
	}
	
	public List<Alquiler> filtrarAlquileres(Filtro filtro){
		this.validarFiltro(filtro);
		return filtro.filtrar(this.alquileres.stream().filter(x->x.estaDisponible()));
	}
	
	public void validarFiltro(Filtro filtro) {
		if(!filtro.esFiltroValido()) {
			throw new Error("Filtro no valido");
		}
	}
}
