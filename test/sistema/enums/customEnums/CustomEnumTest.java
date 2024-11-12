package sistema.enums.customEnums;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioTest {

    private Servicio servicio1;
    private Servicio servicio2;

    @BeforeEach
    public void setUp() {
        servicio1 = new Servicio("Internet");
        servicio2 = new Servicio("Cable TV");
    }

    @Test
    public void testGetTipo() {
        assertEquals(CustomEnumType.SERVICIO, servicio1.getTipo());
        assertEquals(CustomEnumType.SERVICIO, servicio2.getTipo());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(servicio1.equals(servicio1));
    }

    @Test
    public void testEqualsDifferentObjectsSameNombre() {
        Servicio servicio3 = new Servicio("Internet");
        assertTrue(servicio1.equals(servicio3));
    }

    @Test
    public void testEqualsDifferentObjectsDifferentNombre() {
        assertFalse(servicio1.equals(servicio2));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(servicio1.equals(null));
    }


    @Test
    public void testHashCode() {
        Servicio servicio3 = new Servicio("Internet");
        assertEquals(servicio1.hashCode(), servicio3.hashCode());

        assertNotEquals(servicio1.hashCode(), servicio2.hashCode());
    }
}
