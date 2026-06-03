package com.rahul.notificationservice.service;

import com.rahul.notificationservice.entity.Notification;
import com.rahul.notificationservice.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepo notificationRepo;

    public void addNotification(Notification notification){
        log.info("Adding notification to db, message: {}", notification.getMessage());
        notification = notificationRepo.save(notification);

        //SendMailer to send email
        //firebase communication manager: for android;


    }

}
