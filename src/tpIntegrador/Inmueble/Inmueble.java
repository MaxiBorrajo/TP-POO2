package tpIntegrador.Inmueble;

import java.util.List;

import tpIntegradorVersionMia.Sistema.Servicio;

public class Inmueble {
	
	private int superficie;
	private TipoDeInmueble tipo;
	private Ubicacion ubi;
	private List<Servicio> servicios;
	private int capacidad;

	public Inmueble(int superficie, 
			TipoDeInmueble tipo, 
			Ubicacion ubi,
			List<Servicio> servicios,
			int capacidad) {
		this.superficie =  superficie;
		this.tipo = tipo;
		this.ubi = ubi;
		this.servicios = servicios;
		this.capacidad = capacidad;
	}
	
}
