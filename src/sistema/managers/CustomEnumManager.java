package sistema.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sistema.enums.customEnums.*;
import sistema.exceptions.CustomEnumExistenteException;
import sistema.exceptions.ExistentCustomEnumException;

public class CustomEnumManager {
	
	private HashMap<CustomEnumType, List<CustomEnum>> enumMap;
  
    public CustomEnumManager() {
        this.enumMap = new HashMap<>();
    }
    
    private void updateEnums(CustomEnumType tipo, CustomEnum newEnum) throws CustomEnumExistenteException {
	   List<CustomEnum> enumList = enumMap.computeIfAbsent(tipo, k -> new ArrayList<>());
       
	   boolean exists = enumList.stream()
               .anyMatch(existingEnum -> existingEnum.getNombre().equals(newEnum.getNombre()));

	   if (!exists) {
		   enumList.add(newEnum);
	   } else {
		   throw new CustomEnumExistenteException();
	   }
           
    }
   
    public CustomEnum createCustomEnum(String nombre, CustomEnumType tipo) {
        CustomEnum newEnum;
        
        switch (tipo) {
            case CATEGORIA:
                newEnum = new Categoria(nombre);
                break;
            case TIPODEINMUEBLE:
                newEnum = new TipoDeInmueble(nombre);
                break;
            case SERVICIO:
                newEnum = new Servicio(nombre);
                break;
            default:
                throw new IllegalArgumentException("Invalid CustomEnumType");
        }

        updateEnums(tipo, newEnum);
        return newEnum;
    }

}
