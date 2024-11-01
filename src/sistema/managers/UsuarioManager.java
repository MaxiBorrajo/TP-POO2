package sistema.managers;

import java.util.ArrayList;
import java.util.List;

import sistema.exceptions.UsuarioExistenteException;
import sistema.usuario.Usuario;

public class UsuarioManager {
	private List<Usuario> usuarios;

	public UsuarioManager() {
		this.usuarios = new ArrayList<Usuario>();
	}

	public void registrarUsuario(Usuario user) throws UsuarioExistenteException {
		if (this.usuarios.contains(user)) {
			throw new UsuarioExistenteException("Usuario already exists: " + user.getNombre());
		} else {
			this.usuarios.add(user);
		}
	}

	public boolean estaRegistrado(Usuario user) {
		return this.usuarios.contains(user);
	}

	public int cantidadDeUsuarios() {
		return this.usuarios.size();
	}
}
