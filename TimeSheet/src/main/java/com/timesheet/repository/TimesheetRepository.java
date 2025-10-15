package com.timesheet.repository;

import com.timesheet.entity.Timesheet;
import com.timesheet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    Optional<Timesheet> findByUserAndDate(User user, LocalDate date);


}
