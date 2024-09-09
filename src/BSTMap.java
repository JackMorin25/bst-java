import java.util.ArrayList;
import java.util.List;

/**
 * A map implemented with a binary search tree.
 */
public class BSTMap<K extends Comparable<K>, V> {

    private Node root; // points to the root of the BST.
    private int size;

    /**
     * Create a new, empty BST.
     */
    public BSTMap() {
        root = null;
        size = 0;
    }

    /**
     * Put (add a key-value pair) into this BST. If the key already exists, the old
     * value will be overwritten with the new one.
     */
    public void put(K newKey, V newValue) {
        root = put(root,newKey,newValue);
    }

    /**
     * Helper function for put.
     */
    private Node put(Node curr, K newKey, V newValue) {

        //when node is empty make a new node with requested values and return it
        //size is increased to keep up with the instance variable
        if (curr == null) {
            curr = new Node();
            curr.key = newKey;
            curr.value = newValue;
            size++;
            return curr;
        }
        else if(newKey.equals(curr.key)){
            curr.value = newValue;
        }
        else if (newKey.compareTo(curr.key) < 0) {
            curr.left = put(curr.left, newKey, newValue);
        }
        else if (newKey.compareTo(curr.key) > 0) {
            curr.right = put(curr.right, newKey, newValue);
        }

        return curr;
    }

    /**
     * Get a value from this BST, based on its key. If the key doesn't already exist in the BST,
     * this method returns null.
     */
    public V get(K searchKey) {
        return get(root, searchKey);
    }

    private V get(Node curr, K searchKey){
        if(searchKey.equals(curr.key)){
            return curr.value;
        }
        else if(searchKey.compareTo(curr.key) < 0){
           return get(curr.left, searchKey);
        }
        else{
           return get(curr.right, searchKey);
        }
    }

    /**
     * Test if a key is present in this BST. Returns true if the key is found, false if not.
     */
    public boolean containsKey(K searchKey) {
        return containsKey(root, searchKey);
    }

    private boolean containsKey(Node curr, K searchKey){
        //base case when node is empty
        if(curr == null){
            return false;
        }
        else if(searchKey.equals(curr.key)){
            return true;
        }
        else if(searchKey.compareTo(curr.key) < 0){
            return containsKey(curr.left, searchKey);
        }
        else {
            return containsKey(curr.right, searchKey);
        }


    }

    /**
     * Given a key, remove the corresponding key-value pair from this BST. If the key is not found, do nothing.
     */
    public void remove(K removeKey) {
        root = remove(root, removeKey);
    }

    private Node remove(Node curr, K removeKey){
        if(curr == null){
            return null;
        }
        else if(removeKey.equals(curr.key) && curr.left == null){
            size--;
            return curr.right;
        }
        else if(removeKey.equals(curr.key) && curr.right == null){
            size--;
            return curr.left;
        }
        else if (removeKey.equals(curr.key)) {
            curr.key = getLeftmost(curr.right).key;
            curr.value = getLeftmost(curr.right).value;
            curr.right = removeLeftmost(curr.right);
            size--;
        }
        else if (removeKey.compareTo(curr.key) < 0) {
            curr.left = remove(curr.left, removeKey);
        }
        else {
            curr.right = remove(curr.right, removeKey);
        }
        return curr;

    }

    private Node getLeftmost(Node n) {
        if (n.left == null) {
            return n;
        } else {
            return getLeftmost(n.left);
        }
    }

    private Node removeLeftmost(Node n) {
        if (n.left == null) {
            return n.right;
        } else {
            n.left = removeLeftmost(n.left);
        }
        return n;
    }

    /**
     * Return the number of key-value pairs in this BST.
     */
    public int size() {
        return size;
    }

    /**
     * Return the height of this BST.
     */
    public int height() {
        return height(root);
    }

    private int height(Node curr){

        if(curr == null){
            return -1;
        }

        //variables used to compare which side is "higher" up on the tree
        //recursive so that they go through the tree
        int rightHeight = height(curr.right);
        int leftHeight = height(curr.left);

        //anytime a height is found to be larger we return the previous height + 1
        //at the end of recursive calls the function will return the larger variable
        if(leftHeight > rightHeight){
            return leftHeight + 1;
        }
        else {
            return rightHeight + 1;
        }
    }

    /**
     * Return a List of the keys in this BST, ordered by a preorder traversal.
     */
    public List<K> preorderKeys() {
        ArrayList<K> listOfKeys = new ArrayList<>();
        return preorderKeys(root, listOfKeys);
    }

    private List<K> preorderKeys(Node curr, ArrayList<K> list){
        if(curr != null){
            list.add(curr.key);
            preorderKeys(curr.left, list);
            preorderKeys(curr.right, list);
        }

        return list;
    }

    /**
     * Return a List of the keys in this BST, ordered by a inorder traversal.
     */
    public List<K> inorderKeys() {
        ArrayList<K> listOfKeys = new ArrayList<>();
        return inorderKeys(root, listOfKeys);
    }

    private List<K> inorderKeys(Node curr, ArrayList<K> list){
        if(curr != null){
            inorderKeys(curr.left, list);
            list.add(curr.key);
            inorderKeys(curr.right, list);
        }
        return list;
    }

    /**
     * Return a List of the keys in this BST, ordered by a postorder traversal.
     */
    public List<K> postorderKeys() {
        ArrayList<K> listOfKeys = new ArrayList<>();
        return postorderKeys(root, listOfKeys);
    }

    private List<K> postorderKeys(Node curr, ArrayList<K> list){
        if(curr != null){
            postorderKeys(curr.left,list);
            postorderKeys(curr.right,list);
            list.add(curr.key);
        }
        return list;
    }

    /**
     * It is very common to have private classes nested inside other classes. This is most commonly used when
     * the nested class has no meaning apart from being a helper class or utility class for the outside class.
     * In this case, this Node class has no meaning outside of this BSTMap class, so we nest it inside here
     * so as to not prevent another class from declaring a Node class as well.
     */
    private class Node {
        public K key = null;
        public V value = null;
        public Node left = null;
        public Node right = null;

        // Add a constructor here if you'd like.
        // If no constructor is defined, you get the default constructor, `new Node()`.


    }

}
