package com.rocketseat.planner.participant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody PartcipantRequestPayload payload){
        Optional<Participant> participant = this.repository.findById(id);

        if (participant.isPresent()) {
            Participant rawPartcipant = participant.get();
            rawPartcipant.setIdConfirmed();
            rawPartcipant.setName();

            this.repository.save(rawPartcipant);

            return  ResponseEntity.ok(rawPartcipant);
        }
        return  ResponseEntity.notFound().build();
    }
}
