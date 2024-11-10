package sistema.mailSender;

public interface MailSender {
	public void sendEmail(String from, String to, String subject, String text);
}
