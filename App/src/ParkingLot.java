import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        occupied = false;
    }
}

public class ParkingLot {

    private ParkingSpot[] spots;
    private int size;

    public ParkingLot(int capacity) {

        size = capacity;
        spots = new ParkingSpot[size];

        for(int i=0;i<size;i++)
            spots[i] = new ParkingSpot();
    }

    // simple hash function
    private int hash(String plate){
        return Math.abs(plate.hashCode()) % size;
    }

    // park vehicle
    public void parkVehicle(String plate){

        int index = hash(plate);
        int probes = 0;

        while(spots[index].occupied){

            index = (index + 1) % size;
            probes++;
        }

        spots[index].licensePlate = plate;
        spots[index].entryTime = System.currentTimeMillis();
        spots[index].occupied = true;

        System.out.println(
                "Assigned spot #" + index +
                        " (" + probes + " probes)"
        );
    }

    // exit vehicle
    public void exitVehicle(String plate){

        int index = hash(plate);

        while(spots[index].occupied){

            if(spots[index].licensePlate.equals(plate)){

                long duration =
                        System.currentTimeMillis() -
                                spots[index].entryTime;

                spots[index].occupied = false;

                double hours = duration / (1000.0*60*60);
                double fee = hours * 5;

                System.out.println(
                        "Spot #" + index +
                                " freed. Duration: " +
                                hours + " hours Fee: $" + fee
                );

                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found");
    }

    public static void main(String[] args){

        ParkingLot lot = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");
    }
}