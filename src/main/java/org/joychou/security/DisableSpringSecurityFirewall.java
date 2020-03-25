package org.joychou.security;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DisableSpringSecurityFirewall implements HttpFirewall {

    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
        return new FirewalledRequest(request) {
            @Override
            public void reset() {
            }
        };
    }

    @Override
    public HttpServletResponse getFirewalledResponse(HttpServletResponse response) {
        return response;
    }
}
