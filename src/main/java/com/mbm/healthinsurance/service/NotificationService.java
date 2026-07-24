package com.mbm.healthinsurance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbm.healthinsurance.dto.response.NotificationResponse;
import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.Notification;
import com.mbm.healthinsurance.enums.NotificationStatus;
import com.mbm.healthinsurance.enums.NotificationType;
import com.mbm.healthinsurance.exception.ResourceNotFoundException;
import com.mbm.healthinsurance.repository.CustomerRepository;
import com.mbm.healthinsurance.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;

    private final CustomerRepository customerRepository;



    public List<NotificationResponse> getMyNotifications(
            String username) {


        Customer customer =
                customerRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Customer not found"));


        List<Notification> notifications =
                notificationRepository
                .findByCustomerOrderByCreatedAtDesc(
                        customer);



        return notifications.stream()

                .map(this::mapToResponse)

                .toList();

    }



    private NotificationResponse mapToResponse(
            Notification notification) {


        return NotificationResponse.builder()

                .notificationId(
                        notification.getNotificationId())

                .title(
                        notification.getTitle())

                .message(
                        notification.getMessage())

                .notificationType(
                        notification.getNotificationType())

                .status(
                        notification.getStatus())

                .referenceType(
                        notification.getReferenceType())

                .referenceId(
                        notification.getReferenceId())

                .createdAt(
                        notification.getCreatedAt())

                .readAt(
                        notification.getReadAt())

                .build();
    }
    
    public void createNotification(

            Customer customer,

            String title,

            String message,

            NotificationType notificationType,

            String referenceType,

            Long referenceId) {

        Notification notification = Notification.builder()
                .customer(customer)
                .title(title)
                .message(message)
                .notificationType(notificationType)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .status(NotificationStatus.UNREAD)
                .build();

        notificationRepository.save(notification);
    }
    
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {

        return notificationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToNotificationResponse)
                .toList();
    }
    
    private NotificationResponse mapToNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType())
                .status(notification.getStatus())
                .referenceType(notification.getReferenceType())
                .referenceId(notification.getReferenceId())
                .createdAt(notification.getCreatedAt())
                .build();
    }

}