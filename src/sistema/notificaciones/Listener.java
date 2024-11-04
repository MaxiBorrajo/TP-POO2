package sistema.notificaciones;

import java.util.ArrayList;
import java.util.List;

public class Listener {
	
	private List<Suscriptor> sus;
	private EventoNotificador even;
	
	public Listener(EventoNotificador even) {
		this.sus = new ArrayList<Suscriptor>();
		this.even = even;
	}
	
	public void add(Suscriptor sus) {
		this.sus.add(sus);
	}
	
	public void remove(Suscriptor sus) {
		this.sus.remove(sus);
	}

	public void notificar() {
		
	}

	public boolean esEventoPara(EventoNotificador even2) {
		// TODO Auto-generated method stub
		return this.even.esIgualA(even2);
	}
}
