package sistema.managers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sistema.Usuario.Inquilino;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioManagerTest {

	@Mock
    private UsuarioManager usuarioManager;    
    
    @Mock
    private Inquilino usuarioNuevo;


    @Test
    public void testUsuarioManagerPuedeGuardarUnUsuario() {
     
        doNothing().when(usuarioManager).registrarUsuario(usuarioNuevo);
        when(usuarioManager.estaRegistrado(usuarioNuevo)).thenReturn(true);
        when(usuarioManager.cantidadDeUsuarios()).thenReturn(1);

        usuarioManager.registrarUsuario(usuarioNuevo);

        assertTrue(usuarioManager.estaRegistrado(usuarioNuevo));
        assertEquals(1, usuarioManager.cantidadDeUsuarios());

        verify(usuarioManager).registrarUsuario(usuarioNuevo);
        verify(usuarioManager).estaRegistrado(usuarioNuevo);
        verify(usuarioManager).cantidadDeUsuarios();
    }
    @Test
    public void testUnUsuarioNoPuedeRegistrarseDosVeces() {
    	fail("Not yet implemented");
    }
    
}