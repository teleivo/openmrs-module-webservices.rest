package org.openmrs.module.webservices.rest.web.v1_0.search.openmrs1_10;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubclassSearchHandler;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_10.TestOrderSubclassHandler1_10;

import java.util.Arrays;

public class TestOrderSubclassSearchHandler1_10 implements DelegatingSubclassSearchHandler<TestOrderSubclassHandler1_10> {

	public static Log log = LogFactory.getLog(TestOrderSubclassSearchHandler1_10.class);

	SearchQuery searchQuery = new SearchQuery.Builder("Allows you to search for TestOrder's by patient and urgency")
			.withOptionalParameters("patient")
			.build();

	private final SearchConfig searchConfig =
			new SearchConfig("default", RestConstants.VERSION_1 + "/order", Arrays.asList("2.0.*"), searchQuery);

	@Override
	public Class<TestOrderSubclassHandler1_10> getDelegatingSubclassHandler() {
		return TestOrderSubclassHandler1_10.class;
	}

	@Override
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}

	@Override
	public PageableResult search(RequestContext context) throws ResponseException {

		log.info("reached");

		return new EmptySearchResult();
	}
}
