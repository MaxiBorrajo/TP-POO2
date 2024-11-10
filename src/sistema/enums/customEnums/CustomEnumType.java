package sistema.enums.customEnums;

public enum CustomEnumType {
	CATEGORIA {
		@Override
		public CustomEnum crearInstancia(String nombre) {
			return new Categoria(nombre);
		}
	},
	TIPODEINMUEBLE {
		@Override
		public CustomEnum crearInstancia(String nombre) {
			return new TipoDeInmueble(nombre);
		}
	},
	SERVICIO {
		@Override
		public CustomEnum crearInstancia(String nombre) {
			return new Servicio(nombre);
		}
	};

	public abstract CustomEnum crearInstancia(String nombre);
}