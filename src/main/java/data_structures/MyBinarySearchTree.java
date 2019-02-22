package data_structures;

import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import lombok.Data;

/**
 * Created by zhuran on 2019/2/22 0022
 */
public class MyBinarySearchTree<T extends Comparable<? super T>> {
    private BinaryNode root;

    private class BinaryNode {
        private T object;
        private BinaryNode left;
        private BinaryNode right;

        BinaryNode(T o, BinaryNode left, BinaryNode right) {
            this.object = o;
            this.left = left;
            this.right = right;
        }
    }

    public boolean contains(T o) {
        return contains(o, root);
    }

    private boolean contains(T o, BinaryNode node) {
        if (node == null) {
            return false;
        }
        int compare = o.compareTo(node.object);
        if (compare > 0) {
            return contains(o, node.right);
        } else if (compare < 0) {
            return contains(o, node.left);
        } else {
            return true;
        }
    }

    public void insert(T o) {
        root = insert(o, root);
    }

    private BinaryNode insert(T o, BinaryNode node) {
        if (node == null) {
            return new BinaryNode(o, null, null);
        }
        int compare = o.compareTo(node.object);
        if (compare > 0) {
            node.right = insert(o, node.right);
        } else if (compare < 0) {
            node.left = insert(o, node.left);
        } else {

        }
        return node;
    }

    private T findMin(BinaryNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.object;
        } else {
            return findMin(node.left);
        }
    }

    private T findMax(BinaryNode node) {
        if (node == null) {
            return null;
        }
        if (node.right == null) {
            return node.object;
        } else {
            return findMax(node.right);
        }
    }

    public T findMin() {
        return findMin(root);
    }

    public T findMax() {
        return findMax(root);
    }

    public void infixOrder(BinaryNode current) {
        if (current != null) {
            infixOrder(current.left);
            System.out.print(current.object + " ");
            infixOrder(current.right);
        }
    }

    public void preOrder(BinaryNode current) {
        if (current != null) {
            System.out.print(current.object + " ");
            preOrder(current.left);
            preOrder(current.right);
        }
    }

    public void postOrder(BinaryNode current) {
        if (current != null) {
            postOrder(current.left);
            postOrder(current.right);
            System.out.print(current.object + " ");
        }
    }

    public BinaryNode remove(T o) {
        return remove(o, root);
    }

    public BinaryNode remove(T o, BinaryNode node) {
        if (node == null || o == null) {
            return null;
        }
        BinaryNode parent = node;
        boolean isLeft = true;
        while (o.compareTo(node.object) != 0) {
            if (o.compareTo(node.object) < 0) {
                isLeft = true;
                parent = node;
                node = node.left;
            } else {
                isLeft = false;
                parent = node;
                node = node.right;
            }
            if (node == null) {
                return null;
            }
        }
        if (node.left == null && node.right == null) {
            if (isLeft) {
                parent.left = null;
            } else {
                parent.right = null;
            }
            return node;
        } else if (node.left == null) {
            if (isLeft) {
                parent.left = node.right;
            } else {
                parent.right = node.right;
            }
        } else if (node.right == null) {
            if (isLeft) {
                parent.left = node.left;
            } else {
                parent.right = node.left;
            }
        } else {
            BinaryNode sucNode = getSuc(node);
            if (sucNode == null) {
                return null;
            } else if (isLeft) {
                parent.left = sucNode;
            } else {
                parent.right = sucNode;
            }
            sucNode.left = node.left;
        }
        return null;
    }

    public BinaryNode getSuc(BinaryNode node) {
        //被删除点的右子节点
        BinaryNode cur = node.right;
        //替换原点的新点
        BinaryNode suc = cur;
        //替换点的原父节点
        BinaryNode sup = null;
        while (cur != null) {
            sup = suc;
            suc = cur;
            cur = cur.left;
        }
        if (suc != node.right) {
            sup.left = suc.right;
            suc.right = node.right;
        }
        return suc;
    }

    public static void main(String[] args) {
        MyBinarySearchTree<Integer> myBinarySearchTree = new MyBinarySearchTree<>();
        myBinarySearchTree.insert(10);
        myBinarySearchTree.insert(5);
        myBinarySearchTree.insert(3);
        myBinarySearchTree.insert(6);
        myBinarySearchTree.insert(20);
        myBinarySearchTree.insert(30);
        myBinarySearchTree.insert(35);
        myBinarySearchTree.insert(33);
        myBinarySearchTree.insert(29);
        myBinarySearchTree.insert(34);
        myBinarySearchTree.insert(36);
        myBinarySearchTree.infixOrder(myBinarySearchTree.root);
        System.out.println("\n-----------------------------");
        myBinarySearchTree.remove(30);
//        myBinarySearchTree.remove(35);
//        myBinarySearchTree.remove(3);
//        myBinarySearchTree.remove(6);
//        myBinarySearchTree.remove(34);
//        System.out.println(myBinarySearchTree.contains(6));
//        System.out.println(myBinarySearchTree.findMin());
//        System.out.println(myBinarySearchTree.findMax());
        myBinarySearchTree.infixOrder(myBinarySearchTree.root);
//        System.out.println();
//        myBinarySearchTree.preOrder(myBinarySearchTree.root);
//        System.out.println();
//        myBinarySearchTree.postOrder(myBinarySearchTree.root);
    }
}
