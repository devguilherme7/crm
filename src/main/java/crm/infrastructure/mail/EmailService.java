package crm.infrastructure.mail;

public interface EmailService {

    void sendVerificationEmail(String to, String verificationCode);
}
