package com.kodlabs.doktorumyanimda.model.peak;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Peak {
    private String id;
    private String name;
    private String date;
    private String otherUserID;
    private int process;
    private int fee;
    private int peakDay;
    private String picture;
    private String note;

    public Peak(String id, String name, String otherUserID, String date, int process, int fee, int peakDay, String note, String picture) {
        this.id = id;
        this.name = name;
        this.otherUserID = otherUserID;
        this.date = date;
        this.process = process;
        this.fee = fee;
        this.peakDay = peakDay;
        this.picture = picture;
        this.note = note;
    }
}
