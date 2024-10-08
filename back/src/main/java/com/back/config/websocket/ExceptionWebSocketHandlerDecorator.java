package com.back.config.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class ExceptionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {
    private static final Log logger = LogFactory.getLog(ExceptionWebSocketHandlerDecorator.class);


    public ExceptionWebSocketHandlerDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    public static void tryCloseWithError(WebSocketSession session, Throwable exception, Log logger) {
        if (logger.isErrorEnabled()) {
            logger.error("Closing session due to exception for " + session, exception);
        }
        if (session.isOpen()) {
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (Throwable ex) {
                // ignore
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            getDelegate().afterConnectionEstablished(session);
        } catch (Exception ex) {
            tryCloseWithError(session, ex, logger);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            getDelegate().handleMessage(session, message);
        } catch (Exception ex) {
            tryCloseWithError(session, ex, logger);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        try {
            getDelegate().handleTransportError(session, exception);
        } catch (Exception ex) {
            tryCloseWithError(session, ex, logger);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        try {
            getDelegate().afterConnectionClosed(session, closeStatus);
        } catch (Exception ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Unhandled exception after connection closed for " + this, ex);
            }
        }
    }
}
