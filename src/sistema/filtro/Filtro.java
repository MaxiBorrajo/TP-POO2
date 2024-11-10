package sistema.filtro;

import java.util.List;

public abstract class Filtro<T> {

	public List<T> filtrarLista(List<T> lista) {
		return lista.stream().filter(x -> this.cumpleCondicion(x)).toList();
	}

	protected abstract boolean cumpleCondicion(T elem);
}
