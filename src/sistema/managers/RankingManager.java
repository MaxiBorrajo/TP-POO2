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

public class RankingManager {
	private List<Ranking> rankings;

	public RankingManager() {
		this.rankings = new ArrayList<Ranking>();
	}

	public List<Ranking> getValoraciones() {
		return this.rankings;
	};

	public List<Ranking> getValoracionesPorCategoria(Categoria categoria) {
		return new FiltroSimple<Ranking>(r -> r.getCategoria().equals(categoria)).filtrarLista(this.getValoraciones());
	};

	private double getPromedioDeRankings(List<Ranking> rankings) {
		double sumaTotal = rankings.stream().mapToDouble(r -> r.getValoracion()).sum();
		double cantidadRankings = rankings.size();
		return sumaTotal / cantidadRankings;
	}

	public double getPromedioValoraciones() {
		return this.getPromedioDeRankings(this.getValoraciones());
	}

	public double getPromedioValoracionesPorCategoria(Categoria categoria) {
		return this.getPromedioDeRankings(this.getValoracionesPorCategoria(categoria));
	}

	public List<String> getComentarios() {
		return this.getValoraciones().stream().map(v -> v.getComentario()).toList();
	}

	public void añadirValoracion(Ranking valoracion) {
		this.rankings.add(valoracion);
	}

}
