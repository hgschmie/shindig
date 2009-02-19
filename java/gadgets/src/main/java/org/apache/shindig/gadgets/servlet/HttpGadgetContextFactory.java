package org.apache.shindig.gadgets.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shindig.gadgets.GadgetContext;

import com.google.inject.Inject;

/**
 * Creates new GadgetContext objects from Http request and response.
 */
public class HttpGadgetContextFactory {

    @Inject
    public HttpGadgetContextFactory() {
    }

    public GadgetContext getNewGadgetContext(final HttpServletRequest request, final HttpServletResponse response) {
        return new HttpGadgetContext(request);
    }
}
