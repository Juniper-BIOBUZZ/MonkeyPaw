package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

@Config
@TeleOp
public class Monkey_Paw_Controlled extends LinearOpMode {

    private ServoWrapperClass elbow;
    private ServoWrapperClass wrist;
    private DcMotorEx encoder;

    public static int time=3000;
    private Timing.Timer homingTimer = new Timing.Timer(time, TimeUnit.MILLISECONDS);

    public static int x; //for when needing to tune deg
    public static int y; //for when needing to tune deg
    private double currentElbowTarget = 0.0;
    double currentWristTarget = 0.0;

    double actualWristAngle;
    double actualElbowAngle;

    String targetStatusElbow = "";
    String targetStatusWrist = "";
    String targetStatusMechanism = "";


    double tolerance = 5.0;
    boolean elbowCorrect = false;
    boolean wristCorrect = false;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        encoder = hardwareMap.get(DcMotorEx.class, "encoder");
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow = new ServoWrapperClass(hardwareMap.get(Servo.class, "elbow"),true,encoder,10,150.0,52); //26-16 gear ratio (16 is ouput,26 is servo one)
        wrist = new ServoWrapperClass(hardwareMap.get(Servo.class, "wrist"),false,null,10.0,180.0,0 );

        homingTimer.start();
        while(!homingTimer.done() && !isStopRequested()){
            elbow.setAngle(0);
            elbow.runToPos();
            wrist.setAngle(90);
            wrist.runToPos();;
        }
        if(homingTimer.done()){
            encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        telemetry.addData("homingTimer: Status:", homingTimer.done());
        telemetry.addData("Encoder pos:", encoder.getCurrentPosition());
        telemetry.update();
        while (opModeIsActive()) {
            if (gamepad1.cross){ //parallel
                currentElbowTarget = 69.0;
                if (currentElbowTarget < elbow.minAngle) {
                    currentElbowTarget = elbow.minAngle;
                } else if (currentElbowTarget > elbow.maxAngle) {
                    currentElbowTarget = elbow.maxAngle;
                }
                elbow.setAngle(currentElbowTarget);
                elbow.runToPos();

                currentWristTarget = 10.0;
                if (currentWristTarget < wrist.minAngle) {
                    currentWristTarget = wrist.minAngle;
                } else if (currentWristTarget > wrist.maxAngle) {
                    currentWristTarget = wrist.maxAngle;
                }
                wrist.setAngle(currentWristTarget);
                wrist.runToPos();
            }
            ///
            if (gamepad1.triangle){//extract
                currentElbowTarget = 10.0;
                if (currentElbowTarget < elbow.minAngle) {
                    currentElbowTarget = elbow.minAngle;
                } else if (currentElbowTarget > elbow.maxAngle) {
                    currentElbowTarget = elbow.maxAngle;
                }
                elbow.setAngle(currentElbowTarget);
                elbow.runToPos();

                currentWristTarget = 90.0;
                wrist.setAngle(currentWristTarget);
                wrist.runToPos();
            }
            ///
            actualWristAngle = wrist.getAngle();
            actualElbowAngle = elbow.getAngle();
            elbowCorrect = Math.abs(actualElbowAngle - currentElbowTarget) <= tolerance;
            wristCorrect = Math.abs(actualWristAngle - currentWristTarget) <= tolerance;

            if (elbowCorrect) {
                targetStatusElbow = "AT TARGET";
            } else {
                targetStatusElbow = "MOVING/NOT AT TARGET";
            }
            if (wristCorrect) {
                targetStatusWrist = "AT TARGET";
            } else {
                targetStatusWrist = "MOVING/NOT AT TARGET";
            }
            if(elbowCorrect && wristCorrect){
                targetStatusMechanism = "AT TARGET";
            } else {
                targetStatusMechanism = "MOVING/NOT AT TARGET";
            }

            /* for when needing to test
            elbow.setAngle(x);
            elbow.runToPos();
            wrist.setAngle(y);
            wrist.runToPos();
*/
            telemetry.addData("CurrentElbowAngle:", actualElbowAngle);
            telemetry.addData("ElbowTargetAngle:", currentElbowTarget);
            telemetry.addData("Elbow Status:", targetStatusElbow);

            telemetry.addData("CurrentWristAngle:", actualWristAngle);
            telemetry.addData("WristTargetAngle:", currentWristTarget);
            telemetry.addData("Wrist Status:", targetStatusWrist);

            telemetry.addData("Monkey Paw Status:", targetStatusMechanism);

            telemetry.addData("EncoderPOS", encoder.getCurrentPosition());

            telemetry.update();
        }
        waitForStart();
    }
}

