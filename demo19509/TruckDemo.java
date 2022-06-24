package demo19509;

import base.Truck;
import base.*;
// import java.math.*;

class TruckDemo extends Truck {

	private Hub lastHub;	// the hub that the truck left
	private Hub nextHub;	// the hub truck is going towards
	private Highway hwy;	// highway it is at
	private int speed, elapsedTime;
	// speed of the truck and time elapsed on the current highway

	private int stateNum;
	// state variable indicating the following
	// 0 : Starting stage, going to first nearest hub
	// 1 : at a hub
	// 2 : on a highway
	// 3 : moving to destination from last hub
	// 4 : verifying and end of movement
	// 5 : temporary wait/stop stage

	// private int tempcount = (int)(Math.random()*100);
	// giving random values to trucks for distinct manual check

	// super source, dest, deltaT, startTime

	// super functions: setStartTime, setDeltaT, getLoc, getTruckName
	// setLoc, getDest, getLastHub, enter run, draw
	// getStartTime, getSource, update

	public TruckDemo(){
			this.lastHub = null;
			// this.nextHub = Network.getNearestHub(this.getLoc());
			this.elapsedTime = 0;
			this.speed = 100;
			this.stateNum = 0;
	}

	@Override
	public String getTruckName() {
		return "Truck19509";// + tempcount;	// extra number for manual check
	}

	@Override
	protected void update(int deltaT) {
		// to update positions
		// Location loc = getLoc();

		// this.setLoc(new Location(150, 150));

		if (stateNum == 0){
			// just started

			this.nextHub = Network.getNearestHub(this.getLoc());
			// travelling at infinte speed...  ; )
			this.setLoc(new Location(nextHub.getLoc()));
			stateNum = 1;
		}

		else if (stateNum == 1){
			// at a hub

			// if (this.getLoc().equals(this.getDest())){
			// 	System.out.println("Reached");
			// 	stateNum = 4;
			// }
			System.out.println(this.getTruckName() + " Reached next hub");

			// if truck is already at destination hub, go to state 3
			
			if (this.getLoc().equals(this.getDest())){
				// System.out.println("Reached");
				// this.hwy.remove(this);
				stateNum = 3;
				return;
			}
			
			if (nextHub.equals(Network.getNearestHub(getDest()))){
				stateNum = 3;
				return;
			}
			
			// try adding to the hub
			boolean added = nextHub.add(this);
			if (added)	stateNum = 5;

			// elapsedTime += deltaT;
		}

		else if (stateNum == 2){
			// in a highway
			Location lastHubLoc = lastHub.getLoc();
			Location nextHubLoc = nextHub.getLoc();

			// System.out.println(lastHubLoc + " " + nextHubLoc);

			elapsedTime += deltaT;

			// length of the highway
			double dist = Math.sqrt(lastHubLoc.distSqrd(nextHubLoc)*1.0);

			// fraction of distance covered
			double trav = (elapsedTime/1000)*speed / dist;

			// System.out.println(trav);

			// if fraction of distance covered is >= 1
			if (trav >= 1){
				setLoc(new Location(nextHubLoc));
				this.hwy.remove(this);
				
				if (this.getLoc().equals(this.getDest())){
					// System.out.println("Reached");
					stateNum = 3;
					return;
				}

				stateNum = 1;
			}

			else{
				int x1 = lastHubLoc.getX();
				int x2 = nextHubLoc.getX();
				int x;

				// updating x
				if (x1<=x2)
					x = (int)(x1 + Math.abs(x1-x2)*trav);
				else
					x = (int)(x2 + Math.abs(x1-x2)*(1-trav));

				int y1 = lastHubLoc.getY();
				int y2 = nextHubLoc.getY();
				int y;

				// updating y
				if (y1<=y2)
					y = (int)(y1 + Math.abs(y1-y2)*trav);
				else
					y = (int)(y2 + Math.abs(y1-y2)*(1-trav));

				setLoc(new Location(x, y));
			}

			// if (this.getLoc().equals(this.getDest())){
			// 	// System.out.println("Reached");
			// 	this.hwy.remove(this);
			// 	stateNum = 3;
			// 	return;
			// }

			// // if (nextHubLoc.equals(this.getDest())){
			// // 	// System.out.println("Reached");
			// // 	this.hwy.remove(this);
			// // 	stateNum = 3;
			// // 	return;
			// // }

			// if (this.getLoc().equals(nextHubLoc)){
			// 	// reached
			// 	this.hwy.remove(this);
			// 	stateNum = 1;
			// }
		}

		else if (stateNum == 3){
			// going to destination from last hub

			this.setLoc(getDest());
			// this.hwy.remove(this);
			// System.out.println("Reached");
			stateNum = 4;

			// again travelling at infinite speed... : )
		}

		else if (stateNum == 4){	// statenum = 4
			// reached

			System.out.println(this.getTruckName() + " Reached destination");
			stateNum = 5;
		}

		else{
			// temporary wait/stop stage
		}
	}

	public Hub getLastHub(){
		return this.lastHub;
	}

	public void enter(Highway hwy){
		this.hwy = hwy;
		System.out.println(this.getTruckName() + " Highway entered");

		// if null, it is the destination hub
		if (hwy == null){
			stateNum = 3;
			return;
		}

		this.lastHub = hwy.getStart();
		this.nextHub = hwy.getEnd();
		this.speed = hwy.getMaxSpeed();
		this.elapsedTime = 0;
		// enter highway state
		stateNum = 2;
	}

}
