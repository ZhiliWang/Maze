/* Queue.java */

/**
 *  This Queue version is a mutable doubly-linked list queue.  
 *  Its implementation uses the DList3 class that is
 *  circularly-linked and employs a sentinel (dummy) 
 *  node at the head of the list.
 *
 * Invariants:
 * Can insert only from the end of the list
 * Can remove only from the front of the list
 * @author Daniel Elfving
 */

public class Queue {
	 /**
   *  list references the control node for 
   *  the Dlist3 class, which uses a head 
   *  that references the sentinel node
   *  for the DlistNode3 class  
   */
	private int size;
	private DList list;

	/* Queue invariants:
   *  1)  Can insert only from the end of the list
   *  2)  Can remove only from the front of the list
   *  3)  Size is the number of DListNode3s, NOT COUNTING 
   *      the sentinel (denoted by "head" in the Dlist3 class), 
   *      that can be accessed from the sentinel by
   *      a sequence of "next" references.
   */


 /**
   *  Queue() constructor for an empty Queue.
   */
public Queue(){
	size = 0;
	list = new DList();
}

/**
   *  Queue() constructor for a Queue.
   */
public Queue(int item){
  size = 1;
  list = new DList(item);
}

 /**
   *  enqueue() inserts an item at the end of a Queue.
   *  Uses the insertEnd method from Dlist3 class.
   */ 
public void enqueue (int a){
	list.insertEnd(a);
	size++;
}


/**
  * getSize() returns size of Queue
  */
public int getSize(){
  return size;
}


/**
  * isEmpty() returns boolean value of true or false
  */

public boolean isEmpty(){
  return getSize() == 0;
}

/**
  * makeEmpty() clears the queue
  */

public void makeEmpty(){
  while (!isEmpty()){
    dequeue();
  }
}

 /**
   *  dequeue() removes the first item 
   *  (and first non-sentinel node) from
   *  a Queue.  If the list is empty, do nothing.
   *  Uses the removeFront method from Dlist3 class.
   */
public int dequeue(){
  int frontItem = (int)list.findFront();
  list.removeFront();
  size--;
  return frontItem;
}

/**
   *  find() returns the first item in the list 
   *  @return the first item in the list.
   **/
 public Object first(){
    return list.findFront();
  }

/**
   *  last() returns the last item in the list 
   *  @return the last item in the list.
   **/
  public Object last(){
  	return list.findEnd();
  }

    /**
   *  toString() returns a String representation of this Queue.
   *
   *  @return a String representation of this Queue.
   */

public String toString() {
    String result = "[  ";
    DListNode current = list.head.next;
    while (current != list.head) {
      result = result + current.item + "  ";
      current = current.next;
    }
    return result + "]";
  }

  /**
   * Test cases for the four above methods
   * used in Queue class: enqueue, dequeue,
   * first(), and last().  Also checks to 
   * make sure the Dlist3 class is working
   * and functioning properly with the 
   * DlistNode3 class.
   */

  public static void main(String[] args) {

    Queue q = new Queue();
    System.out.println("### TESTING Queue ###\nEmpty list is " + q);
    System.out.println("### TESTING first ###\nFirst element in Empty list is " + q.first());
    System.out.println("### TESTING last ###\nLast element in Empty list is " + q.last());

      if (q.list.head.next != q.list.head) {
        System.out.println("head.next is wrong.");
      }
      if (q.list.head.prev != q.list.head) {
        System.out.println("head.prev is wrong.");
      }

     q.enqueue(9);
     System.out.println("\n\n### TESTING enqueue ###\nInserting 9 at end.\nList with 9 is " + q);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + q.first());
     System.out.println("### TESTING last ###\nLast element 9 in list is " + q.last());

      if (q.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (q.list.head.next.prev != q.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (q.list.head.prev.item != 9) {
        System.out.println("head.prev.item is wrong.");
      }
      if (q.list.head.prev.next != q.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (q.size != 1) {
        System.out.println("size is wrong.");
      }


     q.enqueue(8);
     System.out.println("\n\n### TESTING enqueue ###\nInserting 8 at end.\nlist with 9 8 is " + q);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + q.first());
     System.out.println("### TESTING last ###\nLast element 8 in list is " + q.last());

      if (q.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (q.list.head.next.prev != q.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (q.list.head.prev.item != 8) {
        System.out.println("head.prev.item is wrong.");
      }
      if (q.list.head.prev.next != q.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (q.list.head.next.next != q.list.head.prev) {
        System.out.println("q.list.head.next.next != q.list.head.prev.");
      }
      if (q.list.head.prev.prev != q.list.head.next) {
        System.out.println("q.list.head.prev.prev != q.list.head.next.");
      }
      if (q.list.size != 2) {
        System.out.println("size is wrong.");
      }


     q.enqueue(7);
     System.out.println("\n\n### TESTING enqueue ###\nInserting 7 at end.\nlist with 9 8 7 is " + q);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + q.first());
     System.out.println("### TESTING last ###\nLast element 7 in list is " + q.last());

      if (q.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (q.list.head.next.prev != q.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (q.list.head.prev.item != 7) {
        System.out.println("head.prev.item is wrong.");
      }
      if (q.list.head.prev.next != q.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (q.list.head.next.next.next != q.list.head.prev) {
        System.out.println("q.list.head.next.next != q.list.head.prev.");
      }
      if (q.list.head.prev.prev.prev != q.list.head.next) {
        System.out.println("q.list.head.prev.prev != q.list.head.next.");
      }
      if (q.list.size != 3) {
        System.out.println("size is wrong.");
      }

     q.dequeue();
     System.out.println("\n\n### TESTING dequeue ###\nRemoving 9 at front.\nList with 8 7 is " + q);
     System.out.println("### TESTING first ###\nFirst element 8 in list is " + q.first());
     System.out.println("### TESTING last ###\nLast element 7 in list is " + q.last());

      if (q.list.head.next.item != 8) {
        System.out.println("head.next.item is wrong.");
      }
      if (q.list.head.next.prev != q.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (q.list.head.prev.item != 7) {
        System.out.println("head.prev.item is wrong.");
      }
      if (q.list.head.prev.next != q.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (q.list.size != 2) {
        System.out.println("size is wrong.");
      }

     q.dequeue();
     System.out.println("\n\n### TESTING dequeue ###\nRemoving 8 at front.\nList with 7 is " + q);
     System.out.println("### TESTING first ###\nFirst element 7 in list is " + q.first());
     System.out.println("### TESTING last ###\nLast element 7 in list is " + q.last());

     if (q.list.head.next.item != 7) {
        System.out.println("head.next.item is wrong.");
      }
      if (q.list.head.next.prev != q.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (q.list.head.prev.item != 7) {
        System.out.println("head.prev.item is wrong.");
      }
      if (q.list.head.prev.next != q.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (q.list.size != 1) {
        System.out.println("size is wrong.");
      }
		
	   q.dequeue();
     System.out.println("\n\n### TESTING dequeue ###\nRemoving 8 at front.\nEmpty List is " + q);
     System.out.println("### TESTING first ###\nFirst element in Empty list is " + q.first());
     System.out.println("### TESTING last ###\nLast element in Empty list is " + q.last());

      if (q.list.head.next != q.list.head) {
        System.out.println("head.next is wrong.");
      }
      if (q.list.head.prev != q.list.head) {
        System.out.println("head.prev is wrong.");
      }
      if (q.list.size != 0) {
        System.out.println("size is wrong.");
      }
	}
}