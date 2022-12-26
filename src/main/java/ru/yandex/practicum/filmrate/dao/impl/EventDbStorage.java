package ru.yandex.practicum.filmrate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmrate.dao.EventStorage;
import ru.yandex.practicum.filmrate.model.Directory;
import ru.yandex.practicum.filmrate.model.Event;
import ru.yandex.practicum.filmrate.model.EventOperation;
import ru.yandex.practicum.filmrate.model.EventType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class EventDbStorage implements EventStorage {
	private final JdbcTemplate jdbcTemplate;
	private HashMap<String, Integer> eventOperation = new HashMap<>();
	private HashMap<String, Integer> eventType = new HashMap<>();

	@Autowired
	public EventDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Event> getUserEvents(long id) {
		return jdbcTemplate.query("SELECT fe.event_id, fe.time_feed, " +
						"et.name AS event_type,o.name AS event_operation , fe.user_id,fe.entity_id " +
						"FROM feed AS fe " +
						"LEFT JOIN event_types AS et " +
						"ON fe.event_type_id=et.event_type_id " +
						"LEFT JOIN operations AS o " +
						"ON fe.operation_id=o.operation_id " +
						"WHERE user_id = ? ",
				this::mapRowToEvent, id);
	}

	@Override
	public void addEvent(Event event) {
		if (eventOperation.isEmpty()) {
			eventOperation = fullDirectory("operations", "operation_id", eventOperation);
		}
		if (eventType.isEmpty()) {
			eventType = fullDirectory("event_types", "event_type_id", eventType);
		}
		String sqlQuery = "INSERT INTO feed (time_feed,event_type_id,operation_id,user_id,entity_id) " +
				"VALUES (?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
			stmt.setTimestamp(1, Timestamp.from(Instant.now()));
			stmt.setInt(2, eventType.get(event.getEventType().name()));
			stmt.setInt(3, eventOperation.get(event.getOperation().name()));
			stmt.setLong(4, event.getUserId());
			stmt.setLong(5, event.getEntityId());
			return stmt;
		}, keyHolder);
	}

	/**
	 * Заполнить справочник значениями
	 *
	 * @param nameDictionary - название справочника в бд
	 * @param nameIdInTable  - название поля с id в таблице
	 * @param dictionary     - название справочника в программе
	 */
	private HashMap<String, Integer> fullDirectory(String nameDictionary, String nameIdInTable, HashMap<String, Integer> dictionary) {
		String sqlQuery = "SELECT * " +
				"FROM " + nameDictionary;
		List<Directory> query = jdbcTemplate.query(sqlQuery, (ResultSet rs, int rowNum) -> {
			return Directory.builder()
					.id(rs.getInt(nameIdInTable))
					.name(rs.getString("name"))
					.build();
		});
		query.forEach(e -> dictionary.put(e.getName(), e.getId()));
		return dictionary;
	}

	private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
		return Event.builder()
				.eventId(rs.getLong("event_id"))
				.timestamp(rs.getTimestamp("time_feed").toInstant().toEpochMilli())
				.eventType(EventType.valueOf(rs.getString("event_type")))
				.operation(EventOperation.valueOf(rs.getString("event_operation")))
				.userId(rs.getLong("user_id"))
				.entityId(rs.getLong("entity_id"))
				.build();
	}
}