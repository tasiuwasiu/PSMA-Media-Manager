package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class VideoRecorderServiceFragment extends Fragment implements SurfaceHolder.Callback
{
    MediaRecorder mediaRecorder;
    SurfaceHolder holder;
    CamcorderProfile camcorderProfile;
    Camera camera;
    String path;
    long length;
    boolean isRecording = false;
    boolean useCamera = true;
    boolean previewRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);

    }

    public void setSurfaceView(SurfaceView sv)
    {
        holder = sv.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void prepareRecorder()
    {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setPreviewDisplay(holder.getSurface());

        if (useCamera) {
            camera.unlock();
            mediaRecorder.setCamera(camera);


        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setProfile(camcorderProfile);
    }

    public void startRecording(String path)
    {
        isRecording = true;
        this.path = path;
        prepareRecorder();
        mediaRecorder.setOutputFile(path);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        length = System.currentTimeMillis();
    }

    public void stopRecording()
    {
        mediaRecorder.stop();
        if (useCamera) {
            try {
                camera.reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isRecording = false;
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        if (useCamera) {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if (!isRecording && useCamera) {
            if (previewRunning) {
                camera.stopPreview();
            }
            try {
                Camera.Parameters p = camera.getParameters();
                List<Camera.Size> previewSizes = p.getSupportedPreviewSizes();
                Camera.Size previewSize = previewSizes.get(0);

                p.setPreviewSize(previewSize.width, previewSize.height);
                p.setPreviewFrameRate(camcorderProfile.videoFrameRate);

                camera.setParameters(p);
                camera.setPreviewDisplay(holder);


                int rotation = getActivity().getWindowManager().getDefaultDisplay()
                        .getRotation();
                if (rotation == Surface.ROTATION_0)
                    camera.setDisplayOrientation(270);
                else if (rotation == Surface.ROTATION_90)
                    camera.setDisplayOrientation(180);
                else if (rotation == Surface.ROTATION_180)
                    camera.setDisplayOrientation(0);
                else if (rotation == Surface.ROTATION_270)
                    camera.setDisplayOrientation(0);


                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (isRecording) {
            mediaRecorder.stop();
            isRecording = false;
            mediaRecorder.release();
        }


        if (useCamera) {
            previewRunning = false;
            camera.release();
        }
    }

    public boolean isRecording()
    {
        return isRecording;
    }

    public long getLength()
    {
        return System.currentTimeMillis() - length;
    }

    public String getPath()
    {
        return path;
    }
}
