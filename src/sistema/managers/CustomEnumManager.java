package sistema.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sistema.enums.customEnums.*;
import sistema.exceptions.YaExistenteException;

public class CustomEnumManager {

	private HashMap<CustomEnumType, List<CustomEnum>> enumMap;

	public CustomEnumManager() {
		this.enumMap = new HashMap<>();
	}

	public boolean existeCustomEnum(String nombre, CustomEnumType tipo) {
		List<CustomEnum> enumList = enumMap.get(tipo);
		if (enumList == null) {
			return false;
		}
		return enumList.stream().anyMatch(existingEnum -> existingEnum.getNombre().equals(nombre));
	}

	private void updateEnums(CustomEnumType tipo, CustomEnum newEnum) throws YaExistenteException {
		List<CustomEnum> enumList = enumMap.computeIfAbsent(tipo, k -> new ArrayList<>());

		boolean exists = enumList.stream()
				.anyMatch(existingEnum -> existingEnum.getNombre().equals(newEnum.getNombre()));

		if (!exists) {
			enumList.add(newEnum);
		} else {
			throw new YaExistenteException(tipo.name()); 
		}

	}

	public CustomEnum createCustomEnum(String nombre, CustomEnumType tipo) throws YaExistenteException {
		CustomEnum newEnum = tipo.crearInstancia(nombre);
		updateEnums(tipo, newEnum);
		return newEnum;
	}

}
