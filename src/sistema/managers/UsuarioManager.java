package sistema.managers;
import java.util.ArrayList;
import java.util.List;

import sistema.Usuario.Usuario;

public class UsuarioManager {
	private List<Usuario> usuarios;
	
	public UsuarioManager() {
		this.usuarios = new ArrayList<Usuario>();
	}
	
	public void registrarUsuario(Usuario user) {
		this.usuarios.add(user);
	}
	
	public boolean estaRegistrado(Usuario user) {
		return this.usuarios.contains(user);
	}
	
	public int cantidadDeUsuarios() {
		return this.usuarios.size();
	}
}
