package sistema.managers;
import sistema.Usuario.Usuario;
import sistema.alquiler.Alquiler;
import sistema.reserva.Reserva;
import java.util.List;

public class ReservaManager {
	private List<Reserva> reservas;
	private int siguienteId; 
	
	public void crearReserva(Alquiler alquiler, Usuario usuario) {
		//validaciones

		Reserva reservaNueva = new Reserva() ;
//		id y datos que correspondan
		this.reservas.add(reservaNueva);
	}
}
