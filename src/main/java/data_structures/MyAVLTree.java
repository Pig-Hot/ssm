package data_structures;


/**
 * Created by zhuran on 2019/2/22 0022
 */
public class MyAVLTree<T extends Comparable<? super T>> {
    private AVLNode node;
    private static final int ALLOWED_IMBALANCE = 1;

    private class AVLNode {
        T object;
        int height;
        AVLNode left;
        AVLNode right;

        public AVLNode(T o) {
            this(o, null, null);
        }

        public AVLNode(T object, AVLNode left, AVLNode right) {
            this.object = object;
            this.left = left;
            this.right = right;
        }
    }

    public int getHeight(AVLNode node) {
        return node == null ? -1 : node.height;
    }

    public void insert(T o) {
        node = insert(o, node);
    }

    public AVLNode insert(T o, AVLNode node) {
        if (node == null) {
            return new AVLNode(o);
        }
        int compare = o.compareTo(node.object);
        if (compare > 0) {
            node.right = insert(o, node.right);
        } else if (compare < 0) {
            node.left = insert(o, node.left);
        } else {
            return node;
        }
        return balance(node);
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode nodeRight = node.right;
        node.right = nodeRight.left;
        nodeRight.left = node;
        node.height = Math.max(getHeight(node.left),getHeight(node.right)) + 1;
        nodeRight.height = Math.max(getHeight(nodeRight.right),getHeight(node)) + 1;
        return nodeRight;
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode nodeLeft = node.left;
        node.left = nodeLeft.right;
        nodeLeft.right = node;
        node.height = Math.max(getHeight(node.left),getHeight(node.right)) + 1;
        nodeLeft.height = Math.max(getHeight(nodeLeft.left),getHeight(node)) + 1;
        return nodeLeft;
    }

    private AVLNode rotateLeftRight(AVLNode node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    private AVLNode rotateRightLeft(AVLNode node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    public AVLNode balance(AVLNode node) {
        if (node == null) {
            return null;
        }
        if (getHeight(node.left) - getHeight(node.right) > ALLOWED_IMBALANCE) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                node = rotateRight(node);
            } else {
                node = rotateRightLeft(node);
            }
        } else if (getHeight(node.right) - getHeight(node.left) > ALLOWED_IMBALANCE) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node = rotateLeftRight(node);
            }
        }
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    public void infixOrder(AVLNode current) {
        if (current != null) {
            infixOrder(current.left);
            System.out.print(current.object + " ");
            infixOrder(current.right);
        }
    }

    public void preOrder(AVLNode current) {
        if (current != null) {
            System.out.print(current.object + " ");
            preOrder(current.left);
            preOrder(current.right);
        }
    }
    public static void main(String[] args) {
        MyAVLTree<Integer> tree = new MyAVLTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(3);
        tree.insert(8);
        tree.insert(20);
        tree.insert(6);
        tree.insert(9);
        tree.preOrder(tree.node);
        System.out.println("\n---------------------");
        tree.infixOrder(tree.node);
        System.out.println("\n**********************");
        tree.insert(7);
        tree.preOrder(tree.node);
        System.out.println("\n---------------------");
        tree.infixOrder(tree.node);
    }
}
