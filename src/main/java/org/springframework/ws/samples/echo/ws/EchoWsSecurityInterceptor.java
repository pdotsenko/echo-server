package org.springframework.ws.samples.echo.ws;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.WsSecuritySecurementException;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

public class EchoWsSecurityInterceptor extends Wss4jSecurityInterceptor {

    @Override
	protected void secureMessage(SoapMessage soapMessage, MessageContext messageContext)
			throws WsSecuritySecurementException {
		super.secureMessage(soapMessage, messageContext);
		soapMessage.getDocument().getChildNodes(); // <-- trigger initialization/population of elements (deferred by lazy loading)
	}

}
