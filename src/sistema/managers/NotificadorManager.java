package sistema.managers;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import sistema.notificaciones.EventoNotificador;
import sistema.notificaciones.Listener;
import sistema.notificaciones.Suscriptor;
import sistema.exceptions.*;

public class NotificadorManager {
	
	private Set<Listener> sus;
	
	public NotificadorManager() {
		this.sus = new HashSet<Listener>();
	}
	
	public void notify(EventoNotificador even) {
	
		this.sus.stream().filter(x -> x.esEventoPara(even)).findFirst().ifPresent(x ->x.notificar());
	
	}
	
	public void addSuscriptor(Suscriptor sus , EventoNotificador even) {
		Listener l = this.sus.stream().filter(x -> x.esEventoPara(even)).findFirst().orElse(new Listener(even));
		
		this.sus.add(l);
		l.add(sus);
	
	}
	
	public void removeSuscriptor(Suscriptor sus, EventoNotificador even) throws NoSuscriptoAlEvento {
		Listener l = this.sus.stream().filter(x -> x.esEventoPara(even)).findFirst().orElseThrow(() -> new NoSuscriptoAlEvento("Ese evento no existe"));
		
		l.remove(sus);
	}

	public int cantidadSuscriptores() {
		// TODO Auto-generated method stub
		return this.sus.stream().mapToInt(l -> l.cantidadSuscriptos() ).sum();
	}

	public boolean estaRegistrado(Suscriptor s, EventoNotificador even) throws NoSuscriptoAlEvento {
		// TODO Auto-generated method stub
		Listener l = this.sus.stream().filter(x -> x.esEventoPara(even)).findFirst().orElseThrow(() -> new NoSuscriptoAlEvento("Ese evento no existe"));
		
		return l.contieneA(s);
	}
	
}
