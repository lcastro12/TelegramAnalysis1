package org.telegram.messenger;

import java.util.HashMap;

public class Action {
    public ActionDelegate delegate;
    public int state;

    public interface ActionDelegate {
        void ActionDidFailExecution(Action action);

        void ActionDidFinishExecution(Action action, HashMap<String, Object> hashMap);
    }

    public void execute(HashMap params) {
    }

    public void cancel() {
    }
}
