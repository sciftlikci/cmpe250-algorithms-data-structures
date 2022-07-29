import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Arrays;

class InputArgs {
    // class for returning multiple arguments from readInput method
    public int totalGift = 0;
    public HashMap < String, Edge > edgeMap = new HashMap < String, Edge > ();
    public HashMap < Integer, ArrayList < Integer >> residualNetworkMap = new HashMap < Integer, ArrayList < Integer >> ();
    public int sourceVertexId, sinkVertexId;
}


public class project4main {

    public static InputArgs readInput(String inputPath) {

        // read input method    	
        // necessary
        int lineCounter = 1;
        int vertexCounter = 0;
        int vertexId = 0;
        String vehicleType = "";
        String destinationType = "";
        ArrayList < Integer > idxList = new ArrayList < > (Arrays.asList(new Integer[] {
            1,
            3,
            5,
            7,
            9
        }));

        HashMap < String, ArrayList < Vertex > > vehicleMap = new HashMap < String, ArrayList < Vertex > > ();

        // vehicle map init
        vehicleMap.put("train_green", new ArrayList < Vertex > ());
        vehicleMap.put("reindeer_green", new ArrayList < Vertex > ());
        vehicleMap.put("train_red", new ArrayList < Vertex > ());
        vehicleMap.put("reindeer_red", new ArrayList < Vertex > ());

        // collect inputs
        InputArgs inputArgs = new InputArgs();
        System.out.println("Reading input file...");

        // add super source vertex
        inputArgs.residualNetworkMap.put(vertexId, new ArrayList < Integer > ());
        inputArgs.sourceVertexId = vertexId;
        vertexId += 1;

        // add super sink vertex
        inputArgs.residualNetworkMap.put(vertexId, new ArrayList < Integer > ());
        inputArgs.sinkVertexId = vertexId;
        vertexId += 1;


        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String strCurrentLine;

            while ((strCurrentLine = br.readLine()) != null) {
                String[] splitted = strCurrentLine.split(" ");

                if (idxList.contains(lineCounter)) {
                    // Capacity input line
                    vertexCounter = Integer.parseInt(splitted[0]);
                } else if (lineCounter <= 8) {

                    // vehicle input lines
                    switch (lineCounter) {
                        case 2:
                            vehicleType = "train";
                            destinationType = "green";
                            break;
                        case 4:
                            vehicleType = "train";
                            destinationType = "red";
                            break;
                        case 6:
                            vehicleType = "reindeer";
                            destinationType = "green";
                            break;
                        case 8:
                            vehicleType = "reindeer";
                            destinationType = "red";
                            break;
                    }


                    for (int i = 0; i < vertexCounter; i++) {
                        int capacity = Integer.parseInt(splitted[i]);

                        Vertex vehicle = new Vertex(vertexId, vehicleType, destinationType, capacity);

                        // add sink vertex as neighbor
                        ArrayList < Integer > vehicleSinkList = new ArrayList < Integer > ();
                        vehicleSinkList.add(inputArgs.sinkVertexId);
                        inputArgs.residualNetworkMap.put(vertexId, vehicleSinkList);

                        // add vehicle - sink edge
                        inputArgs.edgeMap.put(String.valueOf(vertexId) + "_" + String.valueOf(inputArgs.sinkVertexId), new Edge(capacity));

                        // add vehicle to map
                        ArrayList < Vertex > currVehicleList = vehicleMap.get(vehicleType + "_" + destinationType);
                        currVehicleList.add(vehicle);
                        vehicleMap.put(vehicleType + "_" + destinationType, currVehicleList);

                        vertexId += 1;
                    }
                } else if (lineCounter == 10) {

                    // ADDING EDGES ROUTINE //
                    ArrayList < Integer > sourceBagList = inputArgs.residualNetworkMap.get(inputArgs.sourceVertexId);

                    for (int i = 0; i < vertexCounter * 2; i += 2) {
                        // iterate over each bag
                        String bagType = splitted[i];
                        int capacity = Integer.parseInt(splitted[i + 1]);

                        Vertex bag = new Vertex(vertexId, bagType);
                        inputArgs.totalGift += capacity;

                        // necessary
                        inputArgs.residualNetworkMap.put(vertexId, new ArrayList < Integer > ());

                        // add source - bag edge
                        inputArgs.edgeMap.put(String.valueOf(inputArgs.sourceVertexId) + "_" + String.valueOf(vertexId), new Edge(capacity));

                        // add bag to source's neighbors
                        sourceBagList.add(vertexId);
                        inputArgs.residualNetworkMap.put(inputArgs.sourceVertexId, sourceBagList);

                        // bag - vehicle matching
                        Stack < String > searchList = new Stack < String > ();

                        // get search list for vehicles
                        if (bagType.contains("bd")) {
                            searchList.push("train_green");
                        } else if (bagType.contains("be")) {
                            searchList.push("reindeer_green");
                        } else if (bagType.contains("cd")) {
                            searchList.push("train_red");
                        } else if (bagType.contains("ce")) {
                            searchList.push("reindeer_red");
                        } else if (bagType.contains("b")) {
                            searchList.push("train_green");
                            searchList.push("reindeer_green");
                        } else if (bagType.contains("c")) {
                            searchList.push("train_red");
                            searchList.push("reindeer_red");
                        } else if (bagType.contains("d")) {
                            searchList.push("train_red");
                            searchList.push("train_green");
                        } else if (bagType.contains("e")) {
                            searchList.push("reindeer_red");
                            searchList.push("reindeer_green");
                        } else {
                            searchList.push("train_green");
                            searchList.push("train_red");
                            searchList.push("reindeer_green");
                            searchList.push("reindeer_red");
                        }

                        boolean bagTypeCheck = bagType.contains("a");

                        while (!searchList.isEmpty()) {
                            String vehicleKey = searchList.pop();
                            ArrayList < Vertex > vehicleList = vehicleMap.get(vehicleKey);

                            for (Vertex vehicle: vehicleList) {
                                // add bag - vehicle edges
                                int bagVehicleCapacity = bagTypeCheck ? 1 : capacity;
                                inputArgs.edgeMap.put(String.valueOf(vertexId) + "_" + String.valueOf(vehicle.id), new Edge(bagVehicleCapacity));

                                // add vehicle to residual graph network
                                ArrayList < Integer > bagNeighbors = inputArgs.residualNetworkMap.get(bag.id);
                                bagNeighbors.add(vehicle.id);
                                inputArgs.residualNetworkMap.put(bag.id, bagNeighbors);
                            }
                        }

                        vertexId += 1;
                    }
                }

                lineCounter++;
            }
        } catch (IOException e) {
            System.out.println("Catch - An error occurred.");
            e.printStackTrace();
        }

        // returning multiple arguments
        return inputArgs;
    }


    public static void writeOutput(String outputPath, int networkFlow, int totalGift) {
        try {
            // create output file
            File myOutputFile = new File(outputPath);

            if (myOutputFile.createNewFile()) {
                System.out.println("Output file created: " + outputPath);
            }

            FileWriter myWriter = new FileWriter(outputPath);
            String newLine = System.getProperty("line.separator");
            System.out.println("Writing results to the output file...");
            myWriter.write(String.valueOf(totalGift - networkFlow) + newLine);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("Catch - An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Done.");
    }


    public static void main(String[] args) {
    	// main routine
    	InputArgs inputArgs = readInput(args[0]);
        GiftDistributor distributor = new GiftDistributor(inputArgs);
        int netFlow = distributor.distributeGifts();
        writeOutput(args[1], netFlow, inputArgs.totalGift);    	
    }
}