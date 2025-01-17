package org.firstinspires.ftc.teamcode.CV;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ColorDetectionPipeline extends OpenCvPipeline {

    private enum PropPosition {
        NOPOS,
        LEFT,
        CENTER,
        RIGHT
    }

    private final Mat ycrcbMat = new Mat();
//    private final Mat binaryMat = new Mat();

    //TODO: change anchor points to correct points
    private final Point LEFTPOS_TOPLEFT_ANCHOR_POINT = new Point(100, 300);

    private final Point CENTERPOS_TOPLEFT_ANCHOR_POINT = new Point(500, 300);

    private final Point RIGHTPOS_TOPLEFT_ANCHOR_POINT = new Point(1000, 300);

    // Width and height for the bounding boxes
    public static int REGION_WIDTH = 30;
    public static int REGION_HEIGHT = 50;

    // Anchor point definitions
    Point left_pointA = new Point(
            LEFTPOS_TOPLEFT_ANCHOR_POINT.x,
            LEFTPOS_TOPLEFT_ANCHOR_POINT.y
    );
    Point left_pointB = new Point(
            LEFTPOS_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            LEFTPOS_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
    );

    Point center_pointA = new Point(
            CENTERPOS_TOPLEFT_ANCHOR_POINT.x,
            CENTERPOS_TOPLEFT_ANCHOR_POINT.y
    );
    Point center_pointB = new Point(
            CENTERPOS_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            CENTERPOS_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
    );

    Point right_pointA = new Point(
            RIGHTPOS_TOPLEFT_ANCHOR_POINT.x,
            RIGHTPOS_TOPLEFT_ANCHOR_POINT.y
    );
    Point right_pointB = new Point(
            RIGHTPOS_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            RIGHTPOS_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
    );

    // Running variable storing the parking position
    private volatile PropDetectionPipeline.PropPosition position = PropDetectionPipeline.PropPosition.NOPOS;

    private final Scalar lowerB;
    private final Scalar upperB;

    public ColorDetectionPipeline(Scalar lowerB, Scalar upperB) {
        this.lowerB = lowerB;
        this.upperB = upperB;
    }


    @Override
    public Mat processFrame(Mat input) {

        Imgproc.rectangle(
                input,
                left_pointA,
                left_pointB,
                new Scalar(255, 255, 255),
                1
        );

        Imgproc.rectangle(
                input,
                center_pointA,
                center_pointB,
                new Scalar(255, 255, 255),
                1
        );

        Imgproc.rectangle(
                input,
                right_pointA,
                right_pointB,
                new Scalar(255, 255, 255),
                1
        );

//        Scalar lowerB = new Scalar(0, 255, 255);
//        Scalar upperB = new Scalar(0, 255, 125);

        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2HSV);

        Mat leftRegion = ycrcbMat.submat(new Rect(left_pointA, left_pointB));
        Mat centerRegion = ycrcbMat.submat(new Rect(center_pointA, center_pointB));
        Mat rightRegion = ycrcbMat.submat(new Rect(right_pointA, right_pointB));

        Core.extractChannel(leftRegion, leftRegion, 1);
        Core.extractChannel(centerRegion, centerRegion, 1);
        Core.extractChannel(rightRegion, rightRegion, 1);

        int leftAvg = (int) Core.mean(leftRegion).val[0];
        int centerAvg = (int) Core.mean(centerRegion).val[0];
        int rightAvg = (int) Core.mean(rightRegion).val[0];
        
        int max = Math.max(leftAvg, Math.max(centerAvg, rightAvg));

        if (max == leftAvg) {
            position = PropDetectionPipeline.PropPosition.LEFT;
            Imgproc.rectangle(
                    input,
                    left_pointA,
                    left_pointB,
                    new Scalar(255, 0, 0),
                    1
            );
        } else if (max == centerAvg) {
            position = PropDetectionPipeline.PropPosition.CENTER;
            Imgproc.rectangle(
                    input,
                    center_pointA,
                    center_pointB,
                    new Scalar(255, 0, 0),
                    1
            );
        } else {
          position = PropDetectionPipeline.PropPosition.RIGHT;
            Imgproc.rectangle(
                    input,
                    right_pointA,
                    right_pointB,
                    new Scalar(255, 0, 0),
                    1
            );
        }

        return input;

    }

    public PropDetectionPipeline.PropPosition getPosition() {
        return position;
    }
}
