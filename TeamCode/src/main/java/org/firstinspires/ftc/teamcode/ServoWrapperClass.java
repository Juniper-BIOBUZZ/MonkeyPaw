package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoWrapperClass {

    // Hardware
    private Servo servo;
    private boolean useEncoder;
    private DcMotorEx encoder;

    // Software hardstops -degrees, right?
    public double minAngle;
    public double maxAngle;

    //Gear ratios and data variables
    private double encoderTicksPerDegree = 1.0;
    private double currentTargetAngle = 0.0;
    private double lastReadEncoderAngle = 0.0;

    //
    private double servoPos;

    public ServoWrapperClass(Servo servo, boolean useEncoder, DcMotorEx encoder, double minAngle, double maxAngle, double encoderTicksPerDegree) {
        this.servo = servo;
        // left side is the thing in the actual class, and right side is what is passed in
        this.useEncoder = useEncoder;
        this.encoder = encoder;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.encoderTicksPerDegree = encoderTicksPerDegree;
    }

    public double readPosition() {
        if (useEncoder && encoder != null) {
            double rawTicks = encoder.getCurrentPosition();
            //first part gets angle, then add minimum so min angle is not breached
            lastReadEncoderAngle = (rawTicks / encoderTicksPerDegree) + minAngle;
        }
        return lastReadEncoderAngle;
    }

    public double getAngle() {
        if (useEncoder) {
            readPosition();
            return lastReadEncoderAngle;
        } else {
            servoPos = servo.getPosition();
            return minAngle + (servoPos * (maxAngle - minAngle));
        }
    }

    public void setAngle(double angle) {
        currentTargetAngle = angle;

    }

    public void runToPos(){
        double position = (currentTargetAngle-minAngle)/(maxAngle-minAngle);
        servo.setPosition(position);
    }

}
