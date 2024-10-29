package sistema.filtro;

import java.util.List;

public class FiltroCompuesto<T> extends Filtro<T>{
	
	private List<Filtro<T>> filtros;
	
	public FiltroCompuesto(List<Filtro<T>> filtros) {
		this.filtros = filtros;
	}

	@Override
	protected boolean cumpleCondicion(T elem) {
		// TODO Auto-generated method stub
		return this.filtros.stream().allMatch(f -> f.cumpleCondicion(elem));
	}
	
	public void agregarFiltro(Filtro<T> filtro) {
		this.filtros.add(filtro);
	}
	
	
}
