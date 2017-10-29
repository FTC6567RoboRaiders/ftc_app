package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.roboraiders.Robot.RoboRaidersAuto;
import com.roboraiders.Robot.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Nick on 10/8/17.
 */

@Autonomous

public class SensorTest extends RoboRaidersAuto {

    public Robot robot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.initialize(hardwareMap);
        telemetry.addData("Initialized", true);
        telemetry.update();

        waitForStart();

        //while (opModeIsActive()) {

            /*robot.colorSensor.red();
            robot.colorSensor.blue();
            robot.distanceSensor.getDistance(DistanceUnit.CM);
            robot.digitalTouch.getState();
            robot.angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(robot.relicTemplate);

            if (vuMark.equals(RelicRecoveryVuMark.LEFT)) {

                robot.pictograph = "LEFT";
            } else if (vuMark.equals(RelicRecoveryVuMark.CENTER)) {

                robot.pictograph = "CENTER";
            } else if (vuMark.equals(RelicRecoveryVuMark.RIGHT)) {

                robot.pictograph = "RIGHT";
            } else {

                robot.pictograph = "UNKNOWN";
            }

            telemetry.addData("Red", robot.colorSensor.red());
            telemetry.addData("Blue", robot.colorSensor.blue());
            telemetry.addData("Distance", robot.distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("Pictograph", robot.pictograph);
            telemetry.addData("IMU Angle", robot.angles.firstAngle);
            telemetry.addData("Touch", robot.digitalTouch.getState());
            telemetry.update();

            imuTurnLeft(robot, 90, 0.5);
            Thread.sleep(1000);
            imuTurnRight(robot, 90, 0.5);
            Thread.sleep(1000);*/

            encodersStrafeLeft(robot, 30, 0.25);
            Thread.sleep(1000);
            encodersStrafeRight(robot, 30, 0.25);
            Thread.sleep(1000);

            //touchSensorCount(robot, 2, 0.2);
            //distanceSensorCount(robot, 3, 0.3);
        //}
    }
}