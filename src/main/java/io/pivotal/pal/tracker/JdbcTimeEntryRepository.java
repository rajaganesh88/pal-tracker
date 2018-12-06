package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource mysqlDataSource){
        this.jdbcTemplate = new JdbcTemplate(mysqlDataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        String insertSql = "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                "VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql,new String[]{"id"});
                ps.setLong(1,timeEntry.getProjectId());
                ps.setLong(2,timeEntry.getUserId());
                ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                ps.setInt(4,timeEntry.getHours());
                System.out.println(ps.toString());
                return ps;
        },keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());

        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String findSql = "Select * from time_entries where id = ?";

        TimeEntry timeEntry;

        try {
            timeEntry = jdbcTemplate.queryForObject(findSql, new TimeEntryRowMapper(), timeEntryId);
        }catch (EmptyResultDataAccessException e){
            timeEntry =null;
        }
        return timeEntry;
    }

    @Override
    public List<TimeEntry> list() {

        List<TimeEntry> listEntry = jdbcTemplate.query("Select * From time_entries", new TimeEntryRowMapper());

        return listEntry;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {
        String updateSql = "Update time_entries set project_id=?, user_id=?, date=?, hours=? where id =?";

        jdbcTemplate.update(updateSql,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                eq);

        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("Delete from time_entries where id =?",timeEntryId);
    }
}
