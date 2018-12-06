package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeEntryRowMapper implements RowMapper<TimeEntry> {


    @Override
    public TimeEntry mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        TimeEntry timeEntry = new TimeEntry();

            timeEntry.setId(resultSet.getLong(1));
            timeEntry.setProjectId(resultSet.getLong(2));
            timeEntry.setUserId(resultSet.getLong(3));
            timeEntry.setDate(resultSet.getDate(4).toLocalDate());
            timeEntry.setHours(resultSet.getInt(5));

            return timeEntry;

    }
}
