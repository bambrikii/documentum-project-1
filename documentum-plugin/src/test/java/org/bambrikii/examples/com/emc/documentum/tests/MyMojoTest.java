package org.bambrikii.examples.com.emc.documentum.tests;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.bambrikii.examples.com.emc.documentum.MyMojo;
import org.junit.Assert;

public class MyMojoTest extends AbstractMojoTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSomething() throws Exception {
		File pom = getTestFile("src/test/resources/unit/project-to-test/pom.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		MyMojo myMojo = (MyMojo) lookupMojo("touch", pom);
		Assert.assertNotNull(myMojo);
		myMojo.execute();
	}
}