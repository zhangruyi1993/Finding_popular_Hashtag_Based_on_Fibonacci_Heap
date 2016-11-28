/**
 * Created by nana on 2016/11/16.
 */
public class FibonacciNode {
    String key;
    int value;
    FibonacciNode parent;
    FibonacciNode child;
    FibonacciNode left;
    FibonacciNode right;
    int degree;
    boolean childCut;
    FibonacciNode (String k,int val) {
        value = val;
        key = k;
        parent = null;
        child = null;
        left = null;
        right = null;
        degree = 0;
        childCut = false;
    }
    boolean smaller(FibonacciNode node) {
        if(this.key.length() > node.key.length()) {
            return false;
        } else if(this.key.length() < node.key.length()) {
            return true;
        } else {
            if(this.key.compareTo(node.key) > 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}