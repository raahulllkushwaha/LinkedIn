package com.rahul.connectionsservice.service;

import com.rahul.connectionsservice.entity.Person;
import com.rahul.connectionsservice.repo.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {
    private final PersonRepo personRepo;

    public List<Person> getFirstDegreeConnectionsOfUsers(Long userId){
        log.info("Getting first degree connections of user with ID: {}", userId);
        return personRepo.getFirstDegreeConnections(userId);
    }
}
