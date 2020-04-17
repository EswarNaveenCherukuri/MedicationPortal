package com.manage.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.hospital.model.Order;

// Repository for orders
public interface OrderRepository extends JpaRepository<Order, Integer> {

	// Method to find all the orders of a user
	public List<Order> findAllByUserId(int userId);

}
