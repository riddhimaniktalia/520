package com.umass.hangout.model;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @PostMapping("/schedule")
    public String scheduleEvent(@RequestParam String summary,
                                @RequestParam String location,
                                @RequestParam String description,
                                @RequestParam String startDateTime,
                                @RequestParam String endDateTime) {
        try {
            Calendar service = GoogleCalendarConfig.getCalendarService();

            Event event = new Event()
                    .setSummary(summary)
                    .setLocation(location)
                    .setDescription(description);

            DateTime startDate = new DateTime(startDateTime);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDate)
                    .setTimeZone(TimeZone.getDefault().getID());
            event.setStart(start);

            DateTime endDate = new DateTime(endDateTime);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDate)
                    .setTimeZone(TimeZone.getDefault().getID());
            event.setEnd(end);

            String calendarId = "primary";
            event = service.events().insert(calendarId, event).execute();

            return "Event created: " + event.getHtmlLink();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating event: " + e.getMessage();
        }
    }
}
