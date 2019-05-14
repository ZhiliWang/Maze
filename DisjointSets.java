import java.util.*;

public class DisjointSets {

    private int[] array;

    public DisjointSets(int numElements) {
        if (numElements < 1)
            throw new IllegalArgumentException("A Disjoint-Set object must be created" +
                                               " with at least one non-negative parameter.");
        array = new int[numElements];

        for (int i = 0; i < array.length; i++)
            array[i] = -1;
    }

    public void union(int set1, int set2) {
        checkIsSetName(set1);
        checkIsSetName(set2);

        if (set1 != set2) { // no reason combine if set1 == set2
            System.out.println("Union operation initiated between ranEdge.x size: " 
                + array[set1] + "\nand ranEdge.y size: " + array[set2]);
            if (array[set2] < array[set1]) { // set2 has larger tree
                array[set2] += array[set1];
                array[set1] = set2;
            } else { // set1 has larger tree
                array[set1] += array[set2];
                array[set2] = set1;
            }
        }
    }

    // public int find(int x) {   // iterative method
    //     isValidElement(x);

    //     int root = x;
    //     while (array[root] >= 0) {
    //         root = array[root];
    //     }

    //     if (root != x) {
    //         int temp = array[x];

    //         while (temp != root) {
    //             array[x] = root;
    //             x = temp;
    //             temp = array[temp];
    //         }
    //     }
    //     return root;
    // }


    public int find(int x){ // recursive method
        if (array[x] < 0){
            return x;
        } else {
            array[x] = find(array[x]); 
            return array[x];
        }
    }

    public int numSets() {
        int count = 0;
        for (int index : array) {
            if (index < 0)
                count++;
        }
        return count;
    }


    public int numElements() {
        return array.length;
    }


    public boolean isSetName(int x){
        isValidElement(x);
        return array[x] < 0;
    }


    public int numElements(int setNum){
        checkIsSetName(setNum);
        return Math.abs(array[setNum]);
    }


    public void printSet(int setNum) {
        System.out.println(Arrays.toString(getElements(setNum)));
    }


    public int[] getElements(int setNum) {
        checkIsSetName(setNum);

        // get a list of all elements in the set
        int[] elements = new int[numElements()];
        int counter = 0;
        for (int i = 0; i < numElements(); i++) {
            if (find(i) == setNum)
                elements[counter++] = i;
        }

        return Arrays.copyOf(elements, counter);
    }


    public int[] getSetNames() {
        int[] setNames = new int[numElements()];
        int counter = 0;
        for (int i = 0; i < numElements(); i++) {
            if (isSetName(i)) {
                setNames[counter] = i;
                counter++;
            }
        }
        return Arrays.copyOf(setNames, counter);
    }


    public String toString() {
        int[] setNames = getSetNames();
        StringBuilder sb = new StringBuilder();
        sb.append("{" + Arrays.toString(getElements(setNames[0]))); // add 1st set to solve fence post problem
        for (int i = 1; i < setNames.length; i++) {
            sb.append(", " + Arrays.toString(getElements(setNames[i])));
        }
        sb.append("}");
        return sb.toString();
    }

    private void checkIsSetName(int setNum) {
        isValidElement(setNum);

        if (array[setNum] >= 0)
            throw new IllegalArgumentException("Element '" + setNum + "' must be a set name.");
    }

    private void isValidElement(int element) {
        if (element < 0 || element >= array.length)
            throw new IllegalArgumentException("Element '" + element + "' is not a valid element.");
    }

    void Connect (int x, int y) {
        int root1 = find(x);
        int root2 = find(y);
        union(root1, root2);
    }
}