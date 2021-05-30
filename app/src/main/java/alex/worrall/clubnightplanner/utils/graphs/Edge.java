package alex.worrall.clubnightplanner.utils.graphs;

import androidx.annotation.Nullable;

/**
 * A class to represent a non-directional weighted edge between two vertices on a graph
 */
public class Edge {
    String sourceNode;
    String targetNode;
    int weight;

    public Edge(String sourceNode, String targetNode, int weight) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.weight = weight;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public int getWeight() {
        return weight;
    }


    // Overridden this way because nodes are not directional (sourceNode and targetNode can be
    // transposed)
    @Override
    public int hashCode() {
        int result = 17;
        int sourceHash = sourceNode != null ? sourceNode.hashCode() : 0;
        int targetHash = targetNode != null ? targetNode.hashCode() : 0;
        result = 31 * result + sourceHash + targetHash;
        result = 31 * result + weight;
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Edge) {
            Edge edge = (Edge) obj;
            return (edge.targetNode.equals(this.targetNode) && edge.sourceNode.equals(this.sourceNode) || edge.sourceNode.equals(this.targetNode) && edge.targetNode.equals(this.sourceNode))
                    && edge.weight == this.weight;
        }
        return false;
    }
}
