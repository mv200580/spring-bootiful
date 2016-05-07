package com.example;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by smv on 07.05.2016.
 */
@Entity
@Data
public class Reservation{

	@Id
	@GeneratedValue
	private Long id;

	private String reservationName;

	public Reservation() {}

	public Reservation(String rn) {
		this.reservationName = rn;
	}


}
