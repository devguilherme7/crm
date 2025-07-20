package crm.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import crm.data.RegistrationSession;
import crm.infrastructure.mail.EmailService;
import crm.infrastructure.security.SecureCodeGenerator;
import crm.repository.RegistrationSessionRepository;
import crm.repository.UserRepository;

@ApplicationScoped
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_IN_SECONDS = 900; // 15 minutes

    private final UserRepository userRepository;
    private final RegistrationSessionRepository sessionRepository;
    private final SecureCodeGenerator codeGenerator;
    private final EmailService emailService;

    @Inject
    public UserRegistrationServiceImpl(
            UserRepository userRepository,
            RegistrationSessionRepository sessionRepository,
            SecureCodeGenerator codeGenerator,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.codeGenerator = codeGenerator;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public String startRegistration(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new WebApplicationException("Endereço de e-mail já cadastrado.", Response.Status.CONFLICT);
        }

        Optional<RegistrationSession> existingSessionOpt = sessionRepository.findByEmail(email);
        if (existingSessionOpt.isPresent()) {
            return existingSessionOpt.get().getId();
        }

        String sessionId = String.valueOf(UUID.randomUUID());
        String verificationCode = codeGenerator.generate(VERIFICATION_CODE_LENGTH);

        var codeExpiresAt = Instant.now().plusSeconds(CODE_EXPIRATION_IN_SECONDS);
        var session = new RegistrationSession(sessionId, email);
        session.setVerificationCode(verificationCode, codeExpiresAt);

        sessionRepository.save(session);
        emailService.sendVerificationEmail(email, verificationCode);

        return sessionId;
    }
}
