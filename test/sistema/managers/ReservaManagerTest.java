package sistema.managers;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import sistema.Inmueble.Inmueble;
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
		this.userManager = new UsuarioManager();
		this.reservaManager = new ReservaManager();
		this.alquilerManager = new AlquilerManager();
        this.propietario = mock(Propietario.class);
        this.inquilino = mock(Inquilino.class);
        this.inmueble = mock(Inmueble.class);
        
        userManager.registrarUsuario(propietario);
        userManager.registrarUsuario(inquilino);
        propietario.addInmueble(inmueble);
        

	}
//	ORI: WIP - Toy en eso
	@Test
	public void unInquilinoPuedeCrearUnaReservaValida() {
		
	}

	@Test
	public void unPropietarioNoPuedeCrearUnaReserva() {
		fail("Not yet implemented");
	}

	@Test
	public void unInquilinoNoPuedeReservarUnAlquilerSiLaFechaNoEstaDisponible() {
		fail("Not yet implemented");
	}

//   Add more tests
}
