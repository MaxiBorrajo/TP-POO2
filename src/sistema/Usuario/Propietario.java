package tpIntegrador.Usuario;

import tpIntegrador.Inmueble.Inmueble;
import tpIntegrador.Sistema.Sistema;

public class Propietario extends Usuario{
	private Inmueble inm;
	public Propietario(String nombre, String mail, String telefono, Inmueble inm) {
		super(nombre, mail, telefono);
		this.inm = inm;
	}
	
	public Inmueble getinmueble() {
		return this.inm;
	}
	
//	public void darDeAltaEn(Sistema sm, Alquiler alq) {
//		sm.darDeAltaAlquiler(alq);
//	}
}
