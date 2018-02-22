package student;

import game.*;
import java.util.*;
import java.util.stream.Collectors;

public class Explorer {

    
    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        Stack<Long> pathExplored = new Stack<>(); //nodes already traversed
        List<Long> lastNode = new ArrayList<>(); //node most previously traversed
        pathExplored.push(state.getCurrentLocation());
        lastNode.add(state.getCurrentLocation());

        while (!(state.getDistanceToTarget() == 0)) {
            List<NodeStatus> localNeighbors = new ArrayList<>();
            Collection<NodeStatus> getNeighbors = state.getNeighbours(); //neighbors of current node and their statuses
            for (NodeStatus selection : getNeighbors) {
                if (!lastNode.contains(selection.getId())) {
                    localNeighbors.add(selection);
                }
            }
            long id; //id of node
            NodeStatus ns;
            if (localNeighbors.size() > 0) {
                ns = localNeighbors.stream().sorted(NodeStatus::compareTo).findAny().get();
                id = ns.getId();
                pathExplored.push(id);
                lastNode.add(id);
            } else {
                pathExplored.pop();
                id = pathExplored.peek();
            }
            state.moveTo(id);
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {

        Integer timeBuffer = Cavern.MAX_EDGE_WEIGHT * 6;
        Node nextGoldTile = state.getCurrentNode();
        while (state.getTimeRemaining() > timeBuffer) {
            int mostValuable = 0;
            Collection<Node> highestGold = state.getVertices().stream().sorted(Comparator.comparing(s -> s.getTile().getGold())).collect(Collectors.toList());
            for (Node n : highestGold) {
                if (n.getTile().getGold() > mostValuable) {
                    mostValuable = n.getTile().getGold();
                    nextGoldTile = n;
                }
            }
            traversePath(bestPath(state.getCurrentNode(),nextGoldTile),state);
        }
        traversePath(bestPath(state.getCurrentNode(),state.getExit()),state);
    }

    private Stack<Node> bestPath(Node startNode, Node endNode) {

        Node start = startNode;
        Node end = endNode;
        InternalMinHeap<Node> trek = new InternalMinHeap<>();
        Map<Long, Integer> pathWeights = new HashMap<>();
        Map<Node,Node> pathMap = new HashMap<>();
        Stack<Node> newPath = new Stack<>();
        trek.add(startNode,0);
        pathWeights.put(startNode.getId(),0);
        while (!trek.isEmpty()) {
            Node currentNode = trek.poll();
            if (start.equals(end)) {
                break;
            }
            int currentWeight = pathWeights.get(currentNode.getId());
            for (Edge edge : currentNode.getExits()) {
                Node anotherNode = edge.getOther(currentNode);
                int thisEdgeWeight = currentWeight + edge.length();
                Integer anotherWeight = pathWeights.get(anotherNode.getId());
                if(anotherWeight == null){
                    trek.add(anotherNode,thisEdgeWeight);pathWeights.put(anotherNode.getId(),thisEdgeWeight);pathMap.put(anotherNode,currentNode);
                }else if(thisEdgeWeight < anotherWeight){
                    pathMap.put(anotherNode,currentNode);trek.changePriority(anotherNode, thisEdgeWeight);pathWeights.put(anotherNode.getId(),thisEdgeWeight);
                }
            }
        }
        while(end != start){
            newPath.push(end);
            end = pathMap.get(end);
        }
        Collections.reverse(newPath);
        return newPath;
    }

    private void traversePath(Stack<Node> path, EscapeState state) {
        for (Node nodesOfPath : path) {
            if (state.getCurrentNode().getTile().getGold() > 0) { state.pickUpGold();}
            state.moveTo(nodesOfPath); }
    }
}
