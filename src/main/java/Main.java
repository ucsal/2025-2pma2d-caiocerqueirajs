import br.com.mariojp.solid.dip.*;

public class Main {
    public static void main(String[] args) {
        boolean dryRun = Boolean.parseBoolean(System.getProperty("DRY_RUN", "false"));

        MailSender sender;
        if (dryRun) {
            sender = new NoopMailSender();
        } else {
            SmtpClient smtpClient = new SmtpClient();
            sender = new SmtpMailSender(smtpClient);
        }

        EmailNotifier notifier = new EmailNotifier(sender);
        notifier.welcome(new User("Caio", "caio@example.com"));

        System.out.println("Email enviado!");
    }
}
