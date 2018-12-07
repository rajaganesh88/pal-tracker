package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository=timeEntryRepository;
        System.out.println("---MeterRegistry"+meterRegistry.isClosed());

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
        System.out.println("---meterRegistry.counter"+meterRegistry.counter("Test"));
        System.out.println("---actionCounter"+meterRegistry.summary("Summary"));

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(timeEntry,HttpStatus.CREATED);
    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.GET)
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {

        TimeEntry result = timeEntryRepository.find(timeEntryId);

        if(null!=result){
            actionCounter.increment();
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(timeEntryRepository.list(),HttpStatus.OK);
    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry testVal = timeEntryRepository.update(timeEntryId,expected);

        if (null!=testVal){
            actionCounter.increment();
            return new ResponseEntity<>(testVal,HttpStatus.OK);
        }
        else return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "{timeEntryId}",method = RequestMethod.DELETE)
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {

        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
