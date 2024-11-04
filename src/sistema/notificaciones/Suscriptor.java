package sistema.notificaciones;

public interface Suscriptor {
	public void popUp(String message, String color, int fontSize);
	public void publish(String message);
	public void update(EventoNotificador even);
}
