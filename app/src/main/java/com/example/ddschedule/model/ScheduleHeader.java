package com.example.ddschedule.model;

import com.chad.library.adapter.base.entity.JSectionEntity;

public class ScheduleHeader extends JSectionEntity {

    private boolean isHeader;
    private Object  object;

    public ScheduleHeader(boolean isHeader, Object object) {
        this.isHeader = isHeader;
        this.object = object;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
