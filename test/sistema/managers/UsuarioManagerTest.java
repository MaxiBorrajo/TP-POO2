package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.enums.RolDeUsuario;
import sistema.exceptions.PermisoDenegadoException;
import sistema.exceptions.UsuarioExistenteException;
import sistema.exceptions.UsuarioNoRegistradoException;
import sistema.usuario.Usuario;

public class UsuarioManagerTest {

	private UsuarioManager usuarioManager;

	@BeforeEach
	public void setUp() {
		this.usuarioManager = new UsuarioManager();
	}
 
	@Test
	public void testUsuarioManagerPuedeGuardarUnUsuario() throws Exception {
		String nombreCompleto = "John Doe";
		String email = "johndoe@example.com";
		String telefono = "123456789";
		RolDeUsuario rol = RolDeUsuario.INQUILINO;

		assertEquals(0, usuarioManager.cantidadDeUsuarios());

		Usuario usuario = usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);

		assertEquals(1, usuarioManager.cantidadDeUsuarios());
		assertTrue(usuarioManager.estaRegistrado(email));
		assertNotNull(usuario);
		assertEquals(nombreCompleto, usuario.getNombre());
		assertEquals(email, usuario.getEmail());
		assertEquals(telefono, usuario.getTelefono());
	}

	@Test
	public void testRegistrarUsuarioThrowsExceptionSiElUsuarioYaExiste() throws Exception {
		String nombreCompleto = "Jane Doe";
		String email = "janedoe@example.com";
		String telefono = "987654321";
		RolDeUsuario rol = RolDeUsuario.PROPIETARIO;

		usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);

		assertThrows(UsuarioExistenteException.class,
				() -> usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol));
	}

	@Test
	public void testValidarUsuarioFallaAlUtilizarUnUsuarioNoRegistrado() {
		Usuario usuario = mock(Usuario.class);
		assertThrows(UsuarioNoRegistradoException.class,
				() -> usuarioManager.validarUsuario(usuario, RolDeUsuario.INQUILINO));
	}

	@Test
	public void testValidarUsuarioInquilinoConRolDePropietarioFalla() throws Exception {
		String nombreCompleto = "Jane Doe";
		String email = "janedoe@example.com";
		String telefono = "987654321";
		RolDeUsuario rol = RolDeUsuario.INQUILINO;

		Usuario usuario = usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);

		assertThrows(PermisoDenegadoException.class,
				() -> usuarioManager.validarUsuario(usuario, RolDeUsuario.PROPIETARIO));

	}

	@Test
	public void testValidarUsuarioPropietarioConRolDeInquilinoFalla() throws Exception {
		String nombreCompleto = "Jane Doe";
		String email = "janedoe@example.com";
		String telefono = "987654321";
		RolDeUsuario rol = RolDeUsuario.PROPIETARIO;

		Usuario usuario = usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);

		assertThrows(PermisoDenegadoException.class,
				() -> usuarioManager.validarUsuario(usuario, RolDeUsuario.INQUILINO));

	}

	@Test
	public void testValidacionExitosaDePropietarioConRolPropietario() throws Exception {
		String nombreCompleto = "Jane Doe";
		String email = "janedoe@example.com";
		String telefono = "987654321";
		RolDeUsuario rol = RolDeUsuario.PROPIETARIO;

		Usuario usuario = usuarioManager.registrarUsuario(nombreCompleto, email, telefono, rol);
		assertDoesNotThrow(() -> usuarioManager.validarUsuario(usuario, RolDeUsuario.PROPIETARIO));

	}
}
