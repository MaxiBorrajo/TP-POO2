package sistema.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sistema.enums.customEnums.*;
import sistema.exceptions.CustomEnumExistenteException;

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
   
    public CustomEnum createCustomEnum(String nombre, CustomEnumType tipo) throws CustomEnumExistenteException {
        CustomEnum newEnum = tipo.crearInstancia(nombre);
        updateEnums(tipo, newEnum);
        return newEnum;
    }

}
