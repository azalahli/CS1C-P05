package lazyTrees;

/**
 * Created by Mober6 on 5/17/2017.
 */

public interface Traverser<E> {
    public void visit(E x);
}

class PrintObject<E> implements Traverser<E> {
    public void visit(E x) {
        System.out.print(x + " ");
    }
};