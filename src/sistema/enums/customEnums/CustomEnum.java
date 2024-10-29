package sistema.enums.customEnums;

public abstract class CustomEnum {
	protected String nombre;
	protected CustomEnumType tipo;

	public String getNombre() {
		return this.nombre;
	}
	public CustomEnumType getTipo() {
		return this.tipo;
	}
}
