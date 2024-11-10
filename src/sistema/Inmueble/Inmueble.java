package sistema.Inmueble;

import java.util.ArrayList;
import java.util.List;

import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Servicio;
import sistema.enums.customEnums.TipoDeInmueble;
import sistema.exceptions.CantidadFotosExcedidaException;
import sistema.exceptions.PermisoDenegadoException;
import sistema.ranking.Rankeable;
import sistema.usuario.Usuario;

public class Inmueble implements Rankeable {
	private int id;
	private int superficie;
	private TipoDeInmueble tipo;
	private Ubicacion ubicacion;
	private List<Servicio> servicios;
	private int capacidad;
	private List<String> fotos;
	private Usuario propietario;
	private int vecesAlquilado;

	public Inmueble(int superficie, TipoDeInmueble tipo, Ubicacion ubi, List<Servicio> servicios, int capacidad,
			Usuario propietario) {

		if(!propietario.getRol().equals(RolDeUsuario.PROPIETARIO)) {

			new PermisoDenegadoException();
		}
		 
		this.superficie = superficie;
		this.tipo = tipo;
		this.ubicacion = ubi;
		this.servicios = servicios;
		this.capacidad = capacidad;
		this.fotos = new ArrayList<String>();
		this.propietario = propietario;
		this.vecesAlquilado = 0;
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	public int getVecesAlquilado() {
		return vecesAlquilado;
	}

	public void setVecesAlquilado(int vecesAlquilado) {
		this.vecesAlquilado = vecesAlquilado;
	}

	public void añadirFoto(String link)  {
		if (this.fotos.size() < 5) {
			this.fotos.add(link);
		} 
	}

	public boolean esDeCiudad(String ciudad) {
		return this.ubicacion.estaEnCiudad(ciudad);
	}

	public int getCapacidad() {
		return this.capacidad;
	}

	public boolean sonElMismoInmueble(Inmueble inm2) {
		return this == inm2;
	}

	public String getTipo() {
		return this.tipo.getNombre();
	}


	

	public Usuario getPropietario() {
		return this.propietario;
	}

	@Override
	public boolean mePuedeValorar(Usuario usuario) {
		return usuario.getRol().equals(RolDeUsuario.INQUILINO);
	}

	public int getSuperficie() {
		// TODO Auto-generated method stub
		return this.superficie;
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.ubicacion.getCiudad();
	}

	public List<String> fotos() {
		// TODO Auto-generated method stub
		return this.fotos;
	}

}
