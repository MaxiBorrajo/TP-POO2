package sistema.Inmueble;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import sistema.Sistema;
import sistema.alquiler.Alquiler;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.NoExistenteException;
import sistema.filtro.FiltroSimple;
import sistema.ranking.Ranking;
import sistema.usuario.Usuario;

public class Visualizacion {
	Inmueble inmueble;
	Sistema sistema;

	public Visualizacion(Inmueble inmueble, Sistema sistema) {
		this.inmueble = inmueble;
		this.sistema = sistema;
	}

	public Inmueble getInmueble() {
		return this.inmueble;
	}

	public Usuario getPropietario() {
		return this.getInmueble().getPropietario();
	}

	public List<String> getComentarios() {
		return this.sistema.getComentarios(this.getInmueble());
	}

	public List<Ranking> getPuntajesEnCategoria(Categoria categoria) throws NoExistenteException {
		return this.sistema.getValoracionesPorCategoria(this.getInmueble(), categoria);
	}

	public List<Ranking> getPuntajesDePropietario() {
		return this.sistema.getValoraciones(getPropietario());
	}

	public double getPuntajePromedioDePropietario() {
		return this.sistema.getPromedioValoraciones(getPropietario());
	}

	public double getPuntajePromedio() {
		return this.sistema.getPromedioValoraciones(getInmueble());
	}

	public double getPuntajePromedioPorCategoria(Categoria categoria) throws NoExistenteException {
		return this.sistema.getPromedioValoracionesPorCategoria(getInmueble(), categoria);
	}

	public String getPropietarioAntiguedad() {
		LocalDate hoy = LocalDate.now();
		Period period = Period.between(this.getPropietario().getFechaCreacion(), hoy);

		int años = period.getYears();
		int meses = period.getMonths();
		int dias = period.getDays();

		return "Propietario hace " + años + " años, " + meses + " meses y " + dias + " días.";
	}

	public int getVecesInmuebleAlquilado() {
		return this.getInmueble().getVecesAlquilado();
	}

	public int getVecesPropietarioAlquiloInmuebles() {
		return this.sistema.getInmuebles(getPropietario()).stream().mapToInt(i -> i.getVecesAlquilado()).sum();
	}

	public List<Inmueble> getPropietarioInmueblesAlquilados() {
		return new FiltroSimple<Inmueble>(i -> i.getVecesAlquilado() > 0)
				.filtrarLista(this.sistema.getInmuebles(getPropietario()));
	}
}
