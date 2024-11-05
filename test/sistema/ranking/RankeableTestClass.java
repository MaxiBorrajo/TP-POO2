package sistema.ranking;

import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class RankeableTestClass extends Rankeable {

	@Override
	protected boolean esValoracionValida(Usuario ranker, Reserva reserva) {
		// TODO Auto-generated method stub
		return true;
	}

}
