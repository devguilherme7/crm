package crm.infrastructure.security;

import java.security.SecureRandom;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SecureVerificationCodeGenerator implements VerificationCodeGenerator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate(int length) {
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            codeBuilder.append(random.nextInt(10));
        }

        return codeBuilder.toString();
    }
}
