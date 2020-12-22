package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Inventory;
import edu.ncsu.csc.iTrust2.repositories.InventoryRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class InventoryService extends Service {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    protected JpaRepository getRepository () {
        return inventoryRepository;
    }

    /**
     * Retrieves the singleton Inventory instance from the database, creating it
     * if it does not exist.
     *
     * @return
     */
    public synchronized Inventory getInventory () {
        final List<Inventory> inventoryList = (List<Inventory>) findAll();
        if ( inventoryList != null && inventoryList.size() == 1 ) {
            return inventoryList.get( 0 );
        }
        else {
            // initialize the inventory with 0 of everything
            final Inventory i = new Inventory( 0, 0, 0, 0 );
            save( i );
            return i;
        }
    }

}
