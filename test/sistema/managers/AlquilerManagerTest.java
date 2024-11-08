package sistema.managers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sistema.alquiler.Alquiler;
import sistema.filtro.FiltroDeSistema;

public class AlquilerManagerTest {

    private AlquilerManager alqM;
    
    @BeforeEach
    void setUp() {
    	this.alqM = new AlquilerManager();
    }
    
//    @Test
//    public void unPropietarioDebeEstarRegistradoParaPoderCrearAlquiler() {
//    	fail("Not yet implemented");
//    }
//    @Test
//    public void unPropietarioPuedeDarDeAltaUnAlquiler() {
//    	fail("Not yet implemented");
//    }
//    @Test
//    public void unInquilinoNoPuedeDarDeAltaUnAlquiler() {
//    	fail("Not yet implemented");
//    }
//    @Test
//    public void unInquilinoPuedeObtenerLaListaDeAlquileres() {
//    	fail("Not yet implemented");
//    }
//    @Test
//    public void unInquilinoPuedeObtenereUnAlquilerSeleccionado() {
//    	fail("Not yet implemented");
//    }
//    @Test
//    public void unInquilinoPuedePedirElCalculoDePrecioParaUnPeriodo() {
//    	fail("Not yet implemented");
//    }
    
    @Test
    public void unInquilinoQuiereFiltrarLosAlquileres() {
    	FiltroDeSistema f  = mock(FiltroDeSistema.class);
    	List<Alquiler> a = this.alqM.getAlquileres();
    	
    	this.alqM.filtrarAlquiler(f);
    	verify(f,times(1)).filtrarLista(a);
    }
    
//    Add more tests
}