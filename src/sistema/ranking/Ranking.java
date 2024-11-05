package sistema.ranking;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.usuario.Usuario;

public class Ranking {
	private int valoracion;
	private Categoria categoria;
	private String comentario;

	public Ranking(int valoracion, Categoria categoria, String comentario) throws RangoValoracionInvalidoException {
		if (valoracion < 1 || valoracion > 5) {
		    throw new RangoValoracionInvalidoException();
		}

		this.valoracion = valoracion;
		this.categoria = categoria;
		this.comentario = comentario;
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
