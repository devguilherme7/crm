package crm.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import crm.data.StartRegistrationRequest;
import crm.data.VerifyCodeRequest;
import crm.service.UserRegistrationService;

@Path("/api/registrations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Registration")
public class RegistrationResource {

    private static final String USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME = "_uac_sid";

    private final UserRegistrationService userRegistrationService;

    @Inject
    public RegistrationResource(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @POST
    @Operation(summary = "Start registration",
            description = "Starts the registration process by sending a verification code to the user's email.")
    @APIResponse(responseCode = "204", description = "Registration started successfully with session cookie set")
    @APIResponse(responseCode = "400", description = "Invalid email or registration failed")
    public Response startRegistration(@Valid StartRegistrationRequest request) {
        String sessionId = userRegistrationService.startRegistration(request.email());
        NewCookie cookie = new NewCookie.Builder(USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME)
                .value(sessionId)
                .maxAge(3600)
                .path("/api/registrations")
                .httpOnly(true)
                .build();

        return Response.noContent().cookie(cookie).build();
    }

    @POST
    @Path("/verifyCode")
    @Operation(
            summary = "Email verification",
            description = "Verify the email address using the 6-digit code sent in Step 1.")
    @APIResponse(responseCode = "204", description = "Email verified successfully.")
    @APIResponse(
            responseCode = "400",
            description = "Invalid verification code, session expired, email already verified, or missing session cookie")
    public Response verifyCode(
            @CookieParam(USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME) String sessionId,
            @Valid VerifyCodeRequest request) {

        userRegistrationService.verifyCode(sessionId, request.verificationCode());
        return Response.noContent().build();
    }

    @POST
    @Path("/resendVerificationCode")
    @Operation(
            summary = "Resend verification code",
            description = "Resend the 6-digit verification code to the registered email.")
    @APIResponse(responseCode = "204", description = "Verification code resent successfully")
    @APIResponse(responseCode = "400", description = "Email already verified, or invalid session")
    public Response resendVerificationCode(
            @CookieParam(USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME) String sessionId) {

        userRegistrationService.resendVerificationCode(sessionId);
        return Response.noContent().build();
    }
}
