package sistema.filtro;

import java.util.List;

import sistema.alquiler.Alquiler;
//La funcion de este clase es para obligar los tipos anden en el sistem para el filtro obligatorio
public class FiltroDeSistema {

	private  FiltroCompuesto<Alquiler> filtroSistema;
	
	public FiltroDeSistema(List<Filtro<Alquiler>> filtros) {
		this.filtroSistema = new FiltroCompuesto<Alquiler>(filtros);
	}
	
	public List<Alquiler> filtrarLista(List<Alquiler> lista){
		return this.filtroSistema.filtrarLista(lista);
	}
	
	public void agregarFiltro(Filtro<Alquiler> filtro) {
		this.filtroSistema.agregarFiltro(filtro);
	}
	
}
