// Generated by Recorder.java
package org.firstinspires.ftc.teamcode.TeleOp.MainTeleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(group="Recorder", name="Replayer")
public class Replayer extends LinearOpMode {

    // AUTOGEN
    double[][] data = {{}, {}};
    String[] motorNames = {""};
    String[] servoNames = {""};




    DcMotor[] motors = new DcMotor[motorNames.length];
    Servo[] servos = new Servo[servoNames.length];
    @Override
    public void runOpMode() throws InterruptedException {
        for (int i = 0; i < motorNames.length; i++) {
            motors[i] = hardwareMap.get(DcMotor.class, motorNames[i]);
        }
        for (int i = 0; i < servoNames.length; i++) {
            servos[i] = hardwareMap.get(Servo.class, servoNames[i]);
        }

        waitForStart();

        replay(data);
    }


    // Yields, synchronous
    public void replay(double[][] data) {
        long replayingStartTime = System.nanoTime();
        int i = 0;
        // Busy looping for each ms (assuming replay loop time is less than recording loop time)
        int length = data[0].length;
        while (opModeIsActive()) {
            while ((long) data[0][i] > System.nanoTime() - replayingStartTime) {
                // Busy loop
            }


            // Motors

            // If needed for (more) accuracy, factor in voltage difference (UNTESTED)
//            double startVoltage = voltageSensor.getVoltage();
//            double powerMultiplier = (double) data[1].get(i) / startVoltage; // assumes linear model
//            for (int i2 = 0; i2 < motors.size(); i2++) {
//                motors.get(i2).setPower((double) data[i2 + 2].get(i) * powerMultiplier);
//
//                telemetry.addData("a", (double) data[i2 + 2].get(i) * powerMultiplier);
//            }

            // Don't factor in voltage (TESTED)
            for (int i2 = 0; i2 < motors.length; i2++) {
                motors[i2].setPower(data[i2 + 2][i]);
            }

            // Servos
            for (int i2 = 0; i2 < servos.length; i2++) {
                servos[i2].setPosition(data[i2 + motors.length + 2][i]);
            }

            // Other code
            // Stop replaying
//            if (gamepad1.b) {
//                break;
//            }

            i = i + 1;
            if (i == length) {
                break;
            }



            // telemetry shit
            telemetry.addData("REPLAYING", i + "/" + length);
            telemetry.update();
        }
    }

}
