package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.util.Timing;

import java.util.concurrent.TimeUnit;

public class WristFSM {
    public enum WristState {
        AT_TARGET,
        NOT_AT_TARGET
    }

    public WristState updateStatus() {
        if (moveTimer.done()) {
            state = WristState.AT_TARGET;
        } else {
            state = WristState.NOT_AT_TARGET;
        }
        return state;
    }
    private WristState state = WristState.NOT_AT_TARGET;
    private Timing.Timer moveTimer = new Timing.Timer(400, TimeUnit.MILLISECONDS);
    public void startMovement() {
        moveTimer.start();
    }
}
