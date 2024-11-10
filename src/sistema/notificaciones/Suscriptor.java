package sistema.notificaciones;

public interface Suscriptor {
	public void update(EventoNotificador even);
	public void publish(String message);
	public void popUp(String message, String color, int fontSize);
}
