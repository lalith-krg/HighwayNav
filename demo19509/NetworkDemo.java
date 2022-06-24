
package demo19509;

import base.*;

import java.util.*;

public class NetworkDemo extends Network{
    private ArrayList<Hub> hubs;
    private ArrayList<Highway> highways;
    private ArrayList<Truck> trucks;

    public NetworkDemo(){
        hubs = new ArrayList<>();
        highways = new ArrayList<>();
        trucks = new ArrayList<>();
    }

	public void add(Hub hub){
        this.hubs.add(hub);
    }

	public void add(Highway hwy){
        this.highways.add(hwy);
    }

    public void add(Truck truck){
        this.trucks.add(truck);
    }
    
    public void start(){
        for (Hub hub: hubs){
            hub.start();
        }

        for (Truck truck: trucks){
            truck.start();
        }
    }
    
	public void redisplay(Display disp){
        for (Hub hub: hubs)         hub.draw(disp);

        for (Highway hwy: highways) hwy.draw(disp);

        for (Truck truck: trucks)   truck.draw(disp);
    }

    public Hub findNearestHubForLoc(Location loc){
        Hub hub = hubs.get(0);

        for (Hub h: hubs){
            if (loc.distSqrd(hub.getLoc()) > loc.distSqrd(h.getLoc()))
                hub = h;
            // else if (loc.distSqrd(hub.getLoc()) = loc.distSqrd(h.getLoc()))
            //     hub = h;
        }

        return hub;
    }
    
}