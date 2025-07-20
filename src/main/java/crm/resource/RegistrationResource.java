package crm.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import crm.data.StartRegistrationRequest;
import crm.service.UserRegistrationService;

@Path("/api/registration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {

    private static final String USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME = "_uac_sid";

    private final UserRegistrationService userRegistrationService;

    @Inject
    public RegistrationResource(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @POST
    public Response startRegistration(@Valid StartRegistrationRequest request) {
        String sessionId = userRegistrationService.startRegistration(request.email());
        NewCookie cookie = new NewCookie.Builder(USER_ACCOUNT_CREATION_SESSION_ID_COOKIE_NAME)
                .value(sessionId)
                .maxAge(3600)
                .path("/api/registration")
                .httpOnly(true)
                .build();

        return Response.noContent().cookie(cookie).build();
    }
}
