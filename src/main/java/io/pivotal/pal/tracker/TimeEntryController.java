package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository=timeEntryRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        return new ResponseEntity(timeEntryRepository.create(timeEntryToCreate),HttpStatus.CREATED);
    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.GET)
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {

        TimeEntry result = timeEntryRepository.find(timeEntryId);

        if(null!=result){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TimeEntry>> list() {

        return new ResponseEntity<>(timeEntryRepository.list(),HttpStatus.OK);
    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry testVal = timeEntryRepository.update(timeEntryId,expected);

        if (null!=testVal){
            return new ResponseEntity<>(testVal,HttpStatus.OK);
        }
        else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.DELETE)
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        System.out.println(timeEntryId);
        timeEntryRepository.delete(timeEntryId);
        timeEntryRepository.find(timeEntryId);
        System.out.println(timeEntryRepository.find(timeEntryId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
