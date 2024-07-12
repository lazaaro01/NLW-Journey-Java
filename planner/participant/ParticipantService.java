package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private  ParticipantRepository repository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
        List<Participant> participants  = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.repository.saveAll(participants);

        System.out.println(participants.getFirst().getId());
    }
    public ParticipantCreateResponsitory registerParticipantToEvent(String email, Trip trip) {
        Participant newPartcipant = new Participant(email, trip);

        return  new ParticipantCreateResponsitory(newPartcipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId){};

    public void triggerConfirmationEmailToParticipants(String email){};

    public List<ParticipantData> getAllParticipantFromEvent(UUID tripId) {
        return (List<ParticipantData>) this.repository.findByTripId(tripId).stream().map(participant -> new ParticipantData(participant.getId(), (String) participant.getName(), participant.getEmail(), (String) participant.getisConfirmed()).toList());
    }

    public void registerParticipantsToEvent(boolean add) {
    }

    public ParticipantCreateResponsitory registerParticipantToEvent(UUID add) {
        return null;
    }

    public List<Participant> getAllPartcipantsFromEvent(UUID id) {
        return List.of();
    }
}
