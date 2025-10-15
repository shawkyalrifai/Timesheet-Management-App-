package com.timesheet.controller;

import com.timesheet.dto.TimesheetRequest;
import com.timesheet.entity.Timesheet;
import com.timesheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/timesheet")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TimesheetController {

    private final TimesheetService timesheetService;
    private static final Logger logger = LoggerFactory.getLogger(TimesheetController.class);

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@Valid @RequestBody TimesheetRequest req) {
        logger.info("TimesheetController.submit called");
        logger.debug("params: {}", req);

        Timesheet t = timesheetService.submit(req);
        return ResponseEntity.ok(t);
    }

    @GetMapping("/today")
    public ResponseEntity<?> today(@RequestParam String email) {
        logger.info("TimesheetController.today called");
        logger.debug("params: {}", email);

        LocalDate today = LocalDate.now();
        Optional<Timesheet> record = timesheetService.getForDay(email, today);

        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No record for today");
            response.put("email", email);
            response.put("date", today.toString());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/day")
    public ResponseEntity<?> day(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(pattern = "yyyy-M-d") LocalDate date) {

        logger.info("TimesheetController.day called");
        logger.debug("params: {} {}", email, date);

        Optional<Timesheet> record = timesheetService.getForDay(email, date);

        if (record.isPresent()) {
            return ResponseEntity.ok(record.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No record for this day");
            response.put("email", email);
            response.put("date", date.toString());
            return ResponseEntity.ok(response);
        }
    }

}
