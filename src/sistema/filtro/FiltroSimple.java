package sistema.filtro;

import java.util.function.Predicate;

public class FiltroSimple<A> extends Filtro<A>{
	
	private Predicate<A> predicado;
	public FiltroSimple(Predicate<A> lambda) {
		this.predicado = lambda;
	}
	@Override
	protected boolean cumpleCondicion(A elem) {
		// TODO Auto-generated method stub
		return this.predicado.test(elem);
	}
	
	
	
}
