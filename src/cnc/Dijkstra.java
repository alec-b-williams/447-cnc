package cnc;

import jig.Vector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Dijkstra {

    private static Dijkstra singleton_instance = null;
    public Map<Integer, Node> nodeList;

    private Dijkstra (CropGame cg) {
        generateNodeList(cg);
    }

    //make Dijkstra a singleton
    //from: https://www.geeksforgeeks.org/singleton-class-java/
    public static Dijkstra getInstance(CropGame cg) {
        if (singleton_instance == null)
            singleton_instance = new Dijkstra(cg);

        return singleton_instance;
    }

    public void generateNodeList(CropGame cg) {
        Vector srcTileTilePos = Levels.levelBaseLocation[cg.level];
        Map<Integer, Node> nodeList = new LinkedHashMap<>();

        //first, generate a list of all our nodes
        for (Tile tile : cg.tiles) {
            if (tile.isTraversable()) {
                float cost = 1;
                if (tile.hasCrop()) {
                    cost += tile.getCrop().getHealth();
                } else {
                    cost += tile.getHealth();
                }
                Node node = new Node(tile.getX(), tile.getY(), cost);
                nodeList.put(node.index, node);
            }
        }

        //then, traverse through every node, calculating the distance to its neighbor nodes
        Node srcNode = nodeList.get(Tile.getTileIndexFromTilePos(srcTileTilePos.getX(), srcTileTilePos.getY()));
        srcNode.distance = 0;

        Node nextNode;

        //TODO: make next node a list to allow for equally efficient paths
        while ((nextNode = getNextMinUnvisitedNode(nodeList)) != null) {
            //System.out.println("Visiting node at " + nextNode.xPos + ", " + nextNode.yPos);
            nextNode.visited = true;
            Vector nextNodeTilePos = Tile.getTileCoordFromPixPos(nextNode.xPos, nextNode.yPos);

            //relax adjacent 4 nodes
            ArrayList<Vector> adjacentNodePos = new ArrayList<>();
            adjacentNodePos.add(new Vector(nextNodeTilePos.getX(), nextNodeTilePos.getY() - 1));
            adjacentNodePos.add(new Vector(nextNodeTilePos.getX(), nextNodeTilePos.getY() + 1));
            adjacentNodePos.add(new Vector(nextNodeTilePos.getX() - 1, nextNodeTilePos.getY()));
            adjacentNodePos.add(new Vector(nextNodeTilePos.getX() + 1, nextNodeTilePos.getY()));

            for (Vector tilePos : adjacentNodePos) {
                Node adjNode = nodeList.get(Tile.getTileIndexFromTilePos(tilePos.getX(), tilePos.getY()));
                if (adjNode != null) {
                    //System.out.println("Relaxing node at " + tilePos.getX() + ", " + tilePos.getY());
                    relax(nextNode, adjNode);
                }
            }
        }

        this.nodeList = nodeList;
    }

    public class Node {
        public float distance;
        public float xPos;
        public float yPos;
        public float cost;
        public int index;
        public int nextTileIndex;
        public boolean visited;

        public Node (float _x, float _y, float _cost) {
            distance = Float.MAX_VALUE-1;
            xPos = _x;
            yPos = _y;
            cost = _cost;
            index = Tile.getTileIndexFromPixPos(_x, _y);
            nextTileIndex = -1;
            visited = false;
        }
    }

    private Node getNextMinUnvisitedNode(Map<Integer, Node> nodeList) {
        AtomicReference<Float> min = new AtomicReference<>(Float.MAX_VALUE - 1);
        AtomicReference<Node> nextNode = new AtomicReference<>();
        nextNode.set(null);

        nodeList.forEach((key, node) -> {
            if (!node.visited) {
                if (node.distance < min.get()) {
                    nextNode.set(node);
                    min.set(node.distance);
                }
            }
        });

        return nextNode.get();
    }

    private void relax(Node srcNode, Node adjacentNode) {
        if ((srcNode.distance + adjacentNode.cost) < adjacentNode.distance) {
            adjacentNode.distance = (srcNode.distance + adjacentNode.cost);
            adjacentNode.nextTileIndex = srcNode.index;
        }
    }
}
