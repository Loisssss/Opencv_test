package project1.lois.com.opencv_test;

import android.content.Context;
import android.hardware.Camera;

import org.opencv.android.JavaCameraView;

public class CameraView extends JavaCameraView {

    public CameraView(Context context, int attrs){
        super(context, attrs);
    }

    public void setFps() {
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewFpsRange( 1, 2 );
        mCamera.setParameters( params );
    }


}
