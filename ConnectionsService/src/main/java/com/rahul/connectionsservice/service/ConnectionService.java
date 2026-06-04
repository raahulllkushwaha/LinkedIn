package com.rahul.connectionsservice.service;

import com.rahul.connectionsservice.auth.AuthContextHolder;
import com.rahul.connectionsservice.entity.Person;
import com.rahul.connectionsservice.exception.BadRequestException;
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
    public List<Person> getSecondDegreeConnectionsOfUsers(Long userId) {
        return personRepo.getSecondDegreeConnections(userId);
    }

    public List<Person> getThirdDegreeConnectionsOfUsers(Long userId) {
        return personRepo.getThirdDegreeConnections(userId);
    }

    public void sendConnectionRequest(Long userId){
        Long fromUserId = AuthContextHolder.getCurrentUserId();

        // guard: can't send request to yourself
        if (fromUserId.equals(userId)) {
            throw new BadRequestException("Cannot send connection request to yourself");
        }

        // guard: already connected
        if (personRepo.alreadyConnected(fromUserId, userId)) {
            throw new BadRequestException("Already connected with this user");
        }

        // guard: request already pending
        if (personRepo.connectionRequestExists(fromUserId, userId)) {
            throw new BadRequestException("Connection request already sent");
        }

        personRepo.addConnectionRequest(fromUserId, userId);
        log.info("Connection request successfully sent!");

    }

    // accept and reject

    public void acceptConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();

        if (personRepo.alreadyConnected(senderId, receiverId)) {
            throw new RuntimeException("You are already connected with this user");
        }

        // 1. Validate that the request actually exists before accepting
        if (!personRepo.connectionRequestExists(senderId, receiverId)) {
            throw new RuntimeException("No pending connection request found from this user");
        }

        // 2. Accept it
        personRepo.acceptConnectionRequest(senderId, receiverId);
        log.info("User {} accepted connection request from User {}", receiverId, senderId);

        if(senderId.equals(receiverId)) {
            throw new RuntimeException("Both sender and receiver are the same");
        }
    }

    public void rejectConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();

        // Prevent self-rejection
        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Both sender and receiver are the same");
        }
        // Validate that the request actually exists before rejecting
        if (!personRepo.connectionRequestExists(senderId, receiverId)) {
            throw new RuntimeException("No pending connection request found from this user");
        }

        // finally Reject it
        personRepo.rejectConnectionRequest(senderId, receiverId);
        log.info("User {} rejected connection request from User {}", receiverId, senderId);
    }
}
