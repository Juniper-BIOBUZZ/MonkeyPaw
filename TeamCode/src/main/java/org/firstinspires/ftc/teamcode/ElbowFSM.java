package org.firstinspires.ftc.teamcode;

public class ElbowFSM {
    public enum ElbowState {
        AT_TARGET,
        NOT_AT_TARGET
    }
    private ElbowState currentState = ElbowState.NOT_AT_TARGET;
    private final double tolerance = 5.0;
    public ElbowState updateStatus(double currentAngle, double targetAngle){
        if (Math.abs(currentAngle - targetAngle) <= tolerance) {
            currentState = ElbowState.AT_TARGET;
        } else {
            currentState = ElbowState.NOT_AT_TARGET;
        }
        return currentState;
        }
    }
