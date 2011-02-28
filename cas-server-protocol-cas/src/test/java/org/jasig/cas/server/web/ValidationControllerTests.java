package org.jasig.cas.server.web;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class ValidationControllerTests {

    private ValidationController validationController;

    public void cas10Protocol() throws IOException {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final StringWriter writer = new StringWriter();
        final ModelAndView modelAndView = this.validationController.validateCas10Request(true, "http://www.cnn.com", "ST-100", mockHttpServletRequest, writer);
        assertNull(modelAndView);
    }

    @Test
    public void cas20Protocol() {

    }

    public void cas20ProtocolWithPGT() {

    }



}
