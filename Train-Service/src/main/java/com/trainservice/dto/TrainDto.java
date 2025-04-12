package com.trainservice.dto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trainservice.model.Train;
import com.trainservice.model.TrainClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainDto {
	private Train train;
	private List<TrainClass> trainClass;
}
