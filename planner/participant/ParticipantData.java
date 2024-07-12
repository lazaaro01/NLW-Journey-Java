package com.rocketseat.planner.participant;

public record ParticipantData(String name, String email, Boolean isConfirmed) {
    public ParticipantData(boolean id, String aVoid, Object o, String aVoid1) {
    }

    public Object toList() {
        return null;
    }
}
