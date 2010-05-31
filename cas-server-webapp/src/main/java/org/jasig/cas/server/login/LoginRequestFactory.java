package org.jasig.cas.server.login;

import com.github.inspektr.common.web.ClientInfoHolder;
import org.jasig.cas.server.util.WebUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Constructs a new LoginRequest based on parameters supplied as part of the request.
 * <p>
 * This is essentially the same as iterating through all of the argument extractors in CAS3.4
 * <p>
 * If it can't construct a {@link org.jasig.cas.server.login.ServiceAccessRequest} it creates a default, non-service
 * login request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class LoginRequestFactory {

    @NotNull
	private CookieGenerator cookieGenerator;

    @NotNull
    @Size(min=1)
    private List<ServiceAccessRequestFactory> serviceAccessRequestFactories;

    public LoginRequestFactory(final CookieGenerator cookieGenerator, final List<ServiceAccessRequestFactory> serviceAccessRequestFactories) {
        this.cookieGenerator = cookieGenerator;
        this.serviceAccessRequestFactories = serviceAccessRequestFactories;
    }

	public String createLoginRequest(final RequestContext context) throws Exception {
        final String remoteIpAddress = ClientInfoHolder.getClientInfo().getClientIpAddress();
		final String sessionIdentifier = WebUtils.getCookieValue(this.cookieGenerator, context.getExternalContext());
		final Map parameters = context.getRequestParameters().asMap();

        for (final ServiceAccessRequestFactory serviceAccessRequestFactory : this.serviceAccessRequestFactories) {
            final LoginRequest loginRequest = serviceAccessRequestFactory.getServiceAccessRequest(sessionIdentifier, remoteIpAddress, parameters);

            if (loginRequest != null) {
                context.getFlowScope().put("loginRequest", loginRequest);
                return "success";
            }
        }

        context.getFlowScope().put("loginRequest", new DefaultLoginRequestImpl(sessionIdentifier, remoteIpAddress, false, false,null));
        return "error";
	}
    
}
