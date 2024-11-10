package sistema.notificaciones;

import java.util.HashSet;

import java.util.Set;

public class Listener {

	private Set<Suscriptor> sus;
	private EventoNotificador even;

	public Listener(EventoNotificador even) {
		this.sus = new HashSet<Suscriptor>();
		this.even = even;
	}

	public void add(Suscriptor sus) {
		this.sus.add(sus);
	}

	public void remove(Suscriptor sus) {
		this.sus.remove(sus);
	}

	public void notificar() {
		this.sus.stream().forEach(s -> even.notificarEspecifica(s));
	}

	public boolean esEventoPara(EventoNotificador even2) {
		return this.even.esIgualA(even2);
	}

	public int cantidadSuscriptos() {
		return this.sus.size();
	}

	public boolean contieneA(Suscriptor s) {
		return this.sus.contains(s);
	}
}
