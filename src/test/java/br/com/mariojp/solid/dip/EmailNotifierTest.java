package br.com.mariojp.solid.dip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailNotifierTest {

    private final User user = new User("Caio", "caio@example.com");

    @Test
    void dryRunShouldNotTouchSmtp() {
        // garante que o SMTP não está disponível
        System.clearProperty("SMTP_AVAILABLE");

        // em dry-run, injetamos diretamente NoopMailSender
        MailSender noop = new NoopMailSender();
        EmailNotifier notifier = new EmailNotifier(noop);

        // não deve lançar exceção mesmo sem SMTP
        assertDoesNotThrow(
                () -> notifier.welcome(user),
                "Em dry-run, o NoopMailSender não deve acionar o SmtpClient"
        );
    }

    @Test
    void prodShouldUseSmtpWithoutError() {
        // simula disponibilidade de SMTP em produção
        System.setProperty("SMTP_AVAILABLE", "true");

        // injetamos o SmtpMailSender com client real
        SmtpClient realClient = new SmtpClient();
        MailSender smtpSender = new SmtpMailSender(realClient);
        EmailNotifier notifier = new EmailNotifier(smtpSender);

        // deve enviar sem lançar exceção
        assertDoesNotThrow(
                () -> notifier.welcome(user),
                "Em produção, o SmtpMailSender deve funcionar sem erros"
        );
    }
}
