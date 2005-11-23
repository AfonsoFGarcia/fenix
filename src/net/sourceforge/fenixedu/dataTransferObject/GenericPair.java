/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class GenericPair<T, V> {

    private T left;

    private V right;

    public GenericPair(T left, V right) {
        super();
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public V getRight() {
        return right;
    }

    public void setRight(V right) {
        this.right = right;
    }

}
