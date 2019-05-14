/* Stack.java */

/**
 *  This Stack version is a mutable doubly-linked list stack.  
 *  Its implementation uses the DList class that is
 *  circularly-linked and employs a sentinel (dummy) 
 *  node at the head of the list.
 *
 * Invariants:
 * Can insert only from the end of the list
 * Can remove only from the front of the list
 * @author Daniel Elfving
 */

public class Stack {
	 /**
   *  list references the control node for 
   *  the Dlist class, which uses a head
   *  that references the sentinel node
   *  for the DlistNode class
   */
	private int size;
	private DList list;

	/* Stack invariants:
   *  1)  Can insert only from the end of the list
   *  2)  Can remove only from the end of the list
   *  3)  Size is the number of DListNode, NOT COUNTING
   *      the sentinel (denoted by "head" in the Dlist3 class), 
   *      that can be accessed from the sentinel by
   *      a sequence of "next" references.
   */


 /**
   *  Stack() constructor for an empty Stack.
   */
public Stack(){
	size = 0;
	list = new DList();
}

 /**
   *  Stack() constructor for a Stack.
   */
public Stack(int item){
  size = 1;
  list = new DList(item);
}


/**
  * getSize() returns size of Stack
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
  * makeEmpty() clears the stack
  */

public void makeEmpty(){
  while (!isEmpty()){
    pop();
  }
}

 /**
   *  push() inserts an item at the end of a Stack.
   *  Uses the insertEnd method from Dlist3 class.
   */ 
public void push (int a){
	list.insertEnd(a);
	size++;
}

 /**
   *  pop() removes the last item 
   *  (and last non-sentinel node) from
   *  a Stack.  If the list is empty, do nothing.
   *  Uses the removeEnd method from Dlist3 class.
   */
public int pop(){
  int endItem = (int)list.findEnd();
  list.removeEnd();
  size--;
  return endItem;
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

    Stack s = new Stack();
    System.out.println("### TESTING STACK ###\nEmpty list is " + s);
    System.out.println("### TESTING first ###\nFirst element in Empty list is " + s.first());
    System.out.println("### TESTING last ###\nLast element in Empty list is " + s.last());

      if (s.list.head.next != s.list.head) {
        System.out.println("head.next is wrong.");
      }
      if (s.list.head.prev != s.list.head) {
        System.out.println("head.prev is wrong.");
      }

     s.push(9);
     System.out.println("\n\n### TESTING push ###\nInserting 9 at end.\nList with 9 is " + s);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + s.first());
     System.out.println("### TESTING last ###\nLast element 9 in list is " + s.last());

      if (s.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (s.list.head.next.prev != s.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (s.list.head.prev.item != 9) {
        System.out.println("head.prev.item is wrong.");
      }
      if (s.list.head.prev.next != s.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (s.size != 1) {
        System.out.println("size is wrong.");
      }


     s.push(8);
     System.out.println("\n\n### TESTING push ###\nInserting 8 at end.\nlist with 9 8 is " + s);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + s.first());
     System.out.println("### TESTING last ###\nLast element 8 in list is " + s.last());

      if (s.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (s.list.head.next.prev != s.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (s.list.head.prev.item != 8) {
        System.out.println("head.prev.item is wrong.");
      }
      if (s.list.head.prev.next != s.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (s.list.head.next.next != s.list.head.prev) {
        System.out.println("q.list.head.next.next != q.list.head.prev.");
      }
      if (s.list.head.prev.prev != s.list.head.next) {
        System.out.println("q.list.head.prev.prev != q.list.head.next.");
      }
      if (s.list.size != 2) {
        System.out.println("size is wrong.");
      }


     s.push(7);
     System.out.println("\n\n### TESTING push ###\nInserting 7 at end.\nlist with 9 8 7 is " + s);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + s.first());
     System.out.println("### TESTING last ###\nLast element 7 in list is " + s.last());

      if (s.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (s.list.head.next.prev != s.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (s.list.head.prev.item != 7) {
        System.out.println("head.prev.item is wrong.");
      }
      if (s.list.head.prev.next != s.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (s.list.head.next.next.next != s.list.head.prev) {
        System.out.println("q.list.head.next.next != q.list.head.prev.");
      }
      if (s.list.head.prev.prev.prev != s.list.head.next) {
        System.out.println("q.list.head.prev.prev != q.list.head.next.");
      }
      if (s.list.size != 3) {
        System.out.println("size is wrong.");
      }

     s.pop();
     System.out.println("\n\n### TESTING pop ###\nRemoving 7 at end.\nList with 9 8 is " + s);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + s.first());
     System.out.println("### TESTING last ###\nLast element 8 in list is " + s.last());

      if (s.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (s.list.head.next.prev != s.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (s.list.head.prev.item != 8) {
        System.out.println("head.prev.item is wrong.");
      }
      if (s.list.head.prev.next != s.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (s.list.size != 2) {
        System.out.println("size is wrong.");
      }

     s.pop();
     System.out.println("\n\n### TESTING pop ###\nRemoving 8 at end.\nList with 9 is " + s);
     System.out.println("### TESTING first ###\nFirst element 9 in list is " + s.first());
     System.out.println("### TESTING last ###\nLast element 9 in list is " + s.last());

     if (s.list.head.next.item != 9) {
        System.out.println("head.next.item is wrong.");
      }
      if (s.list.head.next.prev != s.list.head) {
        System.out.println("head.next.prev is wrong.");
      }
      if (s.list.head.prev.item != 9) {
        System.out.println("head.prev.item is wrong.");
      }
      if (s.list.head.prev.next != s.list.head) {
        System.out.println("head.prev.next is wrong.");
      }
      if (s.list.size != 1) {
        System.out.println("size is wrong.");
      }
		
	   s.pop();
     System.out.println("\n\n### TESTING pop ###\nRemoving 9 at front.\nEmpty List is " + s);
     System.out.println("### TESTING first ###\nFirst element in Empty list is " + s.first());
     System.out.println("### TESTING last ###\nLast element in Empty list is " + s.last());

      if (s.list.head.next != s.list.head) {
        System.out.println("head.next is wrong.");
      }
      if (s.list.head.prev != s.list.head) {
        System.out.println("head.prev is wrong.");
      }
      if (s.list.size != 0) {
        System.out.println("size is wrong.");
      }
	 }
}