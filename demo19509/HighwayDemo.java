package demo19509;

import base.Highway;
import base.Truck;

import java.util.*;

class HighwayDemo extends Highway {

	private int currentCap;				// stores the current capacity
	private ArrayList<Truck> trucks;	// contains the list of trucks on the highway

	// super vars: start, end, maxSpeed, capacity

	// super funs: setHubs, getMaxSpeed, setMaxSpeed, setCapacity
	// getStart, getEnd, getMaxSpeed, draw, hasCapacity
	// add, remove

	public HighwayDemo() {
		trucks = new ArrayList<>();
	}

	@Override
	public synchronized boolean hasCapacity() {
		if (currentCap < getCapacity())
			return true;

		return false;
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if (!trucks.contains(truck) && hasCapacity()){
			trucks.add(truck);
			currentCap++;
			return true;
		}
		
		return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		if (trucks.contains(truck)){
			trucks.remove(truck);
			currentCap--;
		}
	}

}
