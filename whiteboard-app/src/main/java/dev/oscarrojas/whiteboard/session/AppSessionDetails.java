package dev.oscarrojas.whiteboard.session;

import java.util.List;

public record AppSessionDetails(String id, List<String> users) {
}
