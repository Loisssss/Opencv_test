package project1.lois.com.opencv_test;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static project1.lois.com.opencv_test.MainActivity.TAG;

import android.app.Activity;


import java.io.File;
public class ClassifyCoins {


    public Bitmap readPicture(Context context) {
        Bitmap bitmap = null;
        try {

            //read image
            Mat m = Utils.loadResource(context, R.drawable.euro2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
          //  Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2BGRA);

            // show image
            bitmap = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bitmap);
//            ImageView iv = (ImageView) findViewById(R.id.image_view);
//            iv.setImageBitmap(bitmap);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return bitmap;

    }

}