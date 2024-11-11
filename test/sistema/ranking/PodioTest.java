package sistema.ranking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.podio.Podio;
import sistema.usuario.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PodioTest {

    private Podio podio;
    private Usuario inquilinoMock;

    @BeforeEach
    public void setUp() {
        inquilinoMock = mock(Usuario.class);
        when(inquilinoMock.getNombre()).thenReturn("Juan Carlos");
        podio = new Podio(inquilinoMock, 5);
    }

    @Test
    public void testGetInquilino() {
        assertEquals(inquilinoMock, podio.getInquilino());
    }

    @Test
    public void testGetCantidadReservas() {
        assertEquals(5, podio.getCantidadReservas());
    }

    @Test
    public void testToString() {
        String expectedString = "Inquilino: " + inquilinoMock + ", Cantidad de Reservas: 5";
        assertEquals(expectedString, podio.toString());
    }
}
