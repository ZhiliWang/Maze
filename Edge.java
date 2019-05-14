import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Edge {
    
    private Room x; // edge between room x and y
    private Room y;
    private int edgeNumber;
    private static int edgeSize = 0; // count number of Edge objects created
    private static ArrayList<Room> mazeRooms = new ArrayList<>();

    public Edge(int x, int y) { // constructor
        if (x < y) {
            this.x = new Room(x);
            this.y = new Room(y);
        } else {
            this.x = new Room(y);
            this.y = new Room(x);
        }
        
        if (!mazeRooms.contains(this.x)){
            mazeRooms.add(this.x);
        }

        if (!mazeRooms.contains(this.y)){
                mazeRooms.add(this.y);
        }

        edgeNumber = edgeSize; // assign each edge a number
        edgeSize++;

        Collections.sort(mazeRooms, new Comparator<Room>(){
            public int compare(Room room1, Room room2){
                return room1.compareTo(room2);
            }
        });

    }

    Edge(Edge copyEdge){ // copy constructor
        x = copyEdge.x;
        y = copyEdge.y;
        edgeNumber = copyEdge.edgeNumber;
        edgeSize = copyEdge.edgeSize;
    }

    public int getXPosition(){ // accessor methods
        return x.getRoomNumber();
    }

    public int getYPosition(){
        return y.getRoomNumber();
    }

    public Room getXRoom(){
        return x;
    }

    public Room getYRoom(){
        return y;
    }

    public int getEdgeNumber(){
        return edgeNumber;
    }

    public static String getMazeRooms(){  // accessor method
        String rooms = "";
        for (int roomNumber = 0; roomNumber < mazeRooms.size(); roomNumber++){
            if (onLeftColumnRoom(roomNumber)){
                rooms += "\n[Room " + mazeRooms.get(roomNumber) + (mazeRooms.get(roomNumber).hasVisitedRoom() 
                    ? ": visited]" : ": unvisited]"); 
            } else {
                rooms += "[Room " + mazeRooms.get(roomNumber) + (mazeRooms.get(roomNumber).hasVisitedRoom() 
                    ? ": visited]" : ": unvisited]");   
            } 
        }
        return rooms;    
    }

    public static ArrayList<Room> getRooms(){
        return mazeRooms;
    }

    public static int getEdgeSize(){
        return edgeSize;
    }

    public static boolean areAdjacentRooms(Room x, Room y){
        int edgeNumber = getCurrentEdge(x, y);
        int roomDifference = Math.abs(y.getRoomNumber() - x.getRoomNumber());
        if (isSouthEdge(edgeNumber)){
            return roomDifference == 5;
        }
        if (isEastEdge(edgeNumber)){
            return roomDifference == 1;
        }
        return false;
    }


    private static int getMazeSize(){
        int halfTotalEdges = getEdgeSize() / 2;
        int mazeSize = ((int) Math.sqrt(4 * halfTotalEdges + 1) + 1) / 2;
        return mazeSize;
    }

    public static void adjustEdgeSize(){ 
        edgeSize /= 2;
    }

    public static void decrementEdgeSize(){
        edgeSize--;
    }

    private static int getRoomSize(){
        return (int) Math.pow(getMazeSize(), 2); 
    }

    private static boolean onRightColumnRoom(int room) {
        return (room + 1) % getMazeSize() == 0;
    }

    private static boolean onLeftColumnRoom(int room){
        return room % getMazeSize() == 0;
    }

    private static boolean onBottomRowRoom(int room) {
        room += 1;
        return ((getRoomSize() - getMazeSize()) < room && room <= getMazeSize());
    }

    private static boolean onTopRowRoom(int room){
        return room < getMazeSize();
    }

    public static boolean isSouthEdge(int edgeNumber){
        int rowEdges = 2 * getMazeSize() - 1;
        int rowEdgesQuotient = edgeNumber / rowEdges;

        if (onBottomRowEdge(edgeNumber)){
            return edgeNumber % 2 == 0;
        }

        if (!onBottomRowEdge(edgeNumber)){
            if (rowEdgesQuotient % 2 == 1){
                return edgeNumber % 2 == 1;
            }

            if (rowEdgesQuotient % 2 == 0){
                return edgeNumber % 2 == 0;
            }
        }
        return false;
    }

    public static boolean isEastEdge(int edgeNumber){
        int rowEdges = 2 * getMazeSize() - 1;
        int rowEdgesQuotient = edgeNumber / rowEdges;

        if (onBottomRowEdge(edgeNumber)){
            return edgeNumber % 2 == 1;
        }

        if (!onBottomRowEdge(edgeNumber)){
            if (rowEdgesQuotient % 2 == 1){
                return edgeNumber % 2 == 0;
            }

            if (rowEdgesQuotient % 2 == 0){
                return edgeNumber % 2 == 1;
            }
        }
        return true;
    }


    public static void makeEmpty(){
        edgeSize = 0;
        mazeRooms.clear();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Edge) {
            Edge ed = (Edge) obj;
            return (x.getRoomNumber() == ed.x.getRoomNumber() 
                && y.getRoomNumber() == ed.y.getRoomNumber());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(edgeNumber);
    }


    public static int getCurrentEdge(Room x, Room y){
        int currentEdge = 0;
        int rowCount = 0;
        int mazeSize = getMazeSize();
        int remainderY = 0;
        if (mazeSize != 0){
            rowCount = x.getRoomNumber() / mazeSize; 
        }
        if (y.getRoomNumber() - x.getRoomNumber() == mazeSize && !onBottomRowRoom(x.getRoomNumber())){
            currentEdge = 2 * x.getRoomNumber() - rowCount;
        } else if (y.getRoomNumber() - x.getRoomNumber() == 1 && !onBottomRowRoom(x.getRoomNumber())){
            currentEdge = x.getRoomNumber() + y.getRoomNumber() - rowCount;
        } else if (onBottomRowRoom(x.getRoomNumber())){
            remainderY = y.getRoomNumber() % mazeSize;
            currentEdge = x.getRoomNumber() + y.getRoomNumber() - rowCount - remainderY;
        }
        return currentEdge;
    }

    private static boolean onBottomRowEdge(int edgeNumber){
        int mazeSize = getMazeSize();
        int edgeSize = getEdgeSize();
        return ((edgeSize - mazeSize) < edgeNumber && edgeNumber <= edgeSize);
    }  

    private static boolean onTopRowEdge(int edgeNumber){
        return edgeNumber < 2 * getMazeSize() - 1;
    }


    private static boolean onRightColumnEdge(int edgeNumber) {
        int rowEdges = 2 * getMazeSize() - 1;
        return (edgeNumber + 1) % rowEdges == 0;
    }

    private void adjustRooms(int edgeNumber){ // changing the x and y of edge:
        int rowEdges = 2 * getMazeSize() - 1;
        int rowEdgesQuotient = edgeNumber / rowEdges;
        int rowEdgesRemainder = edgeNumber % rowEdges;
        int roomX = x.getRoomNumber();
        int roomY = y.getRoomNumber();

        if (onTopRowEdge(edgeNumber)){
            if (isSouthEdge(edgeNumber)){
                roomX = edgeNumber / 2;
                roomY = roomX + getMazeSize();
            } else if (isEastEdge(edgeNumber)){
                roomX = edgeNumber / 2;
                roomY = roomX + 1;
            }
        } else if (!onBottomRowEdge(edgeNumber)) {
            if (isSouthEdge(edgeNumber)) {
                roomX = (rowEdgesQuotient + edgeNumber) / 2;
                roomY = roomX + getMazeSize();
            } else if (isEastEdge(edgeNumber)) {
                roomX = (rowEdgesQuotient + edgeNumber) / 2 - 1;
                roomY = roomX + 1;
            }
        } else if (onBottomRowEdge(edgeNumber)) {
            roomX = rowEdgesQuotient * getMazeSize() + rowEdgesRemainder;
            roomY = roomX + 1;
        }
    }
}
