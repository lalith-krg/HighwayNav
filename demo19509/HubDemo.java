package demo19509;

import base.Highway;
import base.Hub;
import base.Location;
import base.Truck;
import base.Network;

import java.util.*;

class HubDemo extends Hub {

	private ArrayList<Truck> trucks;	// list of trucks at the hub

	public HubDemo(Location loc) {
		super(loc);
		trucks = new ArrayList<>();
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if (!trucks.contains(truck)){
			System.out.println("Truck added");
			trucks.add(truck);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		if (trucks.contains(truck))
			// trucks.remove(truck);
			trucks.remove(truck);
	}

	@Override
	public synchronized Highway getNextHighway(Hub from, Hub dest) {
		ArrayList<Highway> hwys = getHighways();

		Highway highway = null;

		ArrayList<Highway> visited = new ArrayList<>();
		highway = dfs(hwys, visited, from, dest);

		// **EVEN THE CODE BELOW WORKS, AND IT TAKES SHORTER ROUTES USUALLY**
		// **BUT I CAN THINK OF CASES WHEN THIS WON'T WORK**
		// **EXAMPLE, HIGHWAYS THAT LEAD TO DESTINATION HUB IN SHORT TERM**
		// **BUT NEVER REACHES THE DESTINATION HUB**


		// ***START OF OTHER CODE BELOW***

		
		// for (Highway hwy: hwys){

		// 	if (highway == null){
		// 		highway = hwy;
		// 		continue;
		// 	}

		// 	if (hwy.getEnd().equals(from))	continue;
			
		// 	else{
		// 		int distToDest = highway.getEnd().getLoc().distSqrd(dest.getLoc());

		// 		int newDistToDest = hwy.getEnd().getLoc().distSqrd(dest.getLoc());

		// 		if(distToDest > newDistToDest)
		// 			highway = hwy;
	
		// 	}
		// 	// if (hwy.getStart().equals(dest))	break;
		// }


		// ***END OF OTHER CODE OTHER CODE ABOVE***


		// 		// Hub end = hwy.getEnd();
		// 		// int mind = highway.getEnd().getLoc().distSqrd(this.getLoc());	// min ditance

		// 		// int newd = hwy.getEnd().getLoc().distSqrd(this.getLoc());		// new distance

		// 	// else if(hwy.getEnd().equals(dest)){
		// 	// 	highway = hwy;
		// 	// 	return highway;
		// 	// }

		return highway;
	}

	@Override
	protected void processQ(int deltaT) {
		// if (trucks.size() == 0)	System.out.println("Empty");

		for (Truck truck : trucks){
		// if (trucks.size() > 0){
		// 	Truck truck = trucks.get(0);

			// if destination reached
			if (this.getLoc().equals(truck.getDest())){
				truck.enter(null);
				remove(truck);
				return;
			}

			// if destination "hub" reached
			if (this.getLoc().equals(Network.getNearestHub(truck.getDest()).getLoc())){
				truck.enter(null);
				remove(truck);
				return;
			}

			// find next highway
			Highway hwy = getNextHighway(truck.getLastHub(), Network.getNearestHub(truck.getDest()));

			// try putting truck on the highway
			if (hwy != null && hwy.hasCapacity()){
				hwy.add(truck);
				truck.enter(hwy);
				System.out.println("Sent truck " + truck.getTruckName());
				remove(truck);
			}
			
			// to avoid concurrent exception, a very small delay
			try{
				Thread.sleep(50);
				return;
			}
			catch(Exception e){
				return;
			}
		}
	}

	Highway dfs(ArrayList<Highway> hwys, ArrayList<Highway> visited, Hub from, Hub dest){

		// if (visited.size() > 0 && visited.get(visited.size()-1).getEnd().equals(dest)){
		// 	return visited.get(visited.size()-1);
		// }

		// doing dfs and seeing if the current path can lead us 
		// to the destination hub
		for (Highway hwy: hwys){

			// if truck came from the current hub
			if (hwy.getEnd().equals(from)){
				continue;
			}

			// if this leads to the destination hub
			if (hwy.getEnd().equals(dest)){
				return hwy;
			}

			// if highway already covered in the path
			if (visited.contains(hwy))	return null;

			// add the current highway
			// search in the next segment
			visited.add(hwy);
			Highway h = dfs(hwy.getEnd().getHighways(), visited, from, dest);
			
			// remove from this to try another path
			visited.remove(hwy);

			// if not null, we reached the destination hub at a stage
			if (h != null){
				return hwy;
			}
		}

		return null;
	}
	
}
