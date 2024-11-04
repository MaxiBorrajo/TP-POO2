package sistema.Inmueble;

public class Ubicacion {
	
	private String pais;
	private String ciudad;
	private String direccion;

	public Ubicacion(String pais , String ciudad, String direccion) {
		this.pais = pais;
		this.ciudad = ciudad;
		this.direccion = direccion;
	}

	public boolean estaEnCiudad(String ciudad2) {
		// TODO Auto-generated method stub
		return this.ciudad.equals(ciudad2);
	}

	public String getCiudad() {
		// TODO Auto-generated method stub
		return this.ciudad;
	}
	
}
