package org.firstinspires.ftc.teamcode;

public class MonkeyPawFSM {
    public enum MonkeyPawState{
        IDLE,
        RETRACTED,
        PARALLEL;
    }
    private MonkeyPawState currentState = MonkeyPawState.IDLE;

    private elbowFSM ElbowFSM = new elbowFSM();
    private wristFSM WristFSM = new wristFSM();
    private double currentElbowTarget = 0.0;
    private double currentWristTarget = 0.0;
    public MonkeyPawState findTargetState(boolean cross, boolean triangle) {
        if (cross) {
            if (currentState != MonkeyPawState.PARALLEL) {
                WristFSM.startMovement();
            }
            currentState = MonkeyPawState.PARALLEL;
        } else if (triangle) {
            if (currentState != MonkeyPawState.RETRACTED) {
                WristFSM.startMovement();
            }
            currentState = MonkeyPawState.RETRACTED;
        }
        return currentState;
    }
    public void updateState(ServoWrapperClass elbow, ServoWrapperClass wrist) {
        switch (currentState) {
            case PARALLEL:
                currentElbowTarget = 69.0;
                currentWristTarget = 10.0;
                break;

            case RETRACTED:
                currentElbowTarget = 10.0;
                currentWristTarget = 90.0;
                break;

            case IDLE:
            default:
                return;
        }
        if (currentElbowTarget < elbow.minAngle) {
            currentElbowTarget = elbow.minAngle;
        } else if (currentElbowTarget > elbow.maxAngle) {
            currentElbowTarget = elbow.maxAngle;
        }

        if (currentWristTarget < wrist.minAngle) {
            currentWristTarget = wrist.minAngle;
        } else if (currentWristTarget > wrist.maxAngle) {
            currentWristTarget = wrist.maxAngle;
        }

        elbow.setAngle(currentElbowTarget);
        elbow.runToPos();

        wrist.setAngle(currentWristTarget);
        wrist.runToPos();

        ElbowFSM.updateStatus(elbow.getAngle(), currentElbowTarget);
        WristFSM.updateStatus();
    }

    //state getter-for telemetry
    public MonkeyPawState getCurrentState() {
        return currentState;
    }

    public double getCurrentElbowTarget() {
        return currentElbowTarget;
    }

    public double getCurrentWristTarget() {
        return currentWristTarget;
    }

    public elbowFSM.ElbowState getElbowStatus(double actualAngle) {
        return ElbowFSM.updateStatus(actualAngle, currentElbowTarget);
    }

    public wristFSM.WristState getWristStatus() {
        return WristFSM.updateStatus();
    }
}

