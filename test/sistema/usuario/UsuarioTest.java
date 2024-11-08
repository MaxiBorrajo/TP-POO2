package sistema.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.enums.RolDeUsuario;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario propietario;
    private Usuario inquilino;
    private Usuario admin;

    @BeforeEach
    public void setUp() {
        propietario = new Usuario("Propietario", "propietario@example.com", "123456789", RolDeUsuario.PROPIETARIO);
        inquilino = new Usuario("Inquilino", "inquilino@example.com", "987654321", RolDeUsuario.INQUILINO);
        admin = new Usuario("Admin", "admin@example.com", "456789123", RolDeUsuario.ADMINISTRADOR);
    }

    @Test
    public void testGetters() {
        assertEquals("Propietario", propietario.getNombre());
        assertEquals("propietario@example.com", propietario.getEmail());
        assertEquals("123456789", propietario.getTelefono());
        assertEquals(RolDeUsuario.PROPIETARIO, propietario.getRol());
        assertEquals(LocalDate.now(), propietario.getFechaCreacion());
    }

    @Test
    public void testEquals() {
        Usuario propietarioDuplicado = new Usuario("Propietario Duplicado", "propietario@example.com", "000000000", RolDeUsuario.PROPIETARIO);
        assertEquals(propietario, propietarioDuplicado);
    }

    @Test
    public void testMePuedeValorar() {
        assertTrue(propietario.mePuedeValorar(inquilino));
        assertTrue(inquilino.mePuedeValorar(propietario));
        assertFalse(propietario.mePuedeValorar(admin));
        assertFalse(admin.mePuedeValorar(inquilino));
        assertFalse(inquilino.mePuedeValorar(admin));
    }
}
