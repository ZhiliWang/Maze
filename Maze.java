import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class Maze {
    private static String playAgain = "No"; // default string for playing game once 
    private static boolean correctInput;  // used for checking for correct user input
    private static List<Boolean> openEdgesBool;  // used to keep track oif open edges in boolean form
    private static List<Integer> openEdgesInt; // used to keep track of open edges in integer form
    private static int SIZE = 0; // height & width
    private static int NUM_ROOMS = 0; // re-assigned in main function
    private static int NUM_DOORS = 0; // re-assigned in readMaze
    private static DisjointSets dj;
    private static ArrayList<Integer> edgeListTracker; // used to keep track of edgeNumbers as integers
    private static ArrayList<Edge> edges; // generating all possible edges depending on SIZE
    private static ArrayList<Edge> removedEdges; // used to keep track of removed edges 
    private static ArrayList<Edge> mazeEdges; // most important ArrayList
                                              // empty ArrayList to begin with.
                                              // everytime we remove an edge from "edges", we add the edge to mazeEdges

    public static void main(String[] args) throws WrongInputException {
        System.out.println("Welcome to the \"A-maze-ing Project\"! \nWould you like to randomly generate " +
                "a maze by inputting a size \nor creating the maze by reading your file?");
        do {
            Scanner sc = null;
            int ans = 0;
            System.out.println("Enter \"1\" for random generation or \"2\" for generation by file");
            System.out.print("or \"0\" to quit: ");
            do {
                try {
                    sc = new Scanner(System.in);
                    ans = sc.nextInt(); // take user input of 1 or 2
                    if (ans == 1 || ans == 2 || ans == 0){
                        if (ans == 0){
                            System.out.println("Program has exited successfully!");
                        }
                        break;
                    } else {
                        throw new WrongInputException("\"" + ans + "\" is not the correct input");
                    }

                } catch (WrongInputException wie){
                    System.err.println(wie.getMessage());
                    System.out.println("Select either option \"1\" for random generation");
                    System.out.print("or \"2\" for generation by file or \"0\" to quit: ");
                } catch (InputMismatchException ime){
                    System.out.println("You have not selected a valid option.");
                   System.out.println("Select either option \"1\" for random generation");
                    System.out.print("or \"2\" for generation by file or \"0\" to quit: ");
                } catch (NoSuchElementException nse){
                    System.out.println("You have not selected a valid option.");
                    System.out.println("Select either option \"1\" for random generation");
                    System.out.print("or \"2\" for generation by file or \"0\" to quit: ");
                }
            } while (ans != 1 || ans != 2 || ans != 0);

            if (ans == 1) {
                System.out.println("You have chosen Random Generation Mode.");
                System.out.print("What size would you like? ");
                int ansSize = 0;

                do {
                    sc = null;
                    try {
                        sc = new Scanner(System.in);
                        ansSize = sc.nextInt();
                        SIZE = ansSize;
                        NUM_ROOMS = (int) Math.pow(SIZE, 2);
                        if (SIZE < 2) {
                            throw new WrongInputException("Size must be larger than 1 and you have input: \"" + SIZE + "\"");
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("SIZE must be an integer and you have input: " + ansSize);
                        System.out.print("Select SIZE that is larger than 1: ");
                    } catch (WrongInputException wie){
                        System.err.println(wie.getMessage());
                        System.out.print("Select SIZE that is larger than 1: ");
                    } catch (InputMismatchException ime){
                        System.out.println("SIZE must be an integer and you have input: " + ansSize);
                        System.out.print("Select SIZE that is larger than 1: ");
                    } catch (NoSuchElementException nse){
                        System.out.println("SIZE must be an integer and you have input: " + ansSize);
                        System.out.print("Select SIZE that is larger than 1: ");
                    } 

                } while (SIZE < 2);
                        
                dj = new DisjointSets(NUM_ROOMS);
                edges = initializeEdges();
                System.out.println("Number of initialized edge elements: " + dj.numElements());
                mazeEdges = new ArrayList<Edge>();
       			
                makeMaze();
                printMaze();
                Edge.adjustEdgeSize();
                solveMazeBFS();
                solveMazeDFS();
                System.out.println("\nMaze rooms:\n" + Edge.getMazeRooms());
                System.out.println("\nEdge size: " + Edge.getEdgeSize());
                System.out.println("\nNumbered rooms " + Edge.getRooms());

            } else if (ans == 2) {
                System.out.println("You have chosen Generation By File Mode.");
                do {
                    System.out.print("What is the name of the file? ");
                    readMaze();
                } while (!correctInput);

                makeMazeFromFile();
                printMaze();
                Edge.adjustEdgeSize();
                solveMazeBFS();
                solveMazeDFS();
                System.out.println("\nMaze rooms:\n" + Edge.getMazeRooms());
                System.out.println("\nEdge size: " + Edge.getEdgeSize());
                System.out.println("\nNumbered rooms: " + Edge.getRooms());
            }

            if (ans == 0){ // user wants to quit program
                break;
            }
            
            System.out.print("\nWould you like to play again? ");
            sc = new Scanner(System.in);
            playAgain = sc.nextLine();
            
			if (playAgain.length() == 0 || playAgain.toLowerCase().charAt(0) != 'y'){
                System.out.println("Thanks for playing!");  
                if (sc != null){
                    sc.close();
                }
                break;
            }

            Edge.makeEmpty();
            edges.clear(); // probably unnecessary but to ensure the bug-free-ness
            mazeEdges.clear();
            removedEdges.clear();

        } while (playAgain.toLowerCase().charAt(0) == 'y');
    }

    private static void makeMaze() {
    	int removeEdgeCount = 0;
    	int addEdgeCount = 0;
    	int edgeCount = 0;
    	int edgeOffsetIndex = 0;
    	int correctRand = 0;
    	int missingEdgesCount = 0;
        Edge ranEdge = null;

        removedEdges = new ArrayList<Edge>(); // use to keep track of removed edges
        ArrayList<Integer> randomList = new ArrayList<Integer>(); // use to generate random List of nubmers
        Random random = new Random(); // create a Random object that serves as a random number generator

        while (randomList.size() < edges.size()){  // create random list of numbers without repeating same number
        	int rand = random.nextInt(edges.size()); // create a random number among [0, edges.size())
        	if (!randomList.contains(rand)){
        		randomList.add(rand);
        	}
        }

       ArrayList<Integer> edgeList = new ArrayList<Integer>(); // use to add all edges as Integers

       for (int count = 0; count < edges.size(); count++){ 
    		edgeList.add(edges.get(count).getEdgeNumber());
       }

        while (dj.numSets() > 1){ 

            System.out.println("Number of remaining disjoint edge sets: " + dj.numSets());
            System.out.println("\nDisjoint sets: " + dj);
            System.out.println("\nEdges: " + edges);
            System.out.println("\nMaze Edges: " + mazeEdges);
            System.out.println("\nRandom List: " + randomList);
            System.out.println("\nRemaining edge size: " + edges.size());
            System.out.println("Removing edge number " + edgeCount + ": " + randomList.get(edgeCount));

            ranEdge = edges.get(edgeList.indexOf(randomList.get(edgeCount)));
       		correctRand = edges.indexOf(ranEdge);

       		missingEdgesCount = 0;  // count edges already removed that are less than the current removable edge
       		if (randomList.get(edgeCount) != ranEdge.getEdgeNumber()){
       			for (int count = 0; count < mazeEdges.size(); count++){ 
            		if (mazeEdges.get(count).getEdgeNumber() < randomList.get(edgeCount)){
            			// System.out.println("mazeEdge " + mazeEdges.get(count).getEdgeNumber()
            			// 	+ " and " + "edge to remove " + randomList.get(edgeCount));
            			missingEdgesCount++;
            		}
            	}
       		}

            if (dj.find(ranEdge.getXPosition()) != dj.find(ranEdge.getYPosition())){
            	System.out.println("-------------------------------------------------");
	            System.out.println("Edge index offset: " + missingEdgesCount); 
	            System.out.println("Correct edge index number to remove: " 
	            	+ (correctRand - missingEdgesCount)); 
	            edgeList.remove(correctRand - missingEdgesCount);
	            ranEdge = edges.remove(correctRand - missingEdgesCount); // adjust edge index to remove correct edge
	            removedEdges.add(ranEdge);  // adding open edges that are being removed to the ArrayList
	            System.out.println("Current edge: " + ranEdge.getEdgeNumber());
	 			System.out.println("Edge x: " + ranEdge.getXPosition() + " Edge y: " + ranEdge.getYPosition());
	 			removeEdgeCount++;

            	System.out.println("find(ranEdge.getXPosition()): " + dj.find(ranEdge.getXPosition()) + 
            		" is not equal to find(ranEdge.getYPosition()): " + dj.find(ranEdge.getYPosition()));
                dj.union(dj.find(ranEdge.getXPosition()), dj.find(ranEdge.getYPosition()));
                System.out.println("Union find between ranEdge.getXPosition(): " + dj.find(ranEdge.getXPosition()) + 
                	" and ranEdge.getYPosition(): " + dj.find(ranEdge.getYPosition()) + " completed");
                System.out.println("Current edge: " + ranEdge.getEdgeNumber());
                System.out.println("Edge x: " + ranEdge.getXPosition() + " Edge y: " + ranEdge.getYPosition());

            } else {
            	System.out.println("find(ranEdge.getXPosition()): " + dj.find(ranEdge.getXPosition()) + 
            		" is equal to find(ranEdge.getYPosition()): " + dj.find(ranEdge.getYPosition()));
            	// System.out.println("adding edge number " + edgeCount + ": " + randomList.get(edgeCount));
                // mazeEdges.add(ranEdge);
                System.out.println("Current edge: " + ranEdge.getEdgeNumber());
               	System.out.println("Edge x: " + ranEdge.getXPosition() + " Edge y: " + ranEdge.getYPosition());
                // addEdgeCount++;
            }
            edgeCount++;
        }

        for (Edge edge : edges){ // for (int i = 0; i < edges.length; i++)
            mazeEdges.add(edge); // adding closed edges to the Maze
            addEdgeCount++;
        }

		System.out.println("Total edges removed: " + removeEdgeCount);
        System.out.println("Total edges added: " + addEdgeCount);
		System.out.println("Total edge iterations: " + edgeCount);
		System.out.println("\nList of disjoint set: " + dj); 
		System.out.println("\nList of edges added: " + mazeEdges);
		System.out.println("\nList of edges removed: " + removedEdges);
		System.out.println("\nRandom List: " + randomList + "\n");
        edgeList.clear();
        randomList.clear();
    }

    private static void makeMazeFromFile(){
    	Edge newEdge = null;
    	int removeEdgeCount = 0;
    	int addEdgeCount = 0;
    	int edgeCount = 0;
    	int correctRand = 0;
    	int missingEdgesCount = 0;
    	int currentEdge = 0;

    	removedEdges = new ArrayList<Edge>(); // keep track of removed edges
    	ArrayList<Integer> edgeList = new ArrayList<Integer>(); // get edges as Integers (works with indexOf function)  
    	edgeListTracker = new ArrayList<Integer>(); // keep track of all edges

       	for (int count = 0; count < edges.size(); count++){ 
    		edgeList.add(edges.get(count).getEdgeNumber());
    		edgeListTracker.add(edges.get(count).getEdgeNumber());
       	}

        while (dj.numSets() > 1){

        	System.out.println("Number of remaining disjoint edge sets: " + dj.numSets());
            System.out.println("\nDisjoint sets: " + dj);
            System.out.println("\nEdges: " + edges);
            System.out.println("\nMaze Edges added: " + mazeEdges);
            System.out.println("\nFixed edge list: " + edgeListTracker);
            System.out.println("\nFile edge number List: " + openEdgesInt);
            System.out.println("\nFile edge boolean List: " + openEdgesBool);
            System.out.println("\nRemaining edge size: " + edges.size());

            newEdge = edges.get(edgeList.indexOf(edgeListTracker.get(edgeCount)));
        	correctRand = edges.indexOf(newEdge);

 			missingEdgesCount = 0;  // count edges already removed that are less than the current removable edge
       		if (edgeListTracker.get(edgeCount) != newEdge.getEdgeNumber()){
       			for (int count = 0; count < mazeEdges.size(); count++){ 
            		if (mazeEdges.get(count).getEdgeNumber() < edgeListTracker.get(edgeCount)){
            			// System.out.println("mazeEdge " + mazeEdges.get(count).getEdgeNumber()
            			// 	+ " and " + "edge to remove " + edgeListTracker.get(edgeCount));
            			missingEdgesCount++;
            		}
            	}
       		}
       		System.out.println("-------------------------------------------------");

 			if (openEdgesBool.get(edgeCount)){
 				System.out.println("Is it an open edge? " + openEdgesBool.get(edgeCount));
 				System.out.println("Removing edge " + edgeCount + ": " + edgeListTracker.get(edgeCount));
       			System.out.println("Edge index offset: " + missingEdgesCount); 
            	System.out.println("Correct edge index number to remove: " 
            		+ (correctRand - missingEdgesCount)); 
            	edgeList.remove(correctRand - missingEdgesCount);  
            	newEdge = edges.remove(correctRand - missingEdgesCount); // adjust edge index to remove correct edge
            	removedEdges.add(newEdge);  // adding open edges that are being removed to the ArrayList
 				System.out.println("Current edge: " + newEdge.getEdgeNumber());
 				System.out.println("Edge x: " + newEdge.getXPosition() + " Edge y: " + newEdge.getYPosition());
 				removeEdgeCount++;

 				if (dj.find(newEdge.getXPosition()) != dj.find(newEdge.getYPosition())){
 					System.out.println("find(newEdge.getXPosition()): " + dj.find(newEdge.getXPosition()) + 
            			" is not equal to find(newEdge.getYPosition()): " + dj.find(newEdge.getYPosition()));
                	dj.union(dj.find(newEdge.getXPosition()), dj.find(newEdge.getYPosition()));
                	System.out.println("Union find between newEdge.getXPosition(): " + dj.find(newEdge.getXPosition()) + 
                		" and newEdge.getYPosition(): " + dj.find(newEdge.getYPosition()) + " completed");
                	System.out.println("Current edge: " + newEdge.getEdgeNumber());
                	System.out.println("Edge x: " + newEdge.getXPosition() + " Edge y: " + newEdge.getYPosition());	
            	}

 			} else {
 				System.out.println("find(newEdge.getXPosition()): " + dj.find(newEdge.getXPosition()) + 
            		" is equal to find(newEdge.getYPosition()): " + dj.find(newEdge.getYPosition()));
 				System.out.println("Is it an open edge? " + openEdgesBool.get(edgeCount));
 				System.out.println("Adding edge " + edgeCount + ": " + edgeListTracker.get(edgeCount));
            	mazeEdges.add(newEdge);    // adding closed edges to the Maze
 				System.out.println("Current edge: " + newEdge.getEdgeNumber());
 				System.out.println("Edge x: " + newEdge.getXPosition() + " Edge y: " + newEdge.getYPosition());
 				addEdgeCount++;
 			}
 			edgeCount++;
        }

        // for (Edge edge : edges){ // for (int i = 0; i < edges.length; i++)
        //     mazeEdges.add(edge);
        // }

        System.out.println("Total edges removed: " + removeEdgeCount);
        System.out.println("Total edges added: " + addEdgeCount);
		System.out.println("Total edge iterations: " + edgeCount);
		System.out.println("\nList of disjoint set: " + dj); 
		System.out.println("\nList of edges added: " + mazeEdges);
		System.out.println("\nList of edges removed: " + removedEdges + "\n");
        edgeList.clear();
        openEdgesBool.clear();
        openEdgesInt.clear();
    }

    private static void printMaze() {
        System.out.print("+");
        for (int i = 0; i < SIZE; i++) {
            if (i == 0) {
                System.out.print("   +");
            } else {
                System.out.print("---+");
            }
        }
        System.out.print("\n");

        for (int row = 0; row < SIZE; row++) {
            printVerticalEdges(row);
            System.out.println();
            printHorizontalEdges(row);
            System.out.println();
        }
    }

    private static void printVerticalEdges(int row) {
        System.out.print("|");
        for (int col = 0; col < SIZE; col++) {
            int roomNum = row * SIZE + col;
            if (roomNum + 1 == NUM_ROOMS) // last room
                System.out.print("   |");
            else if (onRight(roomNum)) // this is a wall/"door" of the maze
                System.out.print("   |");
            else if (mazeEdges.contains(new Edge(roomNum, roomNum + 1)))
                System.out.print("   |");
            else
                System.out.print("    ");
        }
    }

    private static void printHorizontalEdges(int row) {
        System.out.print("+");
        for (int col = 0; col < SIZE; col++) {
            int roomNum = row * SIZE + col;
            if (onBottom(roomNum) && col != (SIZE - 1)){
                System.out.print("---+");
            }
 	      	else if (roomNum != (NUM_ROOMS - 1)       		
           		&& mazeEdges.contains(new Edge(roomNum, roomNum + SIZE)))
            	System.out.print("---+");
            else 
                System.out.print("   +");
        }

    } 
     

    private static ArrayList<Edge> initializeEdges() {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int i = 0; i < dj.numElements(); i++) {
            if (!onBottom(i)){
                edges.add(new Edge(i, i + SIZE));
            }
            if (!onRight(i)){
                edges.add(new Edge(i, i + 1));
            }
        }
        return edges; // size of edge is 2 * n * (n-1)
    }

    private static boolean onRight(int room) {
        return (room + 1) % SIZE == 0;
    }

    private static boolean onBottom(int room) {
        room += 1;
        return ((NUM_ROOMS - SIZE) < room && room <= NUM_ROOMS);
    }



    private static void readMaze(){
        NUM_DOORS = 4;
        correctInput = true;
        BufferedReader reader = null;
        String filename = null;
        String container = null;
        String read = null;
        List<String> vip = new ArrayList<String>();
        openEdgesInt = new ArrayList<Integer>();
        openEdgesBool = new ArrayList<Boolean>();
        boolean isOpenEdge = false;;
        int roomCount = 0;
        int doorValue = -1;

        try {

            reader = new BufferedReader(new InputStreamReader(System.in));
            filename = reader.readLine();
            reader = new  BufferedReader (new FileReader (filename));  
            SIZE = Character.getNumericValue(reader.read());
            NUM_ROOMS = (int) Math.pow(SIZE, 2);
            reader.readLine();

            dj = new DisjointSets(NUM_ROOMS);
            edges = initializeEdges();
            System.out.println("Number of initialized edge elements: " + dj.numElements());
            mazeEdges = new ArrayList<Edge>();

            while ((read = reader.readLine()) != null && roomCount < NUM_ROOMS - 1) {
                String[] splitted = read.split("\\n");
                // System.out.println(splitted[0].toCharArray());
                splitted[0] = splitted[0].replaceAll("\\s","").substring(1,3);
                	
                for (int doorCount = 0; doorCount < splitted[0].length(); doorCount++){
                	// System.out.println(splitted[0].toCharArray()[doorCount] 
                		 // + " doorCount: " + doorCount);
                	
					if (onBottom(roomCount)){ // bottom row 
                		splitted[0] = splitted[0].substring(1,2);
                	}

                	if ((roomCount + 1) % SIZE == 0 && roomCount > 0){  // right column 
                		splitted[0] = splitted[0].substring(0,1);			
                	}
            	}

            	vip.add(splitted[0]);
            	roomCount++; 
            }
            for (String countString : vip){
            	for (Character countCharacter : countString.toCharArray()){
            		openEdgesInt.add(Integer.parseInt(countCharacter.toString()));
            		isOpenEdge = Boolean.valueOf(countCharacter.equals('0'));
            		openEdgesBool.add(isOpenEdge);
            	}
            }

            } catch (FileNotFoundException fe){ 
                System.out.println("The file " + filename + " was not found.");
                correctInput = false;
                //fe.printStackTrace();
            } catch (IOException es){
                correctInput = false;
                es.printStackTrace();
            } 
            finally {
                if (correctInput && playAgain.toLowerCase().charAt(0) != 'y'){
                    try {
                        if (reader != null){
                            reader.close();
                        } 
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                } 
            }
        }
    


    private static void solveMazeBFS(){
    	ArrayList<Room> mazeRooms = new ArrayList<>();
    	mazeRooms.addAll(Edge.getRooms());
    	// do something
        Queue q = new Queue();
        int room = 0;
        mazeRooms.get(room).visitRoom();
        q.enqueue(mazeRooms.get(room).getRoomNumber());


        //int room = 0;
        //q.enqueue(room);
        //visitedRoom[room] = true;
        int i = 0;
        int j = 0;
        if (!onRight(i))
            j = i + 1;
        if (!onBottom(i))
            j = i + SIZE;
        boolean pathFound = false;

        while (!q.isEmpty()){
            i = q.dequeue();

            // System.out.println("We just dequeued: " + i);

            if (i == NUM_ROOMS - 1){
                // System.out.println("Path found at " + i);
                pathFound = true;
                break;
            }
            // for
        }

        if (pathFound){

        }

    }

    private static void solveMazeDFS(){
    	ArrayList<Room> mazeRooms = new ArrayList<>();
    	mazeRooms.addAll(Edge.getRooms());
        Stack s = new Stack();
        // do something
        Room startingRoom = mazeRooms.get(0);
        s.push(startingRoom.getRoomNumber());
        startingRoom.visitRoom();
        // mazeRooms.get(roomNumber).visitRoom();
        // for (Room room : mazeRooms){
        // 	if (!room.hasVisitedRoom()){
        // 		solveMazeDFS();
        // 	}
        // }

        ArrayList<Integer> openEdges = new ArrayList<>();

        for (Edge edge : removedEdges){
        	openEdges.add(edge.getEdgeNumber());
        }
        Collections.sort(openEdges);
        System.out.println("open edes" + openEdges);
        int i = 0;
        boolean pathFound = false;
        // Room firstRoom = null;

        while (!s.isEmpty()){
        	System.out.println("Stack " + s);
            i = s.pop();
            if (i == NUM_ROOMS - 1){
                pathFound = true;
                break;
            }
            // for (int j = i; j < edgeListTracker.size(); j++){
            int j = i;
            while (j < removedEdges.size()){

            	// if (j == 0){
            	// 	firstRoom = removedEdges.get(j).getXRoom();	
            	// }
          		
            	Room secondRoom = removedEdges.get(j).getYRoom();
            	j++;
            	int currentEdge = Edge.getCurrentEdge(startingRoom, secondRoom);
            	System.out.println("current edge: " + currentEdge);
            	if (Edge.areAdjacentRooms(startingRoom, secondRoom)){
            		if (openEdges.contains(currentEdge)){
            			if (!secondRoom.hasVisitedRoom()){
            				s.push(secondRoom.getRoomNumber());
            				secondRoom.visitRoom();
            				if (Edge.isSouthEdge(Edge.getCurrentEdge(startingRoom, secondRoom))){
                                startingRoom = secondRoom;
                            }
            			}
            		}	
            		// checkEdgeNumber = Edge.getCurrentEdge(mazeRooms.get(i), mazeRooms.get(j));
            		// if (removedEdges.contains(new Edge(mazeRooms.get(i).getRoomNumber(), 
            		// 	mazeRooms.get(j).getRoomNumber()))){
            			// if (!firstRoom.hasVisitedRoom()){
            			// 	s.push(firstRoom.getRoomNumber());
            			// 	firstRoom.visitRoom();
            			// }
            			
            		// }
            	}
            }
        }
        System.out.println("\nStack s: " + s + " " + i);
        s.makeEmpty();
        // edgeListTracker.clear();
        // if (pathFound){

        // }
    }
} 



    // private static boolean isNorthNeighbor(int room){

    // }

    // private static boolean isSouthNeighbor(int room){

    // }

    // private static boolean isEastNeighbor(int room){

    // }

    // private static boolean isWestNeighbor(int room){

    // }




/* original main function
*
            if (args.length != 1) {
                System.out.println("Must have 1 arguments passed in: <SIZE>");
                System.exit(1);
            }

            try {
                SIZE = Integer.parseInt(args[0]);
                NUM_ROOMS = (int) pow(SIZE, 2);
                if (SIZE < 2) {
                    System.out.println("Size must be larger than 1 and you have input: " + SIZE);
                    System.exit(1);
                }
            } catch (NumberFormatException nfe) {
                System.out.println("SIZE must be an integer and you have input: " + args[0]);
                System.exit(1);
            }

            try {
                dj = new DisjointSets(NUM_ROOMS);
                edges = initializeEdges();
                mazeEdges = new ArrayList<Edge>();

                makeMaze();

                printMaze();
                //solveMazeBFS();
                //solveMazeDFS();
            } catch (Exception e) {
                System.out.println("Exception" + e.getMessage());
                System.exit(1);
            }
*
* */