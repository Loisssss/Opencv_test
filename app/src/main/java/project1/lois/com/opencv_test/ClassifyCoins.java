package project1.lois.com.opencv_test;


import android.content.Context;
import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import java.io.IOException;

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