package com.kodlabs.doktorumyanimda.dto.patient.notes;

import lombok.Data;

@Data
public class PatientNotesContentDTO {
    private String title;
    private String content;
    private String contentID;
    private String updateDateTime;
    private String createDateTime;
}
