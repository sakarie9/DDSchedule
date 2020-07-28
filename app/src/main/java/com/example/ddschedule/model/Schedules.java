package com.example.ddschedule.model;

import java.util.List;

public class Schedules {
    private List<ScheduleModel> schedules;

    public Schedules(List<ScheduleModel> schedules) {
        this.schedules = schedules;
    }

    public List<ScheduleModel> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleModel> schedules) {
        this.schedules = schedules;
    }
}
