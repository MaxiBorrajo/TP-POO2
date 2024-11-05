package sistema.Inmueble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.Inmueble.Inmueble;
import sistema.Inmueble.Ubicacion;
import sistema.usuario.Usuario;
import sistema.reserva.Reserva;
import sistema.alquiler.Alquiler;
import sistema.enums.customEnums.Servicio;
import sistema.enums.customEnums.TipoDeInmueble;
import sistema.usuario.Propietario;

import java.util.List;

public class InmuebleTest {

    private Inmueble inmueble;
    private Usuario mockRanker;
    private Reserva mockReserva;
    private Alquiler mockAlquiler;

    @BeforeEach
    public void setUp() {
        Propietario propietario = mock(Propietario.class);
        Ubicacion ubicacion = mock(Ubicacion.class);
        inmueble = new Inmueble(100, new TipoDeInmueble("Casa"), ubicacion, List.of(new Servicio("Limpieza")), 4, propietario);

        mockRanker = mock(Usuario.class);
        mockReserva = mock(Reserva.class);
        mockAlquiler = mock(Alquiler.class);

        when(mockAlquiler.getInmueble()).thenReturn(inmueble);
        when(mockReserva.getAlquiler()).thenReturn(mockAlquiler);


        when(mockReserva.getInquilino()).thenReturn(mockRanker);
    }

    @Test
    public void testEsValoracionValidaCasoValido() {
        boolean resultado = inmueble.esValoracionValida(mockRanker, mockReserva);

        assertTrue(resultado, "El método debería devolver true cuando el ranker es el inquilino y el inmueble coincide con 'this'");
    }

    @Test
    public void testEsValoracionValidaCasoInvalido() {
        Usuario otroInquilino = mock(Usuario.class);
        when(mockReserva.getInquilino()).thenReturn(otroInquilino);
        
        boolean resultado = inmueble.esValoracionValida(mockRanker, mockReserva);

        assertFalse(resultado, "El método debería devolver false cuando el inquilino no coincide con 'ranker' o el inmueble no coincide con 'this'");
    }
}
