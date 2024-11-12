package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.enums.customEnums.CustomEnumType;

public class CustomEnumManagerTest {
    
    private CustomEnumManager customEnumManager;

    @BeforeEach
    void setUp() {
        customEnumManager = new CustomEnumManager();
    }

    @Test
    void testExisteCustomEnumSiLaListaEsNull() {
        CustomEnumType tipo = CustomEnumType.CATEGORIA;
   
        boolean result = customEnumManager.existeCustomEnum("someEnumName", tipo);
        
        assertFalse(result);
    }
}
