package project1.lois.com.opencv_test;

/**
 * Created by Lois on 2018/1/29.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    public static final String TAG = "MainActivity";

    private JavaCameraView cameraView;
    private ClassifyCoins coins;
    Drawable drawableEuroPic;
    Bitmap euroImage;

    private Button mybutton;
    private File mPhotoFile;
    private String mPhotoPath;
    public final static int CAMERA_RESULT = 1;

//    static {
//
//        if (!OpenCVLoader.initDebug()) {
//            Log.wtf(TAG, "static initializer: OpenCV failed to load!");
//
//        }
//    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    cameraView.enableView();
                    cameraView.enableFpsMeter();
                    coins = new ClassifyCoins();

                    //camera take picture
                    mybutton = (Button) findViewById(R.id.button);
                    mybutton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                            mPhotoPath = getSDPath()+"/"+ getPhotoFileName();
                                            mPhotoFile = new File(mPhotoPath);
                                        if (!mPhotoFile.exists()) {
                                            mPhotoFile.createNewFile();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(mPhotoFile));
                                        startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据

                                    }catch (Exception e){

                                    }
                                }
                            }
                    );

//                    Bitmap bitmap = coins.readPicture(this.mAppContext) ;
//                    ImageView iv = (ImageView) findViewById(R.id.image_view);

//                    //read image from drawable
//                    drawableEuroPic = getResources().getDrawable(R.mipmap.cent20);
//                    euroImage = ((BitmapDrawable) drawableEuroPic).getBitmap();
//
//                    Log.e("img width:"+euroImage.getWidth(),";img height:"+euroImage.getHeight());
//
//                    // change Bitmap to mat
//                    Mat srcImage = new Mat(euroImage.getHeight(), euroImage.getWidth(), CvType.CV_8UC1);
//                    Utils.bitmapToMat(euroImage, srcImage);
//
//                    //find ellipse in Mat format pic
//                    findEllipses(srcImage);
//
//                    // change Mat to Bitmap
//                    Bitmap processedImage = Bitmap.createBitmap(srcImage.cols(), srcImage.rows(), Bitmap.Config.ARGB_8888);
//                    Utils.matToBitmap(srcImage, processedImage);

                    Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
                    if(mPhotoPath != null && bitmap != null){
                        Mat srcImage = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
                        Utils.bitmapToMat(bitmap, srcImage);
                        findEllipses(srcImage);
                        Bitmap processedImage = Bitmap.createBitmap(srcImage.cols(), srcImage.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(srcImage, processedImage);
                        ImageView iv = (ImageView) findViewById(R.id.image_view);
                        iv.setImageBitmap(processedImage);
                    }

                    
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = findViewById(R.id.camera_view);
        cameraView.setCvCameraViewListener(this);


//        ClassifyCoins coins = new ClassifyCoins();
//        coins.readPicture(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    int count = 0;
    private Mat previousFrame;
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        Mat input = inputFrame.gray();
        //findEllipses(inputFrame.gray());
//        if( count == 0 ){
//            count = 60;
//           findEllipses(inputFrame.gray());
//            previousFrame = inputFrame.rgba();
//        }
//        count--;
//        return previousFrame;
        findEllipses(inputFrame.gray());
        return inputFrame.rgba();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void findEllipses(Mat input) {
        List<MatOfPoint> contours = new ArrayList<>();

        // Pre-processing
        Mat image = new Mat();
        Imgproc.blur(input, image, new Size(5,5 ));
        Imgproc.Canny(image, image, 100, 280);

        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        for( int i = 0; i < contours.size(); i++){
//           Imgproc.drawContours(input,contours, i, new Scalar(0, 100, 255), 2);
        }
        // Find rotated rectangles
        List<RotatedRect> rects = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            Point[] array = contours.get(i).toArray();
            // Fit ellipse in contours
            if (array.length > 5) {
                RotatedRect rect = Imgproc.fitEllipse(new MatOfPoint2f(array));
                if(checkEllipse(array, rect)){
                    rects.add(rect);
//                    Log.i(TAG, "rect width " + rect.size.width);
//                    Log.i(TAG, "rect height" + rect.size.height);
                }
            }
        }
        Log.i(TAG, "Contour count " + contours.size());
        Log.i(TAG, "Ellipse count " + rects.size());

        for (RotatedRect rect: rects) {
            Point[] pts = new Point[4];
            rect.points(pts);
          //  Imgproc.rectangle(input, pts[0], pts[2], new Scalar(255, 0, 0, 0), 2);
            Imgproc.ellipse(input, rect, new Scalar(255, 255, 255), 2);

        }
    }

    private boolean checkEllipse(Point[] array, RotatedRect rect){

        double threshold_error = 10.0;
        double threshould_ratio = 0.65;
        double threshold_area_min = 100;
        double threshold_area_max = 650;


        return ( calculateError(array, rect) < threshold_error )
                && ( calculateRatioofAxis(rect) > threshould_ratio
                && (calculateArea(rect) > threshold_area_min)
//                && (calculateArea(rect) < threshold_area_max)
        );
    }


    private  double calculateArea(RotatedRect rect){
        double ellipseArea;
        ellipseArea = rect.size.area();
        return  ellipseArea / 100;
    }

    private double calculateRatioofAxis( RotatedRect rect){
        double ratio = 0;
        ratio = Math.abs((rect.size.width / 2 ) / (rect.size.height / 2));
//        Log.i(TAG, "calculateRatio: " + ratio);

        return ratio;
    }

    private double calculateError(Point[] array, RotatedRect rect){
        double err = 0;
        for (Point point: array) {
            double f = (Math.pow(point.x - rect.center.x, 2) / Math.pow(rect.size.width / 2, 2)) +
                    (Math.pow(point.y - rect.center.y, 2) / Math.pow(rect.size.height / 2, 2));
            err = Math.pow(Math.abs(1.0 - f)  * 10, 2);
//            Log.i(TAG, "calculateError: " + err);
        }

        err = err / array.length;
        return err;
    }

    private void findCircles(Mat input) {
        Mat circles = new Mat();

        // decrease noise to avoid detect the wrong circle
        Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));
        // use HoughCircle to find circle
        Imgproc.HoughCircles(input, circles, Imgproc.CV_HOUGH_GRADIENT, 2, 50, 100, 90, 20, 200);
        Log.i(TAG, String.valueOf("size: " + circles.cols()) + ", " + String.valueOf(circles.rows()));

        // draw the circle which be detected
        if (circles.cols() > 0) {

            for (int x = 0; x < Math.min(circles.cols(), 5); x++ ) {
                double circleVec[] = circles.get(0, x);

                if (circleVec == null) {
                    break;
                }

                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];

                // draw the center of circle
                Imgproc.circle(input, center, 3, new Scalar(0, 0, 50), 5);
                //draw the contour of circle
                Imgproc.circle(input, center, radius, new Scalar(255, 255, 255), 5);
            }
        }

        circles.release();
        input.release();
    }


    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }


    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date)  +".jpg";
    }

}