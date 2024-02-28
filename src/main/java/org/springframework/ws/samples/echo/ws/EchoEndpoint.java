/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ws.samples.echo.ws;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.xml.StaxUtils;
import org.springframework.ws.samples.echo.service.EchoService;
import org.springframework.ws.server.endpoint.AbstractStaxStreamPayloadEndpoint;

import jakarta.xml.bind.JAXBElement;

/**
 * Simple echoing Web service endpoint. Uses a <code>EchoService</code> to create a response string.
 *
 * @author Ingo Siebert
 * @author Arjen Poutsma
 * 
 * @author pauld - updated original echo server code to use AbstractStaxStreamPayloadEndpoint
 */
public class EchoEndpoint extends AbstractStaxStreamPayloadEndpoint {

	/**
	 * Namespace of both request and response.
	 */
	public static final String NAMESPACE_URI = "http://www.springframework.org/spring-ws/samples/echo";

	/**
	 * The local name of the expected request.
	 */
	public static final String ECHO_REQUEST_LOCAL_NAME = "echoRequest";

	/**
	 * The local name of the created response.
	 */
	public static final String ECHO_RESPONSE_LOCAL_NAME = "echoResponse";

	@Autowired
	private EchoService echoService;

	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;

	/**
	 * Reads the given <code>requestElement</code>, and sends a the response back.
	 *
	 * @param requestElement the contents of the SOAP message as DOM elements
	 * @return the response element
	 */
	protected void invokeInternal(XMLStreamReader xsr, XMLStreamWriter xsw) throws Exception {
		Source source = StaxUtils.createStaxSource(xsr);
		Object poe = jaxb2Marshaller.unmarshal(source);
		JAXBElement<String> requestBean = (JAXBElement<String>)poe;
		
		String echo = echoService.echo(requestBean.getValue());

		xsw.writeStartDocument();
		xsw.writeDefaultNamespace(NAMESPACE_URI);
		xsw.writeStartElement(ECHO_RESPONSE_LOCAL_NAME);
		xsw.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		xsw.writeNamespace("", NAMESPACE_URI);
		xsw.writeCharacters(echo);
		xsw.writeEndElement();
		xsw.flush();
	}

}
