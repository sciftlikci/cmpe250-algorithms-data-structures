public class Vertex {
    int id;
    String vehicle;
    String destination;
    int capacity;
    Integer predecessor = null;
    String bagType;

    public Vertex(int id) {
        // constructor
        this.id = id;
    }

    public Vertex(int id, String bagType) {
        // constructor
        this.id = id;
        this.bagType = bagType;
    }

    public Vertex(int id, String vehicle, String destination, int capacity) {
        // constructor
        this.id = id;
        this.vehicle = vehicle;
        this.destination = destination;
        this.capacity = capacity;
    }
}


class Edge {
    int residualCapacity;

    public Edge(int residualCapacity) {
        // constructor
        this.residualCapacity = residualCapacity;
    }
}