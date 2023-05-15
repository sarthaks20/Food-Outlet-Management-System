import java.util.*;

class Node {
    int key, height;
    Node left, right;
}

class Queue {
    private Object arr[];
    private int front;
    private int rear;
    private int N;

    Queue() {
        arr = new Object[1];
        front = 0;
        rear = 0;
        N = 1;
    }

    public void enqueue(Object o) {
        int size = this.size();
        if (size == N - 1) { // if array is full, double the size
            Object newArr[] = new Object[2 * N];
            for (int i = 0; i < size; i++) {
                newArr[i] = arr[(front + i) % N];
            }
            N *= 2;
            arr = newArr;
            front = 0;
            rear = size;
        }
        arr[rear] = o; // add element to last
        rear = (rear + 1) % N;
    }

    public Object dequeue() {
        Object first = arr[front];
        front = (front + 1) % N;
        return first;
    }

    public Object first() {
        return arr[front];
    }

    public Object last() {
        return arr[(N + rear - 1) % N];
    }

    public int size() {
        return (N + rear - front) % N;
    }
}

class CustomerAVLTree {
    Node root;

    CustomerAVLTree() {
        root = null;
    }

    private void updateHeight(Node node) {
        node.height = 1 + (height(node.left) >= height(node.right) ? height(node.left) : height(node.right));
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanceFactor(Node node) {
        return (node == null) ? 0 : height(node.right) - height(node.left);
    }

    // Left Rotation
    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    // Right Rotation
    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private Node balanceTree(Node node) {
        updateHeight(node);
        int balance = getBalanceFactor(node);
        if (balance > 1) {
            if (getBalanceFactor(node.right) > 0) { // Left Rotation
                node = rotateLeft(node);
            } else { // Right Left Rotation
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        } else if (balance < -1) {
            if (getBalanceFactor(node.left) < 0) { // Right Rotation
                node = rotateRight(node);
            } else { // Left Right Rotaion
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        }
        return node;
    }

    Node getCustomerNode(int key) {
        return getCustomerNodeRec(root, key);
    }

    private Node getCustomerNodeRec(Node root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        if (root.key < key)
            return getCustomerNodeRec(root.right, key);
        else
            return getCustomerNodeRec(root.left, key);
    }

    void insertCustomerNode(Node newCustomer) {
        root = insertCustomerNodeRec(root, newCustomer);
    }

    private Node insertCustomerNodeRec(Node root, Node newCustomer) {
        if (root == null)
            return newCustomer;
        if (root.key < newCustomer.key)
            root.right = insertCustomerNodeRec(root.right, newCustomer);
        else
            root.left = insertCustomerNodeRec(root.left, newCustomer);
        return balanceTree(root);
    }

    void deleteCustomerNode(Node Customer) {
        root = deleteCustomerNodeRec(root, Customer);
    }

    private Node deleteCustomerNodeRec(Node root, Node Customer) {
        if (root == null)
            return root;
        if (root.key < Customer.key)
            root.right = deleteCustomerNodeRec(root.right, Customer);
        else if (root.key > Customer.key)
            root.left = deleteCustomerNodeRec(root.left, Customer);
        else {
            if (root.left == null || root.right == null) { // Leaf Node OR Only One Child
                root = (root.left == null) ? root.right : root.left;
            } else {
                Node leftmost = root.right;
                while (leftmost.left != null) {
                    leftmost = leftmost.left;
                }
                root.key = leftmost.key;
                root.right = deleteCustomerNodeRec(root.right, leftmost);
            }
        }
        if (root != null)
            root = balanceTree(root);
        return root;
    }
}

class PriorityQueue {
    Vector<Customer> pq = new Vector<Customer>();

    Customer first() {
        if (pq.size() == 0)
            return null;
        else
            return pq.get(0);
    }

    int size() {
        return pq.size();
    }

    void insertCustomer(Customer customer) {
        if (pq.size() == 0) {
            pq.add(customer);
        } else {
            pq.add(customer);
            int curr = pq.size() - 1;
            int parent = (curr % 2 == 0 ? curr / 2 - 1 : curr / 2);
            while (parent >= 0) {
                if (pq.get(parent).orderPlaceTime > pq.get(curr).orderPlaceTime) {
                    Collections.swap(pq, parent, curr);
                } else if (pq.get(parent).orderPlaceTime == pq.get(curr).orderPlaceTime
                        && pq.get(parent).counterID < pq.get(curr).counterID) {
                    Collections.swap(pq, parent, curr);
                } else {
                    break;
                }
                curr = parent;
                parent = (curr % 2 == 0 ? curr / 2 - 1 : curr / 2);
            }
        }
    }

    void deleteCustomer() {
        if (pq.size() != 0) {
            Collections.swap(pq, 0, pq.size() - 1);
            pq.remove(pq.size() - 1);
            if (pq.size() > 1) {
                int curr = 0;
                int next;
                if (2 * curr + 1 < pq.size() - 1) {// both child exist
                    if (pq.get(2 * curr + 1).orderPlaceTime > pq.get(2 * curr + 2).orderPlaceTime) {
                        next = 2 * curr + 2;
                    } else if (pq.get(2 * curr + 1).orderPlaceTime < pq.get(2 * curr + 2).orderPlaceTime) {
                        next = 2 * curr + 1;
                    } else {
                        if (pq.get(2 * curr + 1).counterID > pq.get(2 * curr + 2).counterID) {
                            next = 2 * curr + 1;
                        } else {
                            next = 2 * curr + 2;
                        }
                    }
                } else {
                    next = 2 * curr + 1;
                }
                while (next < pq.size()) {
                    if (pq.get(curr).orderPlaceTime > pq.get(next).orderPlaceTime) {
                        Collections.swap(pq, curr, next);
                    } else if (pq.get(curr).orderPlaceTime == pq.get(next).orderPlaceTime
                            && pq.get(curr).counterID < pq.get(next).counterID) {
                        Collections.swap(pq, curr, next);
                    } else {
                        break;
                    }
                    curr = next;
                    if (2 * curr + 1 < pq.size() - 1) {// both child exist
                        if (pq.get(2 * curr + 1).orderPlaceTime > pq.get(2 * curr + 2).orderPlaceTime) {
                            next = 2 * curr + 2;
                        } else if (pq.get(2 * curr + 1).orderPlaceTime < pq.get(2 * curr + 2).orderPlaceTime) {
                            next = 2 * curr + 1;
                        } else {
                            if (pq.get(2 * curr + 1).counterID > pq.get(2 * curr + 2).counterID) {
                                next = 2 * curr + 1;
                            } else {
                                next = 2 * curr + 2;
                            }
                        }
                    } else {
                        next = 2 * curr + 1;
                    }
                }
            }
        }
    }
}

class Counter {
    int lastOrderPlaceTime;
    Queue customerQueue;

    Counter() {
        lastOrderPlaceTime = 0;
        customerQueue = new Queue();
    }
}

class Customer extends Node {
    int customerID;
    int orderQuantity;
    int arrivalTime;
    int orderPlaceTime;
    int departureTime;
    int counterID;

    Customer(int id, int t, int numb, int k) {
        customerID = id;
        arrivalTime = t;
        orderQuantity = numb;
        counterID = k;
        orderPlaceTime = -1;
        departureTime = -1;
        key = customerID;
        height = 1;
    }
}

class Patties {
    int cookingStartTime;
    int pattiesCount;
}

public class MMBurgers implements MMBurgersInterface {
    CustomerAVLTree customerAVLTree = new CustomerAVLTree();
    Counter counters[] = new Counter[0];
    Queue griddle = new Queue();
    int griddleSize = 0;
    int griddleState = 0;
    int globalTime = 0;
    int lastPattiesRemoveTime = 0;
    PriorityQueue kitchen = new PriorityQueue();

    public boolean isEmpty() {
        // your implementation
        if (griddleState == 0 && kitchen.size() == 0) {
            for (int i = 1; i <= counters.length - 1; i++) {
                if (counters[i].customerQueue.size() != 0)
                    return false;
            }
            return true;
        } else {
            return false;
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public void setK(int k) throws IllegalNumberException {
        // your implementation
        if (counters.length != 0) {
            throw new IllegalNumberException("K has already been set.");
        } else {
            counters = new Counter[k + 1];
            for (int i = 1; i <= counters.length - 1; i++) {
                counters[i] = new Counter();
            }
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public void setM(int m) throws IllegalNumberException {
        // your implementation
        if (griddleSize != 0) {
            throw new IllegalNumberException("M has already been set.");
        } else {
            griddleSize = m;
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public void advanceTime(int t) throws IllegalNumberException {
        // your implementation
        if (t >= globalTime) {
            for (int i = 1; i <= counters.length - 1; i++) { // order placing
                while (counters[i].customerQueue.size() > 0 && (t >= counters[i].lastOrderPlaceTime + i
                        && t >= ((Customer) counters[i].customerQueue.first()).arrivalTime + i)) {
                    if (counters[i].lastOrderPlaceTime >= ((Customer) counters[i].customerQueue.first()).arrivalTime) {
                        Customer c = (Customer) counters[i].customerQueue.dequeue();
                        c.orderPlaceTime = counters[i].lastOrderPlaceTime + i;
                        counters[i].lastOrderPlaceTime += i;
                        kitchen.insertCustomer(c);
                    } else {
                        Customer c = (Customer) counters[i].customerQueue.dequeue();
                        c.orderPlaceTime = c.arrivalTime + i;
                        counters[i].lastOrderPlaceTime = c.orderPlaceTime;
                        kitchen.insertCustomer(c);
                    }
                }
            }
            while (globalTime <= t) { // griddle
                if (griddleSize == griddleState) {
                    if (((Patties) griddle.first()).cookingStartTime + 10 <= t) {
                        lastPattiesRemoveTime = globalTime = ((Patties) griddle.first()).cookingStartTime + 10;
                        griddleState -= ((Patties) griddle.first()).pattiesCount;
                        griddle.dequeue();
                    } else {
                        break;
                    }
                } else if (griddleState == 0 && kitchen.size() != 0) {
                    int comingPatties = Math.min(griddleSize - griddleState, kitchen.first().orderQuantity);
                    griddleState += comingPatties;
                    Patties p = new Patties();
                    p.pattiesCount = comingPatties;
                    griddle.enqueue(p);
                    kitchen.first().orderQuantity -= comingPatties;
                    if (kitchen.first().orderPlaceTime <= lastPattiesRemoveTime) {
                        ((Patties) griddle.last()).cookingStartTime = lastPattiesRemoveTime;
                        globalTime = lastPattiesRemoveTime;
                    } else {
                        ((Patties) griddle.last()).cookingStartTime = kitchen.first().orderPlaceTime;
                        globalTime = kitchen.first().orderPlaceTime;
                    }
                    if (kitchen.first().orderQuantity == 0) {
                        kitchen.first().departureTime = globalTime + 11;
                        kitchen.deleteCustomer();
                    }
                } else if (griddleState != 0 && kitchen.size() != 0) {
                    if (((Patties) griddle.first()).cookingStartTime + 10 <= globalTime) {
                        lastPattiesRemoveTime = ((Patties) griddle.first()).cookingStartTime + 10;
                        griddleState -= ((Patties) griddle.first()).pattiesCount;
                        griddle.dequeue();
                    } else {
                        int comingPatties = Math.min(griddleSize - griddleState, kitchen.first().orderQuantity);
                        griddleState += comingPatties;
                        Patties p = new Patties();
                        p.pattiesCount = comingPatties;
                        griddle.enqueue(p);
                        kitchen.first().orderQuantity -= comingPatties;
                        ((Patties) griddle.last()).pattiesCount = comingPatties;
                        if (kitchen.first().orderPlaceTime <= lastPattiesRemoveTime) {
                            ((Patties) griddle.last()).cookingStartTime = lastPattiesRemoveTime;
                            globalTime = lastPattiesRemoveTime;
                        } else {
                            ((Patties) griddle.last()).cookingStartTime = kitchen.first().orderPlaceTime;
                            globalTime = kitchen.first().orderPlaceTime;
                        }
                        if (kitchen.first().orderQuantity == 0) {
                            kitchen.first().departureTime = globalTime + 11;
                            kitchen.deleteCustomer();
                        }
                    }
                } else if (griddleState != 0 && kitchen.size() == 0) {
                    if (((Patties) griddle.first()).cookingStartTime + 10 <= t) {
                        lastPattiesRemoveTime = globalTime = ((Patties) griddle.first()).cookingStartTime + 10;
                        griddleState -= ((Patties) griddle.first()).pattiesCount;
                        griddle.dequeue();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            globalTime = t;
        } else {
            throw new IllegalNumberException("Time already passed.");
        }
    }

    // throw new java.lang.UnsupportedOperationException("Not implemented yet.");

    public void arriveCustomer(int key, int t, int numb) throws IllegalNumberException {
        // your implementation
        if (t >= globalTime && customerAVLTree.getCustomerNode(key) == null) {
            advanceTime(t);
            int minCounter = 1;
            for (int i = 1; i <= counters.length - 1; i++) {
                if (counters[i].customerQueue.size() < counters[minCounter].customerQueue.size()) {
                    minCounter = i;
                    if (counters[minCounter].customerQueue.size() == 0)
                        break;
                }
            }
            Customer c = new Customer(key, t, numb, minCounter);
            counters[minCounter].customerQueue.enqueue(c);
            customerAVLTree.insertCustomerNode(c);
        } else {
            throw new IllegalNumberException("Illegal Number.");
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public int customerState(int key, int t) throws IllegalNumberException {
        // your implementation
        if (t >= globalTime) {
            advanceTime(t);
            Customer c = (Customer) customerAVLTree.getCustomerNode(key);
            if (c == null) {
                // System.out.println(0);
                return 0;
            } else {
                if (c.orderPlaceTime == -1) {
                    // System.out.println(c.counterID);
                    return c.counterID;
                } else if (c.departureTime == -1 || c.departureTime > t) {
                    // System.out.println(counters.length);
                    return counters.length;
                } else {
                    // System.out.println(counters.length + 1);
                    return counters.length + 1;
                }
            }
        } else {
            throw new IllegalNumberException("Illegal Number.");
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public int griddleState(int t) throws IllegalNumberException {
        // your implementation
        if (t >= globalTime) {
            advanceTime(t);
            // System.out.println(griddleState);
            return griddleState;
        } else {
            throw new IllegalNumberException("Illegal Number.");
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public int griddleWait(int t) throws IllegalNumberException {
        // your implementation
        if (t >= globalTime) {
            advanceTime(t);
            int count = 0;
            for (int i = 0; i < kitchen.size(); i++) {
                count += kitchen.pq.get(i).orderQuantity;
            }
            // System.out.println(count);
            return count;
        } else {
            throw new IllegalNumberException("Time already passed.");
        }

        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public int customerWaitTime(int key) throws IllegalNumberException {
        // your implementation
        Customer c = (Customer) customerAVLTree.getCustomerNode(key);
        if (c != null) {
            // System.out.println(c.departureTime - c.arrivalTime);
            return c.departureTime - c.arrivalTime;
        } else {
            throw new IllegalNumberException("Not found.");
        }
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public float avgWaitTime() {
        // your implementation
        int sum = 0, count = 0;
        Customer root = (Customer) customerAVLTree.root;
        Customer current, pre;
        current = root;
        while (current != null) {
            if (current.left == null) {
                sum += current.departureTime - current.arrivalTime;
                count++;
                current = (Customer) current.right;
            } else {
                pre = (Customer) current.left;
                while (pre.right != null && pre.right != current)
                    pre = (Customer) pre.right;
                if (pre.right == null) {
                    pre.right = current;
                    current = (Customer) current.left;
                } else {
                    pre.right = null;
                    sum += current.departureTime - current.arrivalTime;
                    count++;
                    current = (Customer) current.right;
                }
            }
        }
        float avg = (float) sum / (float) count;
        // System.out.println(avg);
        return avg;
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
}
