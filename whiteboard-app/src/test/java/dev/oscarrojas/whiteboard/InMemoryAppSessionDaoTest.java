package dev.oscarrojas.whiteboard;

import dev.oscarrojas.whiteboard.canvas.Canvas;
import dev.oscarrojas.whiteboard.ws.protocol.AppMessageBinaryEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InMemoryAppSessionDaoTest {

    AppMessageBinaryEncoder encoder = new AppMessageBinaryEncoder();

    @Test
    void getAllByConnectionCountRange_returnsSessionsWithinRange() {
        AppSessionDao dao = new InMemoryAppSessionDao();
        AppSession session1 = new AppSession(
            UUID.randomUUID().toString(), new Canvas(1, 1),
            encoder
        );
        WebSocketSession ws1 = mock(StandardWebSocketSession.class);
        when(ws1.getId()).thenReturn("ws1");
        WebSocketSession ws2 = mock(StandardWebSocketSession.class);
        when(ws2.getId()).thenReturn("ws2");

        dao.save(session1);

        List<AppSession> sessions;
        // connections in session = 0
        sessions = dao.getAllByConnectionCountRange(0, 1);
        assertEquals(1, sessions.size());

        session1.addConnection(ws1);
        dao.save(session1);
        // connections in session = 1
        sessions = dao.getAllByConnectionCountRange(0, 1);
        assertEquals(1, sessions.size());

        session1.addConnection(ws2);
        dao.save(session1);
        // connections in session = 2 (out of range)
        sessions = dao.getAllByConnectionCountRange(0, 1);
        assertEquals(0, sessions.size());
    }

    @Test
    void save_savesCopyOfSession() {
        AppSessionDao dao = new InMemoryAppSessionDao();
        AppSession session1 = new AppSession(
            UUID.randomUUID().toString(), new Canvas(1, 1),
            encoder
        );
        dao.save(session1);

        WebSocketSession ws1 = mock(StandardWebSocketSession.class);
        when(ws1.getId()).thenReturn("ws1");

        // mutate session1 without saving
        session1.addConnection(ws1);

        // should return originally saved copy of session1 without mutations
        Optional<AppSession> session1Copy = dao.get(session1.getId());

        assertTrue(session1Copy.isPresent());
        assertNotSame(session1, session1Copy.get());
        assertFalse(session1Copy.get().hasConnection(ws1.getId()));

    }

}