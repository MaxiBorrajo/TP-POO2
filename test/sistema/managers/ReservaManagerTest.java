package sistema.managers;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import sistema.Inmueble.Inmueble;
import sistema.enums.RolDeUsuario;
import sistema.exceptions.InmuebleYaGuardadoException;
import sistema.exceptions.UsuarioExistenteException;
import sistema.usuario.*;
import sistema.usuario.Propietario;

public class ReservaManagerTest {
	private ReservaManager reservaManager;
	private Propietario propietario;
	private Inquilino inquilino;
	private Inmueble inmueble;
	private AlquilerManager alquilerManager;
	private UsuarioManager userManager;
	
	@BeforeEach
	public void setUp() throws UsuarioExistenteException, InmuebleYaGuardadoException {
		this.reservaManager = new ReservaManager();
	    this.userManager = mock(UsuarioManager.class);
		this.alquilerManager = mock(AlquilerManager.class);
        this.inmueble = mock(Inmueble.class);
        

        userManager.registrarUsuario("Juan Perez", "juan@example.com", "123456789",RolDeUsuario.INQUILINO);
        userManager.registrarUsuario("Ana Gomez", "ana@example.com", "987654321",RolDeUsuario.PROPIETARIO);

        propietario.addInmueble(inmueble);
        

	}
//	ORI: WIP - Toy en eso
	@Test
	public void unInquilinoPuedeCrearUnaReservaValida() {
		
	}

//	@Test
//	public void unPropietarioNoPuedeCrearUnaReserva() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void unInquilinoNoPuedeReservarUnAlquilerSiLaFechaNoEstaDisponible() {
//		fail("Not yet implemented");
//	}

//   Add more tests
}
