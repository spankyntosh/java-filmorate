package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Event {

    private int eventId;
    private int userId;
    private int entityId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", this.eventId);
        map.put("user_id", this.userId);
        map.put("entity_id", this.entityId);
        map.put("timestamp", this.timestamp);
        map.put("event_type", this.eventType);
        map.put("operation", this.operation);
        return map;
    }
}
