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
	
}
