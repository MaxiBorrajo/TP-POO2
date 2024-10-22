package tpIntegrador.Sistema;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tpIntegrador.Usuario.Inquilino;

public class SistemaTest {
	private Sistema sistema;
    private Inquilino usuarioNuevo;

    @Before
    public void setUp() {
        sistema = new Sistema();
        usuarioNuevo = new Inquilino("pepe", "pepe@pepe.com", "12345678");
    }

    @Test
    public void testSistemaPuedeGuardarUnUsuario() {
        sistema.registrarUsuario(usuarioNuevo);
        assertTrue(sistema.estaRegistrado(usuarioNuevo));
        assertEquals(sistema.cantidadDeUsuarios(), 1);
    }

}
