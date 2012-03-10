package org.bambrikii.examples.com.emc.documentum.smartlist.tests;

import java.util.HashMap;

import org.bambrikii.examples.com.emc.documentum.smartlist.RemoveOperators;
import org.junit.Test;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

public class SmartListTests {
	@Test
	public void test1() throws DfException {

		String user = "dmadmin";
		String password = "dmadmin";
		String domain = "documentum";

		RemoveOperators ro = new RemoveOperators();
		ro.setOperators(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("contains", "begins_wth");
				put("ends_with", "begins_with");
				// put("begin wth", "begins_with");
			}
		});

		IDfClient client = new DfClientX().getLocalClient();
		IDfLoginInfo loginInfo = new DfClientX().getLoginInfo();
		loginInfo.setUser(user);
		loginInfo.setPassword(password);

		IDfSession session = client.newSession(domain, loginInfo);
		ro.setSession(session);
		ro.removeAll();

	}
}
