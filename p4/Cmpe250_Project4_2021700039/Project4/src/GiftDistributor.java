import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GiftDistributor {
    public HashMap < String, Edge > edgeMap;
    public HashMap < Integer, ArrayList < Integer >> residualNetworkMap;
    public HashMap < Integer, Integer > predecessorMap;
    public int pathMaxFlow;
    public int sourceVertexId, sinkVertexId;

    public GiftDistributor(InputArgs inputArgs) {
        this.edgeMap = inputArgs.edgeMap;
        this.residualNetworkMap = inputArgs.residualNetworkMap;
        this.sourceVertexId = inputArgs.sourceVertexId;
        this.sinkVertexId = inputArgs.sinkVertexId;
    }

    public ArrayList < Integer > checkPath() {
        // node visit check
        HashMap < Integer, Boolean > isVisited = new HashMap < Integer, Boolean > ();
        this.predecessorMap = new HashMap < Integer, Integer > ();

        for (Integer vertexId: this.residualNetworkMap.keySet()) {
            isVisited.put(vertexId, false);
            this.predecessorMap.put(vertexId, null);
        }

        // BFS, start with source city
        LinkedList < Integer > vertexQ = new LinkedList < Integer > ();
        vertexQ.add(this.sourceVertexId);
        isVisited.put(this.sourceVertexId, true);
        boolean outerBreak = false;

        // visit & calculate distances
        while (!vertexQ.isEmpty()) {
            if (outerBreak) {
                break;
            }

            // iterate over neighboring vertices
            Integer currVertexId = vertexQ.poll();
            ArrayList < Integer > currVertexNeighbors = this.residualNetworkMap.get(currVertexId);

            for (Integer neighborVertexId: currVertexNeighbors) {
                Edge currEdge = this.edgeMap.get(String.valueOf(currVertexId) + "_" + String.valueOf(neighborVertexId));

                // visit node if not visited & there is possible flow to sink
                if (!isVisited.get(neighborVertexId) && currEdge.residualCapacity > 0) {
                    // make visited
                    isVisited.put(neighborVertexId, true);

                    // assign predecessor to neighbor vertex  
                    this.predecessorMap.put(neighborVertexId, currVertexId);

                    // if neighbor is sink, break
                    if (neighborVertexId == sinkVertexId) {
                        outerBreak = true;
                        break;
                    }

                    // add to BFS queue
                    vertexQ.add(neighborVertexId);
                }
            }
        }

        // get BFS path
        ArrayList < Integer > shortestResidualPath = new ArrayList < Integer > ();
        this.pathMaxFlow = Integer.MAX_VALUE;
        int iterVertexId = this.sinkVertexId;

        while (this.predecessorMap.get(iterVertexId) != null) {
            // add current vertex to the path.
            shortestResidualPath.add(iterVertexId);

            // track max flow on path
            Edge currEdge = this.edgeMap.get(String.valueOf(this.predecessorMap.get(iterVertexId)) + "_" + String.valueOf(iterVertexId));
            this.pathMaxFlow = Math.min(this.pathMaxFlow, currEdge.residualCapacity);

            // iterate
            iterVertexId = this.predecessorMap.get(iterVertexId);
        }

        return shortestResidualPath;
    }

    public int distributeGifts() {
        int netFlow = 0;

        // inital path finding
        ArrayList < Integer > shortestResidualPath = checkPath();

        while (shortestResidualPath.size() > 0) {
            for (Integer vertexId: shortestResidualPath) {
                Integer vertexPredecessorId = this.predecessorMap.get(vertexId);

                String forwardEdgeId = String.valueOf(vertexPredecessorId) + "_" + String.valueOf(vertexId);
                String backwardEdgeId = String.valueOf(vertexId) + "_" + String.valueOf(vertexPredecessorId);

                Edge forwardEdge = this.edgeMap.get(forwardEdgeId);
                Edge backwardEdge;

                if (!this.edgeMap.containsKey(backwardEdgeId)) {
                    backwardEdge = new Edge(0);
                    this.edgeMap.put(backwardEdgeId, backwardEdge);

                    ArrayList < Integer > currVertexNeighbors = this.residualNetworkMap.get(vertexId);
                    currVertexNeighbors.add(vertexPredecessorId);
                    this.residualNetworkMap.put(vertexId, currVertexNeighbors);

                } else {
                    backwardEdge = this.edgeMap.get(backwardEdgeId);
                }

                forwardEdge.residualCapacity -= this.pathMaxFlow;
                backwardEdge.residualCapacity += this.pathMaxFlow;

                this.edgeMap.put(forwardEdgeId, forwardEdge);
                this.edgeMap.put(backwardEdgeId, backwardEdge);
            }

            // increase flow passing through network
            netFlow += this.pathMaxFlow;

            // reset flow value for next bfs
            shortestResidualPath = checkPath();
        }

        return netFlow;
    }
}