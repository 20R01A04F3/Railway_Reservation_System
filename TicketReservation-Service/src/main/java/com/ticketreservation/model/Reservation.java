package com.ticketreservation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservationId;
	private String trainClass;
	private String address;
	private String bankName;
	private String creditNo;
	private int reservedTickets;
	private double totalPrice;
	private String pnrNumber;
	private int trainId;
	private int passengerId;
	private int classId;
}
