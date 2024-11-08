package sistema.ranking;

import java.time.LocalDate;
import java.util.List;

import sistema.enums.customEnums.Categoria;
import sistema.exceptions.ServicioNoTerminadoException;
import sistema.exceptions.ValoracionInvalidaException;
import sistema.managers.RankingManager;
import sistema.reserva.Reserva;
import sistema.usuario.Usuario;

public interface Rankeable {
	public boolean mePuedeValorar(Usuario usuario);
}
