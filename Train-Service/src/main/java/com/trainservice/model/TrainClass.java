package com.trainservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TrainClass {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classId;
	private String classType;
	private double classPrice;
	private int classSeats;
	
	@ManyToOne(targetEntity = Train.class)
	@JoinColumn(name = "train_id",nullable = false)
	private Train train;
}
