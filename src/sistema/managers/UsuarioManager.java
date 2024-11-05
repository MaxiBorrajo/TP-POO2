package sistema.managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import sistema.enums.RolDeUsuario;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.UsuarioExistenteException;
import sistema.exceptions.UsuarioNoRegistradoException;
import sistema.usuario.Inquilino;
import sistema.usuario.Propietario;
import sistema.usuario.Usuario;

public class UsuarioManager {
	private List<Usuario> usuarios;

	public UsuarioManager() {
		this.usuarios = new ArrayList<Usuario>();
	}

	public Usuario registrarUsuario(String nombreCompleto, String email, String telefono, RolDeUsuario rol)
			throws Exception {
		if (this.estaRegistrado(email)) {
			throw new UsuarioExistenteException();
		} else {
			Usuario usuario;
			if (rol == RolDeUsuario.INQUILINO) {
				usuario = new Inquilino(nombreCompleto, email, telefono);
			} else {
				usuario = new Propietario(nombreCompleto, email, telefono, LocalDate.now());

			}

			this.usuarios.add(usuario);
			return usuario;
		}
	}

	public boolean estaRegistrado(String email) {
		return this.usuarios.stream().anyMatch(user -> user.getEmail().equals(email));
	}

	public int cantidadDeUsuarios() {
		return this.usuarios.size();
	}

	public void validarUsuario(Usuario usuario, RolDeUsuario rol)
			throws UsuarioNoRegistradoException, PermisoDenegadoException {
		if (!this.estaRegistrado(usuario.getEmail())) {
			throw new UsuarioNoRegistradoException();
		}
		if (usuario.getRol() != rol) {
			throw new PermisoDenegadoException();
		}
	}
}
