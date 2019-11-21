package IA;

import java.util.ArrayList;
import java.util.List;

public class Rewards {
    private List<Double> rewardsPerRound;

    // Collected reward during one round
    private double roundReward;

    // Collected reward during one cycle
    private double cycleReward;

    Rewards() {
        this.rewardsPerRound = new ArrayList<>();
        this.roundReward = 0;
        this.cycleReward = 0;
    }

    public void addReward(RewardType rewardType) {
        this.roundReward += rewardType.getReward();
        this.cycleReward += rewardType.getReward();
    }

    public double getRoundReward() {
        return roundReward;
    }

    public double getCycleReward() {
        return this.cycleReward;
    }

    public void endOfCycle() {
        this.cycleReward = 0;
    }

    public void endOfRound() {
        this.rewardsPerRound.add(this.roundReward);
        // Every round starts calculating a reward from 0
        this.roundReward = 0;
        this.cycleReward = 0;
    }

    public List<Double> getRewardsPerRound() {
        return rewardsPerRound;
    }
}
