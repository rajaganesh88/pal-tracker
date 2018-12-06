package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    private ResultSet rsltSet;
    private TimeEntry timeEntry;


    public JdbcTimeEntryRepository (DataSource mysqlDataSource){
         this.jdbcTemplate = new JdbcTemplate(mysqlDataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        String insertSql="INSERT INTO time_entries (project_id, user_id, date, hours) " +
                "VALUES (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement pst = connection.prepareStatement(insertSql, new String[]{"id"});
                    pst.setLong(1, timeEntry.getProjectId());
                    pst.setLong(2, timeEntry.getUserId());
                    pst.setDate(3, Date.valueOf(timeEntry.getDate()));
                    pst.setInt(4,timeEntry.getHours());
                    return pst;
                }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId)  {

        String selectQry = "Select * from time_entries where id = ?";

        try{
            timeEntry = jdbcTemplate.queryForObject(selectQry,new Object[] {timeEntryId},new TimeEntryRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            timeEntry =null;
        }
        return timeEntry;
    }

    @Override
    public List<TimeEntry> list() {

        List<TimeEntry> listEntry = new ArrayList<TimeEntry>();
        String selectQry = "Select * from time_entries";

        listEntry = jdbcTemplate.query(selectQry,new TimeEntryRowMapper());

        return listEntry;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry any) {


        long updateId = create(any).getId();
        String updateQry="Update time_entries Set project_id = ?, user_id=?, date=?, hours=? where id=?";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement pst = connection.prepareStatement(updateQry);
                    pst.setLong(1, any.getProjectId());
                    pst.setLong(2, any.getUserId());
                    pst.setDate(3, Date.valueOf(any.getDate()));
                    pst.setInt(4, any.getHours());
                    pst.setLong(5, eq);
                   return pst;
                });


        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("Delete from time_entries where id=?",timeEntryId);
    }

}
