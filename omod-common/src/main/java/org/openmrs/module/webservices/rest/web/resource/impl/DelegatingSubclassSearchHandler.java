package org.openmrs.module.webservices.rest.web.resource.impl;

import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;

/**
 * Represents a {@code SearchHandler} handling searches for a {@code DelegatingSubclassHandler}.
 * 
 * @param <T> the subclass handler for which the search handler is written
 */
public interface DelegatingSubclassSearchHandler<T extends DelegatingSubclassHandler> extends SearchHandler {
	
	/**
	 * Convenience method that lets you retrieve the declared {@code DelegatingSubclassHandler} at
	 * runtime without needing to use introspection yourself.
	 * 
	 * @return the delegating subclass handler for which the search handler is written
	 */
	Class<T> getDelegatingSubclassHandler();
}
