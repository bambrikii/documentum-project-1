package org.bambrikii.examples.com.emc.documentum.tests;

import org.junit.Test;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.common.DfException;

public class Test2 {
	public void method1(Object obj) {
		System.out.println("obj");
	}

	public void method1(String str) {
		System.out.println("str");
	}

	// public void method1(Integer int1) {
	// System.out.println("int");
	// }

	@Test
	public void test1() {
		Test2 t = new Test2();
		t.method1(null);
	}

	@Test
	public void testGetLocalClientt() throws DfException {
		IDfClient client = DfClient.getLocalClient();
		System.out.println(client);
	}
}
