package observer;

@FunctionalInterface
public interface OnFinishObserver {
    void update(Object o);
}
