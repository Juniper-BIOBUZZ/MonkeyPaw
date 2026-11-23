package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "MonkeyPawFSM-Namya")
public class MonkeyPaw_MainTP extends LinearOpMode {
    public int time = 2500;
    private Timing.Timer homingTimer = new Timing.Timer(time, TimeUnit.MILLISECONDS);
    private ServoWrapperClass elbow;
    private ServoWrapperClass wrist;

    private MonkeyPawFSM monkeyPawFSM = new MonkeyPawFSM();

    @Override
    public void runOpMode()  {
        DcMotorEx encoder = hardwareMap.get(DcMotorEx.class, "encoder");
        elbow = new ServoWrapperClass(hardwareMap.get(Servo.class, "elbow"),true,encoder,10,150.0,50.5);//can replace with var for tuning //26-16 gear ratio (16 is ouput,26 is servo one)
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
        telemetry.addData("Encoder pos:", encoder.getCurrentPosition());
        telemetry.addData("Status", "Initialized and Set");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()){
            //the content
            monkeyPawFSM.findTargetState(gamepad1.cross, gamepad1.triangle);
            monkeyPawFSM.updateState(elbow, wrist);

            //telemetry
            telemetry.addData("CurrentElbowAngle:", elbow.getAngle());
            telemetry.addData("ElbowTargetAngle:", monkeyPawFSM.getCurrentElbowTarget());
            telemetry.addData("Elbow Status:", monkeyPawFSM.getElbowStatus(elbow.getAngle()));

            telemetry.addData("CurrentWristAngle:", wrist.getAngle()); // Or mechanismFSM.getCurrentWristTarget() if wrapper tracks angle
            telemetry.addData("WristTargetAngle:", monkeyPawFSM.getCurrentWristTarget());
            telemetry.addData("Wrist Status:", monkeyPawFSM.getWristStatus());

            telemetry.addData("Monkey Paw Status:", monkeyPawFSM.getCurrentState());

            telemetry.addData("EncoderPOS:", encoder.getCurrentPosition());

            telemetry.update();
        }
    }
}
