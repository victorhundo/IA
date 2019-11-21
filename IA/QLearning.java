package IA;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.*;

public class QLearning {
    protected Table<State, Action, Double> Q;
    public Map<Action, Executable> actionFunctions;
    protected Rewards rewards;

    QLearning() {
        this.Q = HashBasedTable.create();
        this.actionFunctions = new HashMap<>();
    }

    public void init() {
        if (Q.isEmpty()) {
            areAllActionsSet();
            initQ();
        }
    }

    private void areAllActionsSet() {
        for (Action action : Action.values()) {
            if (!actionFunctions.containsKey(action)) {
                String message = "Function for action: " + action.name() + " is not defined.";
                throw new IllegalStateException(message);
            }
        }
    }

    private void initQ() {
        if (!State.states.equals(null)) {
            Set<State> availableStates = State.states;
            for (State state : availableStates) {
                for (Action action : Action.values()) {
                    Executable actionFunc = actionFunctions.get(action);
                    action.setExecutableAction(actionFunc);
                    Q.put(state, action, Constants.INITIAL_Q);
                }
            }
        }
    }

    public void setActionFunction(Action action, Executable function) {
        this.actionFunctions.put(action, function);
    }

    public Action randomAction() {
        Random random = new Random();
        Set<Action> availableActions = Q.columnKeySet();

        int randomInt = random.nextInt(availableActions.size() + 1);
        Action randomAction = availableActions.iterator().next();
        Iterator<Action> iterator = availableActions.iterator();
        for (int i = 0; i < randomInt; i++) {
            randomAction = iterator.next();
        }

        return randomAction;
    }

    public Action bestAction(State state) {
        Action bestAction = this.randomAction();
        double bestQ = getQValue(state, bestAction);

        for (Action action : Action.values()) {
            double actionQ = getQValue(state, action);

            if (actionQ > bestQ) {
                bestQ = actionQ;
                bestAction = action;
            }
        }

        return bestAction;
    }

    public double getQValue(State state, Action action) {
        return Q.get(state, action);
    }

    public void updateQ(State state, Action action, State nextState, Rewards rewards) {
        double reward = rewards.getCycleReward();
        double newQValue = (1 - Constants.ALPHA) * this.getQValue(state, action) +
                Constants.ALPHA * (reward + Constants.GAMMA * this.maxQ(nextState));
        Q.put(state, action, newQValue);
    }

    protected double maxQ(State state) {
        double max = - Double.MAX_VALUE;
        for (Action action : Q.columnKeySet()) {
            if (getQValue(state, action) > max) {
                max = getQValue(state, action);
            }
        }
        return max;
    }

    public Action nextAction(State state, int roundNo) {
        return eGreedyAction(state);
    }

    public Action eGreedyAction(State state) {
        double x = Math.random();

        if (x < Constants.EPSILON) {
            Action randomAction = randomAction();
            return randomAction;
        } else {
            Action bestAction = bestAction(state);
            return bestAction;
        }
    }


}
