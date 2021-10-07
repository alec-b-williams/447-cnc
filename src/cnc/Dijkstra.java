package cnc;

import jig.Vector;

import java.util.LinkedHashMap;
import java.util.Map;

public class Dijkstra {

    public Map<Integer, Node> nodeList;

    public Dijkstra (CropGame cg) {
        nodeList = generateNodeList(cg);
    }

    public Map<Integer, Node> generateNodeList(CropGame cg) {
        Vector srcTileTilePos = Levels.levelWellLocation[cg.level];
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

        this.nodeList = nodeList;
        return nodeList;
    }

    public class Node {
        public float distance;
        public float tileX;
        public float tileY;
        public float cost;
        public int index;

        public Node (float _x, float _y, float _cost) {
            distance = Float.MAX_VALUE-1;
            tileX = _x;
            tileY = _y;
            cost = _cost;
            index = Tile.getTileIndexFromPixPos(_x, _y);
        }
    }
}
