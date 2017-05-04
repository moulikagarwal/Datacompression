/**
	CacheHeap class

    CONSTRUCTION: with optional capacity (that defaults to 100)
    Note: d is an attribute giving the # of children (default is 3)
	And, when d is 2, we've got something like a binary heap...

 	******************PUBLIC OPERATIONS*********************
 	void insert( x )    --> Insert x
 	Comparable deleteMin( )--> Return and remove smallest item
 	Comparable delete( i ) --> Returns and removes item at location i
 	Comparable findMin( )  --> Return smallest item
 	boolean isEmpty( ) --> Return true if empty; else false
 	boolean isFull( )   --> Return true if full; else false
 	void makeEmpty( )   --> Remove all items
 	******************ERRORS********************************
 Throws Overflow if capacity exceeded
*/


import java.util.NoSuchElementException;

/**
 * Implements a d-ary heap.
 * Note that all "matching" is based on the compareTo method.
 * @author Ofer H. Gill (your TA)
 */
public class CacheHeap <T> {

    private int d;	// The number of children each node has. And when d=2, we've got a binary heap...
    private int heapSize;	// Number of elements in heap
    private T[] array;	// The heap array
    int cacheLine = 3;
    /**
     * Construct the binary heap.
     * @param capacity the capacity of the binary heap.
     */
    public CacheHeap( int capacity) {
        heapSize = 0;
        d = 4;
        array = (T[]) new Object[ capacity + 1 ];
    }

    /**
     * Construct the binary heap.
     * @param array to be heapified.  Note it is assumed this array does
     * not have data stored in the zeroth element
     */
    public CacheHeap( T [] array, int numKids){
        int i = 0;
        while (array[i] != null)
            i++;
        heapSize = i;

        this.array = array;
        this.d = numKids;
    }

    private int parent(int i) {
        i = i-cacheLine;
        int parent =  (i-1)/d + cacheLine;
        return parent;
    }

    private int kthChild(int i, int k) {
        i -= cacheLine;
        int kChildIndex = d*i + k + cacheLine;
        return kChildIndex;
    }

    public void insert( T x ) {
        if(isFull())
            throw new NoSuchElementException("Heap is full");

        // heapify up
        int hole = heapSize + cacheLine;
        heapSize++;
        array[hole] = x;
        bubbleUp(hole);
    }

     /* Find the smallest item in the priority queue. @return the smallest item, or null, if empty.
     */
    public T findMin( ) {
        if( isEmpty( ) )
            return null;
        return array[0+cacheLine];
    }

    
    public int size() {
        return heapSize;
    }

    /**
     * Remove the smallest item from the priority queue.
     * @return the smallest item, or null, if empty.
     */
    public T removeMin() {
        if(isEmpty())
            return null;

        T minItem = findMin();
        array[ 0 + cacheLine] = array[heapSize - 1 + cacheLine];
        heapSize--;
        bubbleDown( 0+cacheLine );
        return minItem;
    }

    /**
     * Remove the item from array[hole] from the heap.
     * And, we adjust the heap accordingly.
     * @return the smallest item, or null, if empty.
     */
    public T delete( int hole ) {
        if( isEmpty( ) )
            return null;

        T keyItem = array[hole];
        array[ hole ] = array[ heapSize -1 ];
        heapSize--;
        bubbleDown( hole );

        return keyItem;
    }

    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    public void buildMinHeap(T[] array) {
//        this.array = array;
        this.array= (T[]) new Object[array.length+cacheLine];
        int j = cacheLine;
        for(T node : array){
            this.array[j++] = node;
        }
        this.heapSize = array.length;
        for(int i = heapSize+cacheLine - 1; i >= 0+cacheLine; i--)
        	bubbleDown(i);
    }


    public boolean isEmpty( ) {
        return heapSize == 0;
    }


    public boolean isFull( ) {
        return heapSize == array.length;
    }


    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void bubbleDown( int hole ) {
		int child;
		T tmp = array[ hole ];

		for(; kthChild(hole, 1) < heapSize + cacheLine; hole = child ) {
            child = smallestChild(hole);

			if( ((Comparable<T>) array[child]).compareTo( tmp ) < 0 )
			    array[ hole ] = array[ child ];
            else
				break;
        }
	     array[ hole ] = tmp;
    }

    // returns the index corresponding to hole's smallest child...
    // we assume hole's leftmost child is a valid index entry,
    // but we explicitly check validity of indices for all other children...
    // we find the smallest child taken from all valid indices...
    private int smallestChild( int hole ) {
        int bestChildYet = kthChild(hole, 1);
        int k = 2;
        int candidateChild = kthChild(hole, k);
        while ((k <= d) && (candidateChild < heapSize + cacheLine)) {
            if (((Comparable<T>) array[candidateChild]).compareTo(array[bestChildYet]) < 0)
                bestChildYet = candidateChild;
            k++;
            candidateChild = kthChild(hole, k);
        }
        return bestChildYet;
    }


    /**
     * Internal method to percolate up in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void bubbleUp (int hole) {
        T tmp = array[hole];

        for (; hole > 0+cacheLine && ((Comparable<T>) tmp).compareTo( array[parent(hole)] ) < 0; hole = parent(hole) )
            array[ hole ] = array[ parent(hole) ];
        array[ hole ] = tmp;
    }

}
