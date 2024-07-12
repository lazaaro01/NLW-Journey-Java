package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.Participant;
import com.rocketseat.planner.participant.ParticipantCreateResponsitory;
import com.rocketseat.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository repository;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);

        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            this.repository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/{id/confirm}")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setisConfirmed();

            this.repository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return  ResponseEntity.notFound().build();
    }

    // ACTIVITY

    @PostMapping("/{id/activities}")
    public ResponseEntity<ActivityResponse> resgiterActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);


            return ResponseEntity.ok(activityResponse);
        }

        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities") // TRIPS
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

    @PostMapping("/{id/invite}")
    public ResponseEntity<ParticipantCreateResponsitory> invitePartcipant(@PathVariable UUID id, @RequestBody  TripRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();


            ParticipantCreateResponsitory participantResponsitory = this.participantService.registerParticipantToEvent(payload.email());

            if (rawTrip.setisConfirmed()) this.participantService.triggerConfirmationEmailToParticipants(payload.email());

            return ResponseEntity.ok(participantResponsitory);
        }

        return  ResponseEntity.notFound().build();
    }

}
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Participant>> getAllPartcipant(@PathVariable UUID id) {
        List<Participant> participantList = this.participantService.getAllPartcipantsFromEvent(id);

        return ResponseEntity.ok(participantList);
    }

    // LINKS

@PostMapping("/{id/links}")
public ResponseEntity<LinkResponse> resgiterLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
    Optional<Trip> trip = this.repository.findById(id);

    if (trip.isPresent()) {
        Trip rawTrip = trip.get();

        LinkResponse linkResponse = this.linkService.register(payload, rawTrip);


        return ResponseEntity.ok(linkResponse);
    }

    return  ResponseEntity.notFound().build();
}

@GetMapping("/{id}/links") // TRIPS
public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
    List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

    return ResponseEntity.ok(linkDataList);
}

