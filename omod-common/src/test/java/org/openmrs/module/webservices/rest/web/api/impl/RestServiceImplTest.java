package org.openmrs.module.webservices.rest.web.api.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestUtil;
import org.openmrs.module.webservices.rest.web.resource.api.Resource;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.response.InvalidSearchException;
import org.openmrs.util.OpenmrsConstants;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link RestServiceImpl}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class  })
public class RestServiceImplTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Context.class);
	}


	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies throw exception if no handler with id
	 */
	@Test
	public void getSearchHandler_shouldThrowExceptionIfNoHandlerWithId() throws Exception {

		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("none", "concept", OpenmrsConstants.OPENMRS_VERSION_SHORT, new SearchQuery.Builder("Fuzzy search")
		        .withRequiredParameters("q").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));
		service.initialize();

		System.out.println(service.resourceDefinitionsByNames.size());
		System.out.println(service.searchHandlersByIds.size());
		System.out.println(service.allSearchHandlers.size());

		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("s", new String[] { "conceptByMapping" });

		expectedException.expect(InvalidSearchException.class);
		expectedException.expectMessage("The search with id 'conceptByMapping' for '" + searchConfig.getSupportedResource() + "' resource is not recognized");
		service.getSearchHandler("concept", parameters);
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies return handler by id if exists
	 */
	@Test
	public void getSearchHandler_shouldReturnHandlerByIdIfExists() throws Exception {

		RestServiceImpl service = new RestServiceImpl();

		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", OpenmrsConstants.OPENMRS_VERSION_SHORT, new SearchQuery.Builder(
		        "Fuzzy search").withRequiredParameters("q").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));
		service.initialize();

		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("s", new String[] { "conceptByMapping" });
		SearchHandler searchHandler2 = service.getSearchHandler("concept", parameters);
		assertThat(searchHandler2, is(searchHandler));
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies throw ambiguous exception if case 1
	 */
	@Test
	public void getSearchHandler_shouldThrowAmbiguousExceptionIfCase1() throws Exception {
		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName", "code").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));

		SearchHandler searchHandler2 = mock(SearchHandler.class);
		SearchConfig searchConfig2 = new SearchConfig("conceptByMapping2", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName", "code").build());
		when(searchHandler2.getSearchConfig()).thenReturn(searchConfig2);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler2 ));

		RestUtil.disableContext(); //to avoid a Context call
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("sourceName", new String[] { "some name" });
		parameters.put("code", new String[] { "some code" });
		
		try {
			service.getSearchHandler("concept", parameters);
			fail();
		}
		catch (InvalidSearchException e) {}
	}
	
	public Set<SearchQuery> newParameters(SearchQuery... parameters) {
		return new HashSet<SearchQuery>(Arrays.asList(parameters));
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies return handler if case 2
	 */
	@Test
	public void getSearchHandler_shouldReturnHandlerIfCase2() throws Exception {
		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").withOptionalParameters("code").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));

		SearchHandler searchHandler2 = mock(SearchHandler.class);
		SearchConfig searchConfig2 = new SearchConfig("conceptByMapping2", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").build());
		when(searchHandler2.getSearchConfig()).thenReturn(searchConfig2);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler2 ));

		RestUtil.disableContext(); //to avoid a Context call
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("sourceName", new String[] { "some name" });
		parameters.put("code", new String[] { "some code" });
		SearchHandler searchHandler3 = service.getSearchHandler("concept", parameters);
		
		assertThat(searchHandler3, is(searchHandler));
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies throw ambiguous exception if case 3
	 */
	@Test
	public void getSearchHandler_shouldThrowAmbiguousExceptionIfCase3() throws Exception {
		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").withOptionalParameters("code").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));

		SearchHandler searchHandler2 = mock(SearchHandler.class);
		SearchConfig searchConfig2 = new SearchConfig("conceptByMapping2", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").build());
		when(searchHandler2.getSearchConfig()).thenReturn(searchConfig2);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler2 ));

		RestUtil.disableContext(); //to avoid a Context call
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("sourceName", new String[] { "some name" });
		
		try {
			service.getSearchHandler("concept", parameters);
			fail();
		}
		catch (InvalidSearchException e) {}
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies return null if too few parameters
	 */
	@Test
	public void getSearchHandler_shouldReturnNullIfTooFewParameters() throws Exception {
		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").withOptionalParameters("code").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));

		SearchHandler searchHandler2 = mock(SearchHandler.class);
		SearchConfig searchConfig2 = new SearchConfig("conceptByMapping2", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").build());
		when(searchHandler2.getSearchConfig()).thenReturn(searchConfig2);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler2 ));

		RestUtil.disableContext(); //to avoid a Context call
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("code", new String[] { "some name" });
		
		SearchHandler searchHandler3 = service.getSearchHandler("concept", parameters);
		assertThat(searchHandler3, nullValue());
	}
	
	/**
	 * @see RestServiceImpl#getSearchHandler(String,Map)
	 * @verifies return null if resource does not match
	 */
	@Test
	public void getSearchHandler_shouldReturnNullIfResourceDoesNotMatch() throws Exception {
		RestServiceImpl service = new RestServiceImpl();
		
		SearchHandler searchHandler = mock(SearchHandler.class);
		SearchConfig searchConfig = new SearchConfig("conceptByMapping", "concept", "1.8.*", new SearchQuery.Builder(
		        "description").withRequiredParameters("sourceName").build());
		when(searchHandler.getSearchConfig()).thenReturn(searchConfig);
		when(Context.getRegisteredComponents(SearchHandler.class)).thenReturn(Arrays.asList( searchHandler ));

		RestUtil.disableContext(); //to avoid a Context call
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("sourceName", new String[] { "some name" });
		
		SearchHandler searchHandler2 = service.getSearchHandler("nonexistingresource", parameters);
		assertThat(searchHandler2, nullValue());
	}
	
	@Test
	public void getResourceBySupportedClass_shouldReturnTheMostExactMatch() throws Exception {
		//Given
		RestServiceImpl service = new RestServiceImpl();
		
		Resource personResource = mock(Resource.class);
		Resource patientResource = mock(Resource.class);
		
		//Mocked for deterministic order
		service.resourceDefinitionsByNames = new LinkedHashMap<String, RestServiceImpl.ResourceDefinition>();
		service.resourcesBySupportedClasses = new LinkedHashMap<Class<?>, Resource>();
		
		service.resourcesBySupportedClasses.put(Person.class, personResource);
		service.resourcesBySupportedClasses.put(Patient.class, patientResource);
		
		//When
		Resource resource = service.getResourceBySupportedClass(ChildPatient.class);
		
		//Then
		assertThat(resource, is(patientResource));
	}
	
	public static class ChildPatient extends Patient {};
}
