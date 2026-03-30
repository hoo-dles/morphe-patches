package amazon.avod.fsm;

import com.amazon.avod.fsm.Trigger;

public abstract class StateBase<S, T> {
    // This method orginally has protected access (modified in patch code).
    public void doTrigger(Trigger<T> trigger) {
    }
}