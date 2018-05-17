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
public class ClassifyCoins extends Activity {

    public static void main(String[] arg) throws IOException {

//        List<Mat> images = new ArrayList();
//        File folder = new File("D://Download//euro2_test");
//        File[] listOfFiles = folder.listFiles();
//
//        for (File file : listOfFiles) {
//            if (file.isFile()) {
//                String fileName = file.getName();
//                Mat image = imread("D://Download//euro2_test//" + fileName);
//                System.out.println(fileName);
//                Imgcodecs.imwrite("D://Download//euro2_test//" + fileName, image);
//                images.add(image);
//
//            }
//        }


    }

    public void readPicture(Context context) {
        try {
            //read image
            Mat m = Utils.loadResource(context, R.drawable.euro2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
          //  Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2BGRA);

            // show image
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);
            ImageView iv = (ImageView) findViewById(R.id.image_view);
            iv.setImageBitmap(bm);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

}