package crm.service;

import crm.data.CreateAccountRequest;

public interface UserRegistrationService {

    String startRegistration(String email);

    void verifyCode(String sessionId, String verificationCode);

    void resendVerificationCode(String sessionId);

    void createAccount(String sessionId, CreateAccountRequest request);
}
