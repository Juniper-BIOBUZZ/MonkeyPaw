package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.config.Config;


@TeleOp
@Config
public class test extends LinearOpMode{
    public Servo test;
    public Servo test2;
    public static double x;
    public static double x2;
    int minAngle = 0;
    int maxAngle = 180;

    public void runOpMode(){
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        test=hardwareMap.get(Servo.class, "test");
        test2=hardwareMap.get(Servo.class, "test2");
        waitForStart();
        while (opModeIsActive()){
            test.setPosition(x);
            double y = test.getPosition();
            test2.setPosition(x2);
            double y2 = test2.getPosition();

            double currentAngle1 = minAngle + (y * (maxAngle - minAngle));
            double currentAngle2 = minAngle + (y2 * (maxAngle - minAngle));
            double targetAngle1 = minAngle + (x * (maxAngle - minAngle));
            double targetAngle2 = minAngle + (x2 * (maxAngle - minAngle));

            telemetry.addData("currentELBOW", y);
            telemetry.addData("targetELBOW", x);
            telemetry.addData("currentWRIST", y2);
            telemetry.addData("targetWRIST", x2);
            telemetry.addData("currentElbowAng", currentAngle1);
            telemetry.addData("targetElbowAng", targetAngle1);
            telemetry.addData("currentWRISTAng", currentAngle2);
            telemetry.addData("targetWRISTAng", targetAngle2);

            telemetry.update();
        }
    }
}
