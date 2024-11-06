package sistema.Inmueble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sistema.Inmueble.Visualizacion;
import sistema.alquiler.Alquiler;
import sistema.enums.customEnums.Categoria;
import sistema.ranking.Ranking;
import sistema.usuario.Propietario;
import sistema.Inmueble.Inmueble;

public class VisualizacionTest {
    
    private Visualizacion visualizacion;
    private Inmueble inmuebleMock;
    private Propietario propietarioMock;

    @BeforeEach
    public void setUp() {
        inmuebleMock = mock(Inmueble.class);
        propietarioMock = mock(Propietario.class);

        when(inmuebleMock.getPropietario()).thenReturn(propietarioMock);

        visualizacion = new Visualizacion(inmuebleMock);
    }

    @Test
    public void testGetInmueble() {
        assertEquals(inmuebleMock, visualizacion.getInmueble());
    }

    @Test
    public void testGetPropietario() {
        assertEquals(propietarioMock, visualizacion.getPropietario());
    }

    @Test
    public void testGetComentarios() {
        List<String> comentarios = Arrays.asList("Buen lugar", "Mal mantenimiento");
        when(inmuebleMock.getComentarios()).thenReturn(comentarios);
        
        assertEquals(comentarios, visualizacion.getComentarios());
    }

    @Test
    public void testGetPuntajesEnCategoria() {
        Categoria categoriaMock = new Categoria("Limpieza");
        List<Ranking> rankings = Arrays.asList(mock(Ranking.class), mock(Ranking.class));
        
        when(inmuebleMock.getValoracionesPorCategoria(categoriaMock)).thenReturn(rankings);
        
        assertEquals(rankings, visualizacion.getPuntajesEnCategoria(categoriaMock));
    }

    @Test
    public void testGetPuntajesDePropietario() {
        List<Ranking> puntajesPropietario = Arrays.asList(mock(Ranking.class), mock(Ranking.class));
        
        when(propietarioMock.getValoraciones()).thenReturn(puntajesPropietario);
        
        assertEquals(puntajesPropietario, visualizacion.getPuntajesDePropietario());
    }

    @Test
    public void testGetPuntajePromedioDePropietario() { 
        when(propietarioMock.getPromedioValoraciones()).thenReturn(4.5);
        
        assertEquals(4.5, visualizacion.getPuntajePromedioDePropietario());
    }

    @Test
    public void testGetPuntajePromedio() {
    	when(inmuebleMock.getPromedioValoraciones()).thenReturn(4.2);
        
        assertEquals(4.2, visualizacion.getPuntajePromedio());
    }

    @Test
    public void testGetPuntajePromedioPorCategoria() {
        Categoria categoriaMock = new Categoria("Limpieza");
        
        when(inmuebleMock.getPromedioValoracionesPorCategoria(categoriaMock)).thenReturn(3.8);
        
        assertEquals(3.8, visualizacion.getPuntajePromedioPorCategoria(categoriaMock));
    }

    @Test
    public void testGetPropietarioAntiguedad() {
        LocalDate fechaCreacion = LocalDate.now().minusYears(2).minusMonths(3).minusDays(15);
        
        when(propietarioMock.getFechaCreacion()).thenReturn(fechaCreacion);
        
        assertEquals("Propietario hace 2 años, 3 meses y 15 días.", visualizacion.getPropietarioAntiguedad());
    }

    @Test
    public void testGetVecesInmuebleAlquilado() {
        when(inmuebleMock.getVecesAlquilado()).thenReturn(5);
        
        assertEquals(5, visualizacion.getVecesInmuebleAlquilado());
    }

    @Test
    public void testGetVecesPropietarioOfertoInmuebles() {
        Inmueble inmueble1 = mock(Inmueble.class);
        Inmueble inmueble2 = mock(Inmueble.class);
        when(inmueble1.getVecesAlquilado()).thenReturn(3);
        when(inmueble2.getVecesAlquilado()).thenReturn(2);
        
        List<Inmueble> inmuebles = Arrays.asList(inmueble1, inmueble2);
        when(propietarioMock.getInmuebles()).thenReturn(inmuebles);
        
        assertEquals(5, visualizacion.getVecesPropietarioAlquiloInmuebles());
    }

    @Test
    public void testGetPropietarioInmueblesOfertados() {
        Inmueble inmueble1 = mock(Inmueble.class);
        Inmueble inmueble2 = mock(Inmueble.class);
        
        when(inmueble1.getVecesAlquilado()).thenReturn(3);
        when(inmueble2.getVecesAlquilado()).thenReturn(0);
        
        List<Inmueble> inmuebles = Arrays.asList(inmueble1, inmueble2);
        when(propietarioMock.getInmuebles()).thenReturn(inmuebles);
        
        List<Inmueble> result = visualizacion.getPropietarioInmueblesAlquilados();
        
        assertEquals(1, result.size());
        assertEquals(inmueble1, result.get(0));
    }
}