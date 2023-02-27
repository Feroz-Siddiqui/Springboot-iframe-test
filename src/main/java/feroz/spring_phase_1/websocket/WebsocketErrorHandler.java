package feroz.spring_phase_1.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class WebsocketErrorHandler extends StompSubProtocolErrorHandler {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload,
			Throwable cause, StompHeaderAccessor clientHeaderAccessor) {

		logger.error("error --> " + cause.getLocalizedMessage());
		logger.error("error clientHeaderAccessor --> " + clientHeaderAccessor.toString());
		logger.error("error errorHeaderAccessor --> " + errorHeaderAccessor.toString());

		// TODO Auto-generated method stub
		return super.handleInternal(errorHeaderAccessor, errorPayload, cause, clientHeaderAccessor);
	}

	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
		logger.error("handleClientMessageProcessingError error --> " + ex.getLocalizedMessage());

		// TODO Auto-generated method stub
		return super.handleClientMessageProcessingError(clientMessage, ex);
	}
}