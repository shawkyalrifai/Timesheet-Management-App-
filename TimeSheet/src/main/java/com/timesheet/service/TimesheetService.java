package com.timesheet.service;

import com.timesheet.dto.TimesheetRequest;
import com.timesheet.entity.Timesheet;
import com.timesheet.entity.User;
import com.timesheet.repository.TimesheetRepository;
import com.timesheet.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final UserService userService;
    private static final Logger logger =
            LoggerFactory.getLogger(TimesheetService.class);

    public Timesheet submit(TimesheetRequest req) {
        logger.info("submit() called for {}", req.getEmail());

        if (req.getEmail() == null || req.getDate() == null) {
            throw new IllegalArgumentException("Email and date required");
        }
        if (req.getLoginTime() != null && req.getLogoutTime() != null) {
            throw new IllegalArgumentException("Cannot submit login and logout together");
        }

        User user = userService.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDate date = DateTimeUtils.parseDate(req.getDate());
        Optional<Timesheet> existingOpt = timesheetRepository.findByUserAndDate(user, date);
        Timesheet timesheet = existingOpt.orElseGet(Timesheet::new);

        timesheet.setUser(user);
        timesheet.setDate(date);

        if (req.getLoginTime() != null && !req.getLoginTime().isEmpty()) {
            if (existingOpt.isPresent() && timesheet.getLoginTime() != null) {
                throw new IllegalArgumentException("You already logged in today.");
            }
            timesheet.setLoginTime(DateTimeUtils.parseToDateTime(date, req.getLoginTime()));
        }

        if (req.getLogoutTime() != null && !req.getLogoutTime().isEmpty()) {
            if (!existingOpt.isPresent() || timesheet.getLoginTime() == null) {
                throw new IllegalArgumentException("You cannot logout before logging in.");
            }

            if (timesheet.getLogoutTime() != null) {
                throw new IllegalArgumentException("You have already logged out today.");
            }

            timesheet.setLogoutTime(DateTimeUtils.parseToDateTime(date, req.getLogoutTime()));
        }

        Timesheet saved = timesheetRepository.save(timesheet);
        logger.info("submit() saved for {} date {}", user.getEmail(), date);
        return saved;
    }



    public Optional<Timesheet> getForDay(String email, LocalDate date) {
        logger.info("getForDay() called");
        logger.debug("getForDay() params: {} {}", email, date);
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) return Optional.empty();
        return timesheetRepository.findByUserAndDate(user, date);
    }
}
