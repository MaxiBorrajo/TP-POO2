package sistema.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sistema.enums.customEnums.*;

public class CustomEnumManager {
	
	private HashMap<CustomEnumType, List<CustomEnum>> enumMap;
  
    public CustomEnumManager() {
        this.enumMap = new HashMap<>();
    }
    
    private void updateEnums(CustomEnumType tipo, CustomEnum newEnum) {
	   List<CustomEnum> enumList = enumMap.computeIfAbsent(tipo, k -> new ArrayList<>());
       
	   boolean exists = enumList.stream()
               .anyMatch(existingEnum -> existingEnum.getNombre().equals(newEnum.getNombre()));

	   if (!exists) {
		   enumList.add(newEnum);
	   } else {
		   System.out.println("Enum duplicado" + newEnum.getNombre());
	   }
           
    }
   
	public Categoria crearCategoria(String nombre) {
        Categoria nuevaCategoria = new Categoria(nombre);
        this.updateEnums(nuevaCategoria.getTipo(), nuevaCategoria);
        return nuevaCategoria;
    }
	public Servicio crearServicio(String nombre) {
        Servicio nuevoServicio = new Servicio(nombre);
        this.updateEnums(nuevoServicio.getTipo(), nuevoServicio);
        return nuevoServicio;
    }

    public TipoDeInmueble crearTipoDeInmueble(String nombre) {
        TipoDeInmueble nuevoTipoDeInmueble = new TipoDeInmueble(nombre);
        this.updateEnums(nuevoTipoDeInmueble.getTipo(), nuevoTipoDeInmueble);
        return nuevoTipoDeInmueble;
    }

}
