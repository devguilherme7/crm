package crm.service;

public interface UserRegistrationService {

    String startRegistration(String email);

    void verifyCode(String sessionId, String verificationCode);

    void resendVerificationCode(String sessionId);
}
