package sistema.Inmueble;
import sistema.enums.customEnums.Servicio;
import sistema.enums.customEnums.TipoDeInmueble;

import java.util.List;
;

public class Inmueble {
	private int id;
	private int superficie;
	private TipoDeInmueble tipo;
	private Ubicacion ubicacion;
	private List<Servicio> servicios;
	private int capacidad;

	public Inmueble(int superficie, 
			TipoDeInmueble tipo, 
			Ubicacion ubi,
			List<Servicio> servicios,
			int capacidad) {
		this.superficie =  superficie;
		this.tipo = tipo;
		this.ubicacion = ubi;
		this.servicios = servicios;
		this.capacidad = capacidad;
	}

	public boolean esDeCiudad(String ciudad) {
		// TODO Auto-generated method stub
		return this.ubicacion.estaEnCiudad(ciudad);
	}

	public int getCapacidad() {
		// TODO Auto-generated method stub
		return this.capacidad;
	}

	public boolean sonElMismoInmueble(Inmueble inm2) {
		// TODO Auto-generated method stub
		return this == inm2;
	}
	
	

	public String getTipo() {
		// TODO Auto-generated method stub
		return this.tipo.getNombre();
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.ubicacion.getCiudad();
	}
	
}
