package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
