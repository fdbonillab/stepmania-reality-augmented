package com.fdbgames.mole.stepmaniac;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.rectangle;

public class MainActivityv2 extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    private Mat                    mRgba;
    private Mat                    mGray;
    private Mat base;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    public boolean hasChanges(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 1000;//5;
        //Mat base = new Mat();
        Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return false;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);

        //Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);

        Imgproc.GaussianBlur(bg, bg, new Size(21, 21), 0);
        Imgproc.GaussianBlur(cg, cg, new Size(21, 21), 0);
        ///Core.absdiff(bg, cg, diff);////parece que resalta el objeto estatico
        Core.absdiff(cg, bg, diff);
        //Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return false;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        //Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        //Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            Log.i(TAG, "****** points " + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 0, 255, 0 );
        Imgproc.drawContours( current, points,-1,color,3);
        Imgproc.putText(current, "intento 1",
                new Point(1, 1),
                Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,0,0));
        base = current.clone();
        return true;
    }
    public boolean hasChanges3(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 1000;//5;
        //Mat base = new Mat();
        Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return false;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);

        //Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);

        Imgproc.GaussianBlur(bg, bg, new Size(21, 21), 0);
        Imgproc.GaussianBlur(cg, cg, new Size(21, 21), 0);
        ///Core.absdiff(bg, cg, diff);////parece que resalta el objeto estatico
        Core.absdiff(cg, bg, diff);
        //Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return false;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        //Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        //Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            Log.i(TAG, "****** points version 3" + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 0, 255, 0 );
        Imgproc.drawContours( current, points,-1,color,3);
        Imgproc.putText(current, "intento 1",
                new Point(1, 1),
                Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,0,0));
        base = current.clone();
        return true;
    }
    public Mat hasChanges2(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;
        //Mat base = new Mat();
        Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return current;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);
        Core.absdiff(bg, cg, diff);
        Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return current;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        //Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        //Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            Log.i(TAG, "****** points " + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 255, 255, 0 );
        Imgproc.drawContours( current, points,-1,color,3);
        base = current.clone();
        return current;
    }

    public MainActivityv2() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    private Mat mFiltrada = null;
    public void onCameraViewStarted(int width, int height) {
        if ( mFiltrada == null){
            mFiltrada = new Mat(width, height, CvType.CV_8UC2);
        }
    }

    public void onCameraViewStopped() {
        if (mFiltrada != null){
           mFiltrada.release();
        }
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        //mRgba = inputFrame.rgba();
        /*if( mFiltrada != null){
            Imgproc.Canny();
        } else {
            return inputFrame.rgba();
        }*/
        //mGray = inputFrame.gray();
        hasChanges3(mRgba);
        //mRgba = hasChanges2(mRgba);
        return inputFrame.rgba();
        //return inputFrame.gray();
    }
}

