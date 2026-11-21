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
    private Timing.Timer homingTimer = new Timing.Timer(1500, TimeUnit.MILLISECONDS);

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        encoder = hardwareMap.get(DcMotorEx.class, "encoder");
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow = new ServoWrapperClass(hardwareMap.get(Servo.class, "elbow"),true,encoder,0.0,0.0,0.0 );
        wrist = new ServoWrapperClass(hardwareMap.get(Servo.class, "wrist"),false,null,0.0,0.0,0.0 );


        homingTimer.start();
        while(!homingTimer.done() && !isStopRequested()){
            elbow.setAngle(0);
            elbow.runToPos();
            wrist.setAngle(0);
            wrist.runToPos();
        }
        if(homingTimer.done()){
            encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        waitForStart();
        while (opModeIsActive()) {
            //for the actual movement make it so that it sets + uses target position and the
            //correlation(ratios) to move
            if(gamepad1.cross){
                wrist.setAngle(10);
                wrist.runToPos();
                elbow.setAngle(10);
                elbow.runToPos();
                //need to completely change this so like its connected, like it moves based of
                //each other-need to figure that out
            }
            if (gamepad1.triangle){
                elbow.setAngle(0);
                elbow.runToPos();
                wrist.setAngle(0);
                wrist.runToPos();
                //change to what it should be
            }
            //for elbow you'll like need a loop prob checking pos w/encoder every time to be precise


            telemetry.addData("elbowAngle:", elbow.getAngle());
            telemetry.addData("wristAngle:", wrist.getAngle());
            telemetry.addData("EncoderPOS", encoder.getCurrentPosition());
            telemetry.update();
        }
    }
}
