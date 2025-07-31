package com.kodlabs.doktorumyanimda.events;

public enum EventsType {
    NEW_USER_EVENTS("NewUserEvents"),
    APPROVAL_CODE_EVENTS("ApprovalCodeEvents"),
    NEW_APPOINTMENT_REQUEST_EVENTS("NewAppointmentRequestEvents"),
    APPOINTMENT_DECISION_EVENTS("AppointmentDecisionEvents"),
    MESSAGES("Messages");

    private final String value;
    EventsType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
