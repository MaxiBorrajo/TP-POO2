package sistema.managers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.filtro.FiltroSimple;
import sistema.ranking.Rankeable;
import sistema.ranking.Ranking;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class RankingManager {
	private List<Ranking> rankings;

	public RankingManager() {
		this.rankings = new ArrayList<Ranking>();
	}

	public List<Ranking> getValoraciones(Rankeable rankeable) {
		return new FiltroSimple<Ranking>(r -> r.getRankeable().equals(rankeable)).filtrarLista(rankings);
	};

	public List<Ranking> getValoracionesPorCategoria(Rankeable rankeable, Categoria categoria) {
		return new FiltroSimple<Ranking>(r -> r.getCategoria().equals(categoria))
				.filtrarLista(this.getValoraciones(rankeable));
	};

	private double getPromedioDeRankings(List<Ranking> rankings) {
		double sumaTotal = rankings.stream().mapToDouble(r -> r.getValoracion()).sum();
		double cantidadRankings = rankings.size();
		return sumaTotal / cantidadRankings;
	}

	public double getPromedioValoraciones(Rankeable rankeable) {
		return this.getPromedioDeRankings(this.getValoraciones(rankeable));
	}

	public double getPromedioValoracionesPorCategoria(Rankeable rankeable, Categoria categoria) {
		return this.getPromedioDeRankings(this.getValoracionesPorCategoria(rankeable, categoria));
	}

	public List<String> getComentarios(Rankeable rankeable) {
		return this.getValoraciones(rankeable).stream().map(v -> v.getComentario()).toList();
	}

	public void validarPuedeAñadirValoracion(Ranking valoracion)
			throws ServicioNoTerminadoException, ValoracionInvalidaException {
		if (!valoracion.getReserva().fechaPosteriorAFinal(LocalDate.now())) {
			throw new ServicioNoTerminadoException();
		}

		if (!valoracion.getRankeable().mePuedeValorar(valoracion.getRanker())) {
			throw new ValoracionInvalidaException();
		}
	};

	public void añadirValoracion(Ranking valoracion) throws ServicioNoTerminadoException, ValoracionInvalidaException {
		// TODO Auto-generated method stub
		this.validarPuedeAñadirValoracion(valoracion);
		this.rankings.add(valoracion);
	}

}
