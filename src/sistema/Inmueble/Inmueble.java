package sistema.Inmueble;
import sistema.enums.Servicio;
import sistema.enums.TipoDeInmueble;

import java.util.List;

import tpIntegradorVersionMia.Sistema.Servicio;

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
	
}
