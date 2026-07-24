package com.mbm.healthinsurance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbm.healthinsurance.entity.Customer;
import com.mbm.healthinsurance.entity.Notification;
import com.mbm.healthinsurance.enums.NotificationStatus;

public interface NotificationRepository
		extends JpaRepository<Notification, Long> {

	List<Notification> findByCustomerOrderByCreatedAtDesc(
			Customer customer);

	long countByCustomer(Customer customer);

	long countByCustomerAndStatus(
			Customer customer,
			NotificationStatus status);

	List<Notification> findTop5ByCustomerOrderByCreatedAtDesc(
			Customer customer);

	List<Notification> findAllByOrderByCreatedAtDesc();

}