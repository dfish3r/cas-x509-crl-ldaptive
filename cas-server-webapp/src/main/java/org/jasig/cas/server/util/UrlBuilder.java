package org.jasig.cas.server.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */

@Named("urlBuilder")
public final class UrlBuilder {

    @NotNull
    private List<String> acceptableParameters = Arrays.asList("service", "method", "gateway", "renew");

    public String generateQueryString(final ExternalContext requestContext) {
        return generateQueryString(requestContext, new String[0]);
    }

    public String generateQueryString(final ExternalContext requestContext, final String parameterToIgnore) {
        return generateQueryString(requestContext, Arrays.asList(parameterToIgnore));
    }

    public String generateQueryString(final ExternalContext requestContext, final String... parametersToIgnore) {
        return generateQueryString(requestContext, Arrays.asList(parametersToIgnore));
    }

    public String generateQueryString(final ExternalContext externalContext, final List<String> parametersToIgnore) {
        if (!(externalContext instanceof ServletExternalContext)) {
            return null;
        }

        final ServletExternalContext servletExternalContext = (ServletExternalContext) externalContext;
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletExternalContext.getNativeRequest();

        final StringBuilder builder = new StringBuilder(StringUtils.length(httpServletRequest.getQueryString()));

        for (final Map.Entry<String, String[]> entry : (Set<Map.Entry<String, String[]>>) httpServletRequest.getParameterMap().entrySet()) {
            final String key = entry.getKey();
            final String[] values = entry.getValue();

            if (this.acceptableParameters.contains(key) && !parametersToIgnore.contains(key)) {
                for (final String value : values) {
                    builder.append(encode(key));
                    builder.append("=");
                    builder.append(value);
                    builder.append("&");
                }
            }
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() -1);
        }

        return builder.toString();
    }

    private String encode(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAcceptableParameters(final List<String> acceptableParameters) {
        this.acceptableParameters = acceptableParameters;
    }
}
