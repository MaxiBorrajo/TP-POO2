package sistema.usuario;


import java.time.LocalDate;
import java.util.List;

import sistema.enums.RolDeUsuario;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.managers.RankingManager;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;

public class Usuario implements Rankeable{
	private String nombre;
	private String email;
	private String telefono;
	private RolDeUsuario rol;
	private LocalDate fechaCreacion;

	public Usuario(String nombreCompleto, String email, String telefono, RolDeUsuario rol) {
		super();
		this.nombre = nombreCompleto;
		this.email = email;
		this.telefono = telefono;
		this.rol = rol;
		this.fechaCreacion = LocalDate.now();
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public String getNombre() {
		return this.nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		return email.equals(other.email);
	}

	public String getEmail() {
		return this.email;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public RolDeUsuario getRol() {
		return this.rol;
	}

	/*
	 * Si soy inquilino solo me valora un propietari
	 * Si soy propietario solo me valora un inquilino
	 * */
	@Override
	public boolean mePuedeValorar(Usuario usuario) {
		return (usuario.getRol().equals(RolDeUsuario.INQUILINO) && this.getRol().equals(RolDeUsuario.PROPIETARIO)) ||
				(usuario.getRol().equals(RolDeUsuario.PROPIETARIO) && this.getRol().equals(RolDeUsuario.INQUILINO));
	}
}
