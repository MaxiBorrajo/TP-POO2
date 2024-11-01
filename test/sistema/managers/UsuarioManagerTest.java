package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.exceptions.UsuarioExistenteException;
import sistema.usuario.Inquilino;

public class UsuarioManagerTest {

    private UsuarioManager usuarioManager;    
    private Inquilino inquilinoUno;

    @BeforeEach
    public void setUp() {
        this.usuarioManager = new UsuarioManager();  
        this.inquilinoUno = mock(Inquilino.class);
    }
    
    @Test
    public void testUsuarioManagerPuedeGuardarUnUsuario() throws UsuarioExistenteException {
        // initial state: cantidadDeUsuarios should be 0
        assertEquals(0, usuarioManager.cantidadDeUsuarios(), "Initial user count should be 0");
        
        // Register a user
        usuarioManager.registrarUsuario(inquilinoUno);
        
        // final state: cantidadDeUsuarios should now be 1 and user should be registered in the system
        assertEquals(1, usuarioManager.cantidadDeUsuarios(), "User count should be 1 after registration");
        assertTrue(usuarioManager.estaRegistrado(inquilinoUno));
    }
    @Test
    public void testRegistrarUsuarioThrowsExceptionSiElUsuarioYaExiste() throws UsuarioExistenteException {
        // Register the user once
        usuarioManager.registrarUsuario(inquilinoUno);
        
        // Attempt to register the same user again and assert that an exception is thrown
        assertThrows(UsuarioExistenteException.class, () -> usuarioManager.registrarUsuario(inquilinoUno), 
                     "Expected UsuarioExistenteException to be thrown when registering the same user twice");
    }

}
