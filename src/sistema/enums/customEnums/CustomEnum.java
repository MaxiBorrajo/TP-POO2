package sistema.enums.customEnums;

import java.util.Objects;

public abstract class CustomEnum {
	protected String nombre;
	protected CustomEnumType tipo;

	public String getNombre() {
		return this.nombre;
	}
	public CustomEnumType getTipo() {
		return this.tipo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustomEnum customEnum = (CustomEnum) obj;
		return Objects.equals(nombre, customEnum.nombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}
}
