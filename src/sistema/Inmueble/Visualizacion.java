package sistema.Inmueble;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import sistema.alquiler.Alquiler;
import sistema.enums.customEnums.Categoria;
import sistema.filtro.FiltroSimple;
import sistema.ranking.Ranking;
import sistema.usuario.Propietario;

public class Visualizacion {
	Inmueble inmueble;

	public Visualizacion(Inmueble inmueble) {
		this.inmueble = inmueble;
	}

	public Inmueble getInmueble() {
		return this.inmueble;
	}

	public Propietario getPropietario() {
		return this.getInmueble().getPropietario();
	}

	public List<String> getComentarios() {
		return this.getInmueble().getComentarios();
	}

	public List<Ranking> getPuntajesEnCategoria(Categoria categoria) {
		return this.getInmueble().getValoracionesPorCategoria(categoria);
	}

	public List<Ranking> getPuntajesDePropietario() {
		return this.getPropietario().getValoraciones();
	}

	public double getPuntajePromedioDePropietario() {
		return this.getPropietario().getPromedioValoraciones();
	}

	public double getPuntajePromedio() {
		return this.getInmueble().getPromedioValoraciones();
	}

	public double getPuntajePromedioPorCategoria(Categoria categoria) {
		return this.getInmueble().getPromedioValoracionesPorCategoria(categoria);
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
		return this.getPropietario().getInmuebles().stream().mapToInt(i -> i.getVecesAlquilado()).sum();
	}

	public List<Inmueble> getPropietarioInmueblesAlquilados() {
		return new FiltroSimple<Inmueble>(i -> i.getVecesAlquilado() > 0)
				.filtrarLista(this.getPropietario().getInmuebles());
	}
}
