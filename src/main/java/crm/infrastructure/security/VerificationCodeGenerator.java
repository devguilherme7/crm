package crm.infrastructure.security;

public interface VerificationCodeGenerator {

    String generate(int length);
}
