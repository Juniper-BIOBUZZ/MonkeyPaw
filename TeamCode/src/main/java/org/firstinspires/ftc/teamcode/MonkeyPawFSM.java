package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MonkeyPawFSM{
    public enum MonkeyPawState{
        IDLE,
        RETRACTING,
        RETRACTED,
        EXTENDING,
        EXTENDED
    }
    private MonkeyPawState currentState = MonkeyPawState.IDLE;
    private ElbowFSM elbowFSM = new ElbowFSM();
    private WristFSM wristFSM = new WristFSM();
    private ServoWrapperClass elbow;
    private ServoWrapperClass wrist;
    private DcMotorEx encoder;
    public MonkeyPawFSM(HardwareMap hardwareMap) {
        elbow = new ServoWrapperClass(hardwareMap.get(Servo.class, "elbow"),true,encoder,10,150.0,50.5, 10.0, 69.0);//can replace with var for tuning //26-16 gear ratio (16 is ouput,26 is servo one)
        wrist = new ServoWrapperClass(hardwareMap.get(Servo.class, "wrist"),false,null,10.0,180.0,0 , 90.0, 10.0);

    }

    public MonkeyPawState findTargetState(boolean cross, boolean triangle) {
        if (cross) {
            if (currentState != MonkeyPawState.EXTENDED) {
                wristFSM.startMovement();
            }
            currentState = MonkeyPawState.EXTENDING;
        } else if (triangle) {
            if (currentState != MonkeyPawState.RETRACTED) {
                wristFSM.startMovement();
            }
            currentState = MonkeyPawState.RETRACTING;
        }
        return currentState;
    }
    public void updateState() {
        switch (currentState) {
            case EXTENDING:
                elbow.extend();
                wrist.extend();
                if (Math.abs(elbow.getAngle() - elbow.getTargetAngle()) < 2.0 &&
                        Math.abs(wrist.getAngle() - wrist.getTargetAngle()) < 2.0) {
                    currentState = MonkeyPawState.EXTENDED;
                }
                break;

            case RETRACTING:
                elbow.retract();
                wrist.retract();
                if (Math.abs(elbow.getAngle() - elbow.getTargetAngle()) < 2.0 &&
                        Math.abs(wrist.getAngle() - wrist.getTargetAngle()) < 2.0) {
                    currentState = MonkeyPawState.RETRACTED;
                }
                break;

            case IDLE:
            case EXTENDED:
            case RETRACTED:
            default:
                return;
        }

        elbowFSM.updateStatus(elbow.getAngle(), elbow.getTargetAngle());
        wristFSM.updateStatus();
    }

    //state getter-for telemetry
    public MonkeyPawState getCurrentState() {
        return currentState;
    }
    public void retractAll() {
        elbow.retract();
        wrist.retract();
    }
    public ElbowFSM.ElbowState getElbowStatus() {
        return elbowFSM.updateStatus(elbow.getAngle(), elbow.getTargetAngle());
    }

    public WristFSM.WristState getWristStatus() {
        return wristFSM.updateStatus();
    }
    public double getElbowAngle() { return elbow.getAngle(); }
    public double getElbowTarget() { return elbow.getTargetAngle(); }
    public double getWristAngle() { return wrist.getAngle(); }
    public double getWristTarget() { return wrist.getTargetAngle(); }
}

