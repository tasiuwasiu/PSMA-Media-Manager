package lab.wasikrafal.psmaprojekt;

import android.content.Context;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Preview extends SurfaceView implements SurfaceHolder.Callback
{
    SurfaceHolder holder;

    public Preview (Context context)
    {
        super(context);
        holder=getHolder();
        holder.addCallback(this);
    }

    public Surface getSurface()
    {
        return holder.getSurface();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {

    }
}
