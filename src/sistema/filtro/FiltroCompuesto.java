package sistema.filtro;

import java.util.ArrayList;
import java.util.List;

public class FiltroCompuesto<T> extends Filtro<T> {

	private List<Filtro<T>> filtros;

	public FiltroCompuesto(List<Filtro<T>> filtros) {
		this.filtros = filtros;
	}

	public FiltroCompuesto() {
		this.filtros = new ArrayList<Filtro<T>>();
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
