package student;

import game.*;

import java.util.*;

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
    //TODO: Escape from the cavern before time runs out

//      * `Node getCurrentNode()`:
//> return the Node corresponding to the explorers location.
//
//* `Node getExit()`:
//
//> return the `Node` corresponding to the exit to the cavern (the destination).
//
//              * `Collection<Node> getVertices()`:
//
//> return a collection of all traversable nodes in the graph.
//
//* `int getTimeRemaining()`:
//
//> return the number of steps the explorer has left before the ceiling collapses.
//
//              * `void moveTo(Node n)`:
//
//> move the explorer to node `n`.
//> This will fail if the given node is not adjacent to the explorers current location.
//              > Calling this function will decrement the time remaining.
//
//* `void pickUpGold()`:
//
//> collect all gold on the current tile.
//              > This will fail if there is no gold on the current tile or it has already been collected.

      //started with making notes on strategies

      Node startNode = state.getCurrentNode(); // the current node
      long id = 0;
      Node n = null;
      Node target = state.getExit();
      state.pickUpGold();

      Map<Long, Integer> edgeWeights = new HashMap<>();
      edgeWeights.put(startNode.getId(),0);


      InternalMinHeap<Node> frontier = new InternalMinHeap<>();

      /** Contains an entry for each node in the Settled and Frontier sets. */
      Map<Long, Integer> pathWeights = new HashMap<>();

      pathWeights.put(startNode.getId(), 0);
      frontier.add(startNode, 0);
      /// invariant: as in lecture notes
      while (!frontier.isEmpty()) {
          Node f = frontier.poll();
          if (f.equals(target)) {
              break;
          }

          int nWeight = pathWeights.get(f.getId());

          for (Edge e : f.getExits()) {
              Node w = e.getOther(f);
              int weightThroughN = nWeight + e.length();
              Integer existingWeight = pathWeights.get(w.getId());
              if (existingWeight == null) {
                  pathWeights.put(w.getId(), weightThroughN);
                  frontier.add(w, weightThroughN);
              } else if (weightThroughN < existingWeight) {
                  pathWeights.put(w.getId(), weightThroughN);
                  frontier.changePriority(w, weightThroughN);
              }
          }
      }state.moveTo(n);




  }
}