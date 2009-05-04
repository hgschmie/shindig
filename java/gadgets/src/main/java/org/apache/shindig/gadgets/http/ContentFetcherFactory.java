package org.apache.shindig.gadgets.http;

import org.apache.shindig.gadgets.GadgetException;

public interface ContentFetcherFactory
{

    public abstract HttpResponse fetch(final HttpRequest request) throws GadgetException;

}
