package com.CVStoDB.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "clientes")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

	@Id
	private Integer cartela;
	private String nome;
	
}
