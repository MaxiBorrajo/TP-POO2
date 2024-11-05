package sistema.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.reserva.Reserva;
import sistema.Inmueble.Inmueble;
import sistema.alquiler.Alquiler;

public class InquilinoTest {

    private Inquilino inquilino;
    private Usuario mockRanker;
    private Reserva mockReserva;
    private Inmueble mockInmueble;

    @BeforeEach
    public void setUp() {
        inquilino = new Inquilino("Inquilino", "inquilino@gmail.com", "telefono");
        mockRanker = mock(Usuario.class);
        mockReserva = mock(Reserva.class);
        mockInmueble = mock(Inmueble.class);

        Alquiler mockAlquiler = mock(Alquiler.class);
        when(mockAlquiler.getInmueble()).thenReturn(mockInmueble);

        when(mockReserva.getAlquiler()).thenReturn(mockAlquiler);

        when(mockInmueble.getPropietario()).thenReturn(mockRanker);

        when(mockReserva.getInquilino()).thenReturn(inquilino);
    }

    @Test
    public void testEsValoracionValidaCasoValido() {
        boolean resultado = inquilino.esValoracionValida(mockRanker, mockReserva);

        assertTrue(resultado, "El método debería devolver true cuando el ranker es el propietario y el inquilino coincide con 'this'");
    }

    @Test
    public void testEsValoracionValidaCasoInvalido() {
        Inquilino otroInquilino = mock(Inquilino.class);
        when(mockReserva.getInquilino()).thenReturn(otroInquilino);

        boolean resultado = inquilino.esValoracionValida(mockRanker, mockReserva);

        assertFalse(resultado, "El método debería devolver false cuando el inquilino no coincide con 'this' o el ranker no es el propietario");
    }
}
