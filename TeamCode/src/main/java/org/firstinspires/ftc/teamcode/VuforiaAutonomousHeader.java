package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Katelin Zichittella on 11/6/16.
 */

public abstract class VuforiaAutonomousHeader extends LinearOpMode {

    DcMotor motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight;

    public VuforiaLocalizer vuforiaLocalizer;
    public VuforiaLocalizer.Parameters parameters;
    public VuforiaTrackables visionTargets;
    public VuforiaTrackable target;
    public VuforiaTrackableDefaultListener listener;

    public OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY = "AedUDNP/////AAAAGXH2ZpUID0KanSX9ZSR37LKFSFokxIqmy/g0BNepdA9EepixxnO00qygLnMJq3Fg9gZxnkUJaKgk14/UjhxPWVQIs90ZXJLc21NvQvOeZ3dOogagVP8yFnFQs2xCijGmC/CE30ojlAnbhAhqz1y4tZPW2QkK5Qt0xCakTTSAw3KPQX2mZxX+qMxI2ljrN0eaxaKVnKnAUl8x3naF1mez7f9c8Xdi1O5auL0ePdG6bJhWjEO1YwpSd8WkSzNDEkmw20zpQ7zaOOPw5MeUQUr9vAS0fef0GnLjlS1gb67ajUDlEcbbbIeSrLW/oyRGTil8ueQC2SWafdspSWL3SJNaQKWydies23BxJxM/FoLuYYjx";

    int MAX_TARGETS = 4;
    double ON_AXIS = 10;
    double CLOSE_ENOUGH = 20;

    double YAW_GAIN = 0.018;
    double LATERAL_GAIN = 0.0027;
    double AXIAL_GAIN = 0.0017;

    VuforiaTrackables targets = null;
    boolean targetFound = false;
    String targetName = null;
    double robotX = 0;
    double robotY = 0;
    double robotBearing = 0;
    double targetRange = 0;
    double targetBearing = 0;
    double relativeBearing = 0;
    /*
    public boolean cruiseControl(double standOffDistance) {
        boolean closeEnough;


        double Y  = (relativeBearing * YAW_GAIN);


        double L  =(robotY * LATERAL_GAIN);


        double A  = (-(robotX + standOffDistance) * AXIAL_GAIN);

  // Katelin this was in the code we sourced from; you marked that it should be included but we didn't include
  // it the other day at your house so I wasn't sure if we skipped it on purpose... I commented it out bc
  // the variable myRobot is established in a part that you marked not to include and I wasn't sure what I should do.
        myRobot.setYaw(Y);
        myRobot.setAxial(A);
        myRobot.setLateral(L);

        closeEnough = ( (Math.abs(robotX + standOffDistance) < CLOSE_ENOUGH) &&
                (Math.abs(robotY) < ON_AXIS));

        return (closeEnough);
    }
    */
    public void initialize() {

        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Initialized", true);
        telemetry.update();
    }

    public void setupVuforia() {

        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        target = visionTargets.get(0);
        target.setName("Wheels Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 90, 90));

        target = visionTargets.get(1);
        target.setName("Tools Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 90, 90));

        target = visionTargets.get(2);
        target.setName("Legos Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 90, 90));

        target = visionTargets.get(3);
        target.setName("Gears Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 90, 90));

        phoneLocation = createMatrix(0, 225, 0, 90, 0, 0);

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    public String formatMatrix(OpenGLMatrix matrix) {

        return matrix.formatAsTransform();
    }

    public void activateTracking() {

        if (targets != null) {

            targets.activate();
        }
    }

    public boolean targetsAreVisible() {

        int targetTestID = 0;

        while ((targetTestID < MAX_TARGETS) && !targetIsVisible(targetTestID)) {

            targetTestID++;
        }
        return (targetFound);
    }

    public boolean targetIsVisible(int targetId) {

        VuforiaTrackable target = targets.get(targetId);
        VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener)target.getListener();
        OpenGLMatrix location  = null;

        if ((target != null) && (listener != null) && listener.isVisible()) {

            targetFound = true;
            targetName = target.getName();

            location = listener.getUpdatedRobotLocation();

            if (location != null) {

                VectorF trans = location.getTranslation();
                Orientation rot = Orientation.getOrientation(location, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                robotX = trans.get(0);
                robotY = trans.get(1);

                robotBearing = rot.thirdAngle;

                targetRange = Math.hypot(robotX, robotY);

                targetBearing = Math.toDegrees(-Math.asin(robotY / targetRange));

                relativeBearing = targetBearing - robotBearing;
            }

            targetFound = true;
        }
        else {

            targetFound = false;
            targetName = "None";
        }

        return targetFound;
    }
// this isn't in the source code, does this have to do with the fact we are specifying it to our own robot?
    public void setMotorPower(double left, double right) {

        motorFrontLeft.setPower(left);
        motorBackLeft.setPower(left);
        motorFrontRight.setPower(right);
        motorBackRight.setPower(right);
    }
}