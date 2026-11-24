package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp(name = "MonkeyPawFSM-Namya")
public class MonkeyPaw_MainTP extends LinearOpMode {
    private MonkeyPawFSM monkeyPawFSM;
    @Override
    public void runOpMode()  {
        monkeyPawFSM = new MonkeyPawFSM(hardwareMap);
        DcMotorEx encoder = hardwareMap.get(DcMotorEx.class, "encoder");

        while(!isStopRequested()){
          monkeyPawFSM.retractAll();
        }
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Encoder pos:", encoder.getCurrentPosition());
        telemetry.addData("Status", "Initialized and Set");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()){
            //the content
            monkeyPawFSM.findTargetState(gamepad1.cross, gamepad1.triangle);
            monkeyPawFSM.updateState();

            //telemetry
            telemetry.addData("CurrentElbowAngle:", monkeyPawFSM.getElbowAngle());
            telemetry.addData("ElbowTargetAngle:", monkeyPawFSM.getElbowTarget());
            telemetry.addData("Elbow Status:", monkeyPawFSM.getElbowStatus());

            telemetry.addData("CurrentWristAngle:", monkeyPawFSM.getWristAngle());
            telemetry.addData("WristTargetAngle:", monkeyPawFSM.getWristTarget());
            telemetry.addData("Wrist Status:", monkeyPawFSM.getWristStatus());

            telemetry.addData("Monkey Paw Status:", monkeyPawFSM.getCurrentState());

            telemetry.addData("EncoderPOS:", encoder.getCurrentPosition());

            telemetry.update();
        }
    }
}
