package main;
import java.util.*;

public class DirectedGraph {
    private final Map<Integer, Set<Integer>> adjList; // adjacency list

    public DirectedGraph() {
        adjList = new HashMap<>();
    }

    public void addEdge(int a, int b) {
        if (adjList.get(a) != null) {
            adjList.get(a).add(b);
        }
    }

    public Set<Integer> getDescendants(int id) {
        Set<Integer> visited = new HashSet<>();
        dfs(id, visited);
        return visited;
    }

    public Set<Integer> getDescendants(Set<Integer> ids) {
        Set<Integer> visited = new HashSet<>();
        for (int id : ids) {
            visited.addAll(getDescendants(id));
        }
        return visited;
    }

    private void dfs(int curr, Set<Integer> visited) {
        if (!visited.contains(curr)) {
            visited.add(curr);
            for (int neighbor : adjList.getOrDefault(curr, Set.of())) {
                dfs(neighbor, visited);
            }
        }
    }
}
