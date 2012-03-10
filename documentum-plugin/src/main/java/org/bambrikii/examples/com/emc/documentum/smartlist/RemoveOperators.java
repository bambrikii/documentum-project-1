package org.bambrikii.examples.com.emc.documentum.smartlist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bambrikii.examples.com.emc.documentum.ExpressionSet;
import org.bambrikii.examples.com.emc.documentum.SimpleAttributeExpression;
import org.bambrikii.examples.com.emc.documentum.SmartListDefinition;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;

public class RemoveOperators {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static Logger logger = Logger.getLogger(RemoveOperators.class);
	private Map<String, String> operators;
	private IDfSession session;

	public Map<String, String> getOperators() {
		return operators;
	}

	public void setOperators(Map<String, String> operators) {
		this.operators = operators;
	}

	public IDfSession getSession() {
		return session;
	}

	public void setSession(IDfSession session) {
		this.session = session;
	}

	public void removeAll() {
		IDfQuery query = new DfQuery("select r_object_id from dm_smart_list where query_type = 'query_builder' ");
		try {
			IDfCollection coll = query.execute(session, IDfQuery.DF_EXECREAD_QUERY);
			while (coll.next()) {
				String rObjectId = coll.getString("r_object_id");
				IDfSysObject obj = (IDfSysObject) session.getObject(new DfId(rObjectId));
				try {
					InputStream is = obj.getContent();
					StringWriter sw = new StringWriter();
					IOUtils.copy(is, sw, DEFAULT_CHARSET);
					logger.debug("Source xml: " + sw.toString());
					String resultXml = remove(sw.toString());
					logger.debug("Result xml: " + resultXml.toString());
					OutputStream os = new ByteArrayOutputStream();
					os.write(resultXml.getBytes(DEFAULT_CHARSET));
					obj.setContent((ByteArrayOutputStream) os);
					obj.save();
				} catch (IOException ex) {
					logger.error(ex);
					ex.printStackTrace();
				} catch (JAXBException ex) {
					logger.error(ex);
					ex.printStackTrace();
				}
			}
			coll.close();
		} catch (DfException ex) {
			logger.error(ex);
			ex.printStackTrace();
		}
	}

	public String remove(String input) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(SmartListDefinition.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		SmartListDefinition response = (SmartListDefinition) unmarshaller.unmarshal(new ByteArrayInputStream(input
				.getBytes()));
		System.out.println(response);

		for (ExpressionSet exprSet1 : response.getQueryBuilder().getExpressionSet().getExpressionSet()) {
			recurseExpessionSet(exprSet1);
		}

		StringWriter sw2 = new StringWriter();

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(response, sw2);
		return sw2.toString();
	}

	private void recurseExpessionSet(ExpressionSet exprSet1) {
		SimpleAttributeExpression simpleAttributeExpression = exprSet1.getSimpleAttributeExpression();
		if (simpleAttributeExpression != null) {
			String searchOp = simpleAttributeExpression.getSearchOperation();
			for (String removeOp : operators.keySet()) {
				if (removeOp.equals(searchOp)) {
					simpleAttributeExpression.setSearchOperation(operators.get(removeOp));
				}
			}
		}
		for (ExpressionSet exprSet : exprSet1.getExpressionSet()) {
			recurseExpessionSet(exprSet);
		}
	}
}
