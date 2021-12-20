package com.einvoive.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Document
public class Logs {

    @Id
    private String id;
    private String name;
    private String date;
    private String description;

    public Logs(String name, String description) {
        this.name = name;
        this.description = description;
        setLocalDateTime();
    }

    private void setLocalDateTime() {
        ZoneId destinationTimeZone = ZoneId.of("Asia/Riyadh");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now(destinationTimeZone);
        this.date = dtf.format(now);
    }

    public String getId() {    return id;   }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
