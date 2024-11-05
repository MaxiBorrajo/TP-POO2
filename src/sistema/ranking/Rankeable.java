package sistema.ranking;

import java.time.LocalDate;
import java.util.List;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.managers.RankingManager;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public abstract class Rankeable {
	private RankingManager rankingManager;
	
	public Rankeable() {
		this.rankingManager = new RankingManager();
	}
	
	public List<Ranking> getValoraciones() {
		// TODO Auto-generated method stub
		return this.rankingManager.getValoraciones();
	}


	public List<Ranking> getValoracionesPorCategoria(Categoria categoria) {
	    return this.rankingManager.getValoracionesPorCategoria(categoria);
	}

	public double getPromedioValoraciones() {
	    return this.rankingManager.getPromedioRanking();
	}

	public double getPromedioValoracionesPorCategoria(Categoria categoria) {
	    return this.rankingManager.getPromedioValoracionesPorCategoria(categoria);
	}


	public void añadirValoracion(Ranking valoracion, Usuario ranker, Reserva reserva) throws ServicioNoTerminadoException, ValoracionInvalidaException {
		// TODO Auto-generated method stub
		this.validarPuedeAñadirValoracion(ranker, reserva);
		this.rankingManager.añadirValoracion(valoracion);
	}

	public void validarPuedeAñadirValoracion(Usuario ranker, Reserva reserva) throws ServicioNoTerminadoException, ValoracionInvalidaException {
		if(!reserva.fechaPosteriorAFinal(LocalDate.now())) {
			throw new ServicioNoTerminadoException();
		}
		
		if(!this.esValoracionValida(ranker, reserva)) {
			throw new ValoracionInvalidaException();
		}
	};
	
	abstract protected boolean esValoracionValida(Usuario ranker, Reserva reserva);
	

	public List<String> getComentarios() {
		// TODO Auto-generated method stub
		return this.rankingManager.getComentarios();
	}
}
