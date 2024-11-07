package sistema.Inmueble;

import java.util.ArrayList;
import java.util.List;

import sistema.enums.customEnums.Servicio;
import sistema.enums.customEnums.TipoDeInmueble;
import sistema.managers.RankingManager;
import sistema.ranking.Rankeable;
import sistema.reserva.Reserva;
import sistema.usuario.Propietario;
import sistema.usuario.Usuario;;

public class Inmueble extends Rankeable {
	private int id;
	private int superficie;
	private TipoDeInmueble tipo;
	private Ubicacion ubicacion;
	private List<Servicio> servicios;
	private int capacidad;
	private List<String> fotos;
	private Propietario propietario;
	private RankingManager rankingManager;
	private int vecesAlquilado;

	public Inmueble(int superficie, TipoDeInmueble tipo, Ubicacion ubi, List<Servicio> servicios, int capacidad,
			Propietario propietario) {
		this.superficie = superficie;
		this.tipo = tipo;
		this.ubicacion = ubi;
		this.servicios = servicios;
		this.capacidad = capacidad;
		this.fotos = new ArrayList<String>();
		this.propietario = propietario;
		this.rankingManager = new RankingManager();
		this.vecesAlquilado = 0;
	}

	public int getVecesAlquilado() {
		return vecesAlquilado;
	}

	public void setVecesAlquilado(int vecesAlquilado) {
		this.vecesAlquilado = vecesAlquilado;
	}

	public void añadirFoto(String link) throws CantidadFotosExcedidaException {
		if (this.fotos.size() < 5) {
			this.fotos.add(link);
		} else {
			throw new CantidadFotosExcedidaException();
		}
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

	public Propietario getPropietario() {
		// TODO Auto-generated method stub
		return this.propietario;
	}

	@Override
	public boolean esValoracionValida(Usuario ranker, Reserva reserva) {
		// TODO Auto-generated method stub
		return reserva.getAlquiler().getInmueble().equals(this) && reserva.getInquilino().equals(ranker);
	}

}
