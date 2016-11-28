import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class FibonacciHeap {
    FibonacciNode current;
    FibonacciNode max;
    int nodeNum;
    int maxDegree;
    FibonacciHeap() {
        current = null;
        max = null;
        nodeNum = 0;
        maxDegree = 0;
    }
    void insert(FibonacciNode node) {
        //delete the node from it's parent if exist
        if (node.parent != null) {
            FibonacciNode parent = node.parent;
            node.parent = null;
            parent.degree--;
            if (parent.degree == 0) {
                parent.child = null;
            } else if (parent.child == node) {
                FibonacciNode temp = node.left;
                parent.child = temp;
            }
        }
        //delete the node from it's sibling if exist
        if (node.left != null && node.right != null && node.left != node.right) {
            FibonacciNode left = node.left;
            FibonacciNode right = node.right;
            left.right = right;
            right.left = left;
        } else if (node.left !=null && node.right != null && node.left == node.right) {
            FibonacciNode temp = node.left;
            temp.right = null;
            temp.left = null;

        }
        node.left = null;
        node.right = null;
        //update the nodeNum
        this.nodeNum++;
        //adding the node into the root double linked and update the max node
        if (this.nodeNum == 1) {
            this.current = node;
            this.max = node;
        } else if (this.nodeNum == 2) {
            this.current.left = node;
            this.current.right = node;
            node.left = this.current;
            node.right = this.current;
            if (this.max.value < node.value | (this.max.value == node.value && this.max.key.compareTo(node.key) > 0 /*this.max.smaller(node)*/)){
                this.max = node;
            }
        } else {
            FibonacciNode left = this.current.left;
            left.right = node;
            node.left = left;
            node.right = this.current;
            this.current.left = node;
            if (this.max.value < node.value | (this.max.value == node.value && this.max.key.compareTo(node.key) > 0 /*this.max.smaller(node)*/)) {
                this.max = node;
            }
        }
    }
    //increase
    void increase(FibonacciNode node, int num) {
        node.value += num;
        if(node.parent != null) {
            FibonacciNode parent = node.parent;
            if (node.value > parent.value | (node.value == parent.value && parent.key.compareTo(node.key) < 0/*parent.smaller(node)*/)) {
                this.insert(node);//insert node into the root circle
                this.cascadingCut(parent);
            }
        } else if (this.max.value < node.value | (this.max.value == node.value && this.max.key.compareTo(node.key) > 0)) {
            this.max = node;
        }
    }
    //cascading cut
    void cascadingCut(FibonacciNode node) {
        FibonacciNode parent = node.parent;
        if (node.childCut && parent != null) {
            this.insert(node);
            this.cascadingCut(parent);
        } else if (parent != null) {
            parent.childCut = true;
        }
    }
    //delete max
    FibonacciHeap deleteMax() {
        FibonacciNode maxNode = this.max;
        //cut and insert all max's child into root circle
        if (maxNode.child != null) {
            //this.max = this.max.child;
            FibonacciNode child = maxNode.child;
            //maxNode.child = null;
            while (child.left != null) {
                this.insert(child.left);
            }
            this.insert(child);
        }
        //delete the maxNode from its sibling
        this.nodeNum--;
        if (this.nodeNum == 0) {
            this.current = null;
            this.max = null;
        } else if (this.nodeNum == 1) {
            this.current = this.max.left;
            this.current.left = null;
            this.current.right = null;
            this.max = this.current;
        } else {
            FibonacciNode left = maxNode.left;
            FibonacciNode right = maxNode.right;
            left.right = right;
            right.left = left;
            maxNode.left = null;
            maxNode.right = null;
            this.current = left;
            this.max = right;
            //Loop the root to find the max
            for (int i = 0; i < this.nodeNum; i++) {
                if (this.max.value < this.current.value | (this.max.value == this.current.value && this.max.key.compareTo(this.current.key) > 0/*this.max.smaller(this.current)*/)) {
                    this.max = this.current;
                }
                this.current = this.current.left;
            }
        }
        // Bionomial Merge the rest of the heap
        FibonacciNode[] map = new FibonacciNode[(this.maxDegree + 1) * this.nodeNum];
        return this.bioMerge(map);
    }
    FibonacciHeap bioMerge(FibonacciNode[] map) {
        if (this.nodeNum > 1) {
            int i = 0;
            while (i < this.nodeNum) {
                FibonacciNode node = this.current;
                FibonacciNode next = node.left;
                int degree = node.degree;
                if(map[degree] == null) {
                    map[degree] = node;
                    i++;
                    this.current = next;
                } else if (map[degree] == node) {
                    this.current = next;
                    continue;
                } else {
                        //set the node with bigger value to be in the root
                        FibonacciNode root;
                        FibonacciNode child;
                        if (map[degree].value > node.value | (map[degree].value == node.value && map[degree].key.compareTo(node.key) < 0/*!map[degree].smaller(node)*/)) {
                            root = map[degree];
                            child = node;
                            this.swap(root,child);
                        } else {
                            root = node;
                            child = map[degree];
                        }
                        //delete child from the root circle
                        this.nodeNum--;
                        if (this.nodeNum == 1) {
                            this.max = root;
                            root.left = null;
                            root.right = null;
                        } else {
                            FibonacciNode left = child.left;
                            FibonacciNode right = child.right;
                            left.right = right;
                            right.left = left;
                        }
                        //insert the child into root's child
                        child.parent = root;
                        child.childCut = false;
                        if (root.child == null) {
                            root.child = child;
                            child.left = null;
                            child.right = null;
                        } else if(root.degree == 1) {
                            FibonacciNode temp = root.child;
                            temp.left = child;
                            temp.right = child;
                            child.left = temp;
                            child.right = temp;
                        } else {
                            FibonacciNode left = root.child;
                            FibonacciNode right = left.right;
                            left.right = child;
                            child.left = left;
                            right.left = child;
                            child.right = right;
                        }
                        map[root.degree] = null;
                        i--;
                        root.degree++;
                        if (root.degree > this.maxDegree) {
                            this.maxDegree = root.degree;
                        }
                        if (map[root.degree] == null) {
                            map[root.degree] = root;
                            i++;
                            this.current = next;
                        }
                    }
                }
            }
            this.current = this.max;
        return this;
    }
    void merge(FibonacciHeap heap) {
        while(heap.nodeNum > 1) {
            FibonacciNode temp = heap.current;
            heap.current = temp.left;
            this.insert(temp);
            heap.nodeNum--;
        }
        this.insert(heap.current);
    }
    void swap(FibonacciNode a, FibonacciNode b) {
        FibonacciNode aleft = a.left;
        FibonacciNode aright = a.right;
        FibonacciNode bleft = b.left;
        FibonacciNode bright = b.right;
        if (bleft == a) {
            bleft = b;
        }
        a.left = bleft; bleft.right = a;
        if (bright == a) {
            bright = b;
        }
        a.right = bright; bright.left = a;
        if (aleft == b) {
            aleft = a;
        }
        b.left = aleft; aleft.right = b;
        if (aright == b) {
            aright = a;
        }
        b.right = aright; aright.left = b;
        this.current = a;
    }
}