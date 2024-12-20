package sistema.usuario;

import java.time.LocalDate;

import sistema.enums.RolDeUsuario;
import sistema.ranking.Rankeable;

public class Usuario implements Rankeable {
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

		Usuario other = (Usuario) obj;
		return email.equals(other.getEmail());
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
	 * Si soy inquilino solo me valora un propietari0. Si soy propietario solo me
	 * valora un inquilino.
	 */
	@Override
	public boolean mePuedeValorar(Usuario usuario) {
		return this.mePuedeValorarSiendoInquilino(usuario) || this.mePuedeValorarSiendoPropietario(usuario);
	}

	private boolean mePuedeValorarSiendoPropietario(Usuario usuario) {
		return usuario.getRol().equals(RolDeUsuario.PROPIETARIO) && this.getRol().equals(RolDeUsuario.INQUILINO);
	}

	private boolean mePuedeValorarSiendoInquilino(Usuario usuario) {
		return (usuario.getRol().equals(RolDeUsuario.INQUILINO) && this.getRol().equals(RolDeUsuario.PROPIETARIO));
	}
}
