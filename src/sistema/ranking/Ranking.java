package sistema.ranking;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public class Ranking {
	private int valoracion;
	private Categoria categoria;
	private String comentario;
	private Reserva reserva;
	private Rankeable rankeable;
	private Usuario ranker;

	public Ranking(int valoracion, Categoria categoria, String comentario, Reserva reserva, Rankeable rankeable,
			Usuario ranker) throws RangoValoracionInvalidoException {
		
		if (valoracion < 1 || valoracion > 5) {
			throw new RangoValoracionInvalidoException();
		}

		this.valoracion = valoracion;
		this.categoria = categoria;
		this.comentario = comentario;
		this.rankeable = rankeable;
		this.ranker = ranker;
		this.reserva = reserva;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public Rankeable getRankeable() {
		return rankeable;
	}

	public Usuario getRanker() {
		return ranker;
	}

	public int getValoracion() {
		return this.valoracion;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public String getComentario() {
		return this.comentario;
	}
}
