package crm.infrastructure.mail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class QuarkusEmailService implements EmailService {

    private final Mailer mailer;

    @Inject
    public QuarkusEmailService(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public void sendVerificationEmail(String to, String verificationCode) {
        mailer.send(Mail.withText(to, "Verify your email address",
                String.format("Your verification code is: %s", verificationCode)));
    }
}
