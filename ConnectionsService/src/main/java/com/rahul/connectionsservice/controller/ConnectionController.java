package com.rahul.connectionsservice.controller;

import com.rahul.connectionsservice.entity.Person;
import com.rahul.connectionsservice.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Period;
import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping("/{userId}/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnection(@PathVariable Long userId){
        log.info("User id is: {}", userId);
        List<Person> personList = connectionService.getFirstDegreeConnectionsOfUsers(userId);
        return ResponseEntity.ok(personList);
    }
    @GetMapping("/{userId}/second-degree")
    public ResponseEntity<List<Person>> getSecondDegreeConnection(@PathVariable Long userId) {
        List<Person> personList = connectionService.getSecondDegreeConnectionsOfUsers(userId);
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/{userId}/third-degree")
    public ResponseEntity<List<Person>> getThirdDegreeConnection(@PathVariable Long userId) {
        List<Person> personList = connectionService.getThirdDegreeConnectionsOfUsers(userId);
        return ResponseEntity.ok(personList);
    }

    @PostMapping("/request/{userId")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable Long userId){
        connectionService.sendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accept/{userId")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject/{userId")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }
}
