package org.openmrs.module.webservices.rest.web;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.webservices.rest.web.resource.api.Resource;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link OpenmrsClassScanner}.
 */
public class OpenmrsClassScannerTest {

	/**
	 * @verifies return classes if given existing type and concrete true
	 * @see OpenmrsClassScanner#getClasses(Class, boolean)
	 */
	@Test
	public void getClasses_shouldReturnClassesIfGivenExistingTypeAndConcreteTrue() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return empty list if given existing type and concrete false
	 * @see OpenmrsClassScanner#getClasses(Class, boolean)
	 */
	@Test
	public void getClasses_shouldReturnEmptyListIfGivenExistingTypeAndConcreteFalse() throws Exception {

		List list;
//		list = OpenmrsClassScanner.getInstance().getClasses(Resource.class, false);
//		for (Object resource : list) {
//			System.out.println(resource);
//		}

		list = OpenmrsClassScanner.getInstance().getClasses(OpenmrsClassScannerTest.class, false);
		for (Object resource : list) {
			System.out.println(resource);
		}
//		assertTrue(list.isEmpty());
//		assertThat(list, is(empty()));
	}

	/**
	 * @verifies return empty list if given type cannot be found on the classpath
	 * @see OpenmrsClassScanner#getClasses(Class, boolean)
	 */
	@Test
	public void getClasses_shouldReturnEmptyListIfGivenTypeCannotBeFoundOnTheClasspath() throws Exception {

		List list;
		list = OpenmrsClassScanner.getInstance().getClasses(OpenmrsClassScannerTest.class, false);
		assertThat(list, is(empty()));
	}
}
