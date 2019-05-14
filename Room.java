public class Room implements Comparable<Room> {

	protected Room parent;
	protected int depth;
	protected boolean visited;
    protected int roomNumber;

    public Room (int roomNumber){ // constructor
    	this.roomNumber = roomNumber;
    	this.visited = false;
    }
    
    Room(Room copyRoom){ // copy constructor
    	parent = copyRoom.parent;
    	depth = copyRoom.depth;
    	roomNumber = copyRoom.roomNumber;
        visited = copyRoom.visited;
    }

    public void setRoom(int roomNumber){  // mutator method
    	this.roomNumber = roomNumber;
    }

    public void visitRoom(){ // mutator method
    	this.visited = true;
    }

    public boolean hasVisitedRoom(){ 
    	return this.visited;
    }

    public int getRoomNumber(){ // accessor method
    	return this.roomNumber;
    }

	public void visit(Room origin){
		this.parent = origin;
		if (origin == null){
			this.depth = 0;
		} else {
			this.depth = origin.depth + 1;
		}
	}

	@Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Room) {
            Room rm = (Room) obj;
            return (roomNumber == rm.roomNumber && visited == rm.visited);
        }
        return false;
    }

    @Override
    public int compareTo(Room anotherRoom){
    	if (this.roomNumber < anotherRoom.roomNumber){
    		return - 1;
    	} else {
    		return 1;
    	}
    }

    @Override
    public String toString() {
    	return String.valueOf(roomNumber);
    }
}