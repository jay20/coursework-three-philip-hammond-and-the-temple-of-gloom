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
   * getCurrentLocation() long returning distance from explorer's current location to the Orb
   * getNeighbours() returns collection of Node Status Objects containing neighbor ID & distance from Orb
   * getDistanceToTarget() distance from explore's location to the Orb
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

      while (!(state.getDistanceToTarget() ==0)) {
          List<NodeStatus> localNeighbors = new ArrayList<>();
          Collection<NodeStatus> getNeighbors = state.getNeighbours(); //neighbors of current node and their statuses
          for (NodeStatus selection : getNeighbors){
              if (!lastNode.contains(selection.getId())) {
                  localNeighbors.add(selection);
              }
          }
          long id; //id of node
          NodeStatus ns;
          if (localNeighbors.size() > 0 ){
              ns = (NodeStatus) localNeighbors.stream().sorted(NodeStatus::compareTo).findAny().get();
              id = ns.getId();
              pathExplored.push(id);
              lastNode.add(id);
          }
          else{
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
      Collection<Node> highestGold = state.getVertices().stream().sorted(Comparator.comparing(s -> s.getTile().getGold())).collect(Collectors.toList());
      Integer timeRemaining = state.getTimeRemaining();
      Integer biggestGold = 0;
      Node currentNode = state.getCurrentNode();
      Node endNode = state.getExit();
      Node nextGold = null;
      ArrayList<Node> findShortestPath = new ArrayList<>();
      boolean keepGoing = true;

      while (!(currentNode == endNode)){
          if (state.getCurrentNode().getTile().getGold() > 0)state.pickUpGold();

      }



//      Node n = null;
//      Node target = state.getExit();
//
//      InternalMinHeap<Node> trek = new InternalMinHeap<>();
//      Map<Long, Integer> pathWeights = new HashMap<>();
//      Integer timeRemaining = state.getTimeRemaining();
//      Node startNode = state.getCurrentNode(); // the current node
//
//      Collection<Node> highestGold = state.getVertices().stream().sorted(Comparator.comparing(s -> s.getTile().getGold())).collect(Collectors.toList());
//
//
//      pathWeights.put(startNode.getId(), 0);
//      trek.add(startNode, 0);
//      /// invariant: as in lecture notes
//      while (!trek.isEmpty()) {
//          Node f = trek.poll();
//          if (f.equals(target)) {
//              break;
//          }
//
//          int nWeight = pathWeights.get(f.getId());
//
//          for (Edge e : f.getExits()) {
//              Node w = e.getOther(f);
//              int weightThroughN = nWeight + e.length();
//              Integer existingWeight = pathWeights.get(w.getId());
//              if (existingWeight == null || weightThroughN <= existingWeight) {
//                  pathWeights.put(w.getId(), weightThroughN);
//                  trek.add(w, weightThroughN);
//              } else if (weightThroughN < existingWeight) {
//                  pathWeights.put(w.getId(), weightThroughN);
//                  trek.changePriority(w, weightThroughN);
//              }
//              if (state.getCurrentNode().getTile().getGold() > 0)state.pickUpGold();
//          }
//      }state.moveTo(n);
/* current strategy:
while the start/ current node is not the exit node, get time remaining
calculate the current path back and get the highest pile of gold available
if you can reach this and get to the exit in time, go for it
continue this until there's not enough time left
continuously pick up gold along the way
head to the exit when there is just enough time left
 */


  }
}