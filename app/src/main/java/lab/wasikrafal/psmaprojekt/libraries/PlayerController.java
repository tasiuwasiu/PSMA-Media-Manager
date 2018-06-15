package lab.wasikrafal.psmaprojekt.libraries;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;


import lab.wasikrafal.psmaprojekt.R;

public class PlayerController extends FrameLayout
{

    MediaPlayerControl playerControl;
    Context context;
    ViewGroup anchor;
    View root;
    ProgressBar progressBar;
    TextView endTime, currentTime;
    boolean showing;
    boolean dragging;
    private static final int TIMEOUT = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    boolean useFastForward;
    boolean fromXml;
    StringBuilder formatBuilder;
    Formatter formatter;
    ImageButton pauseButton;
    ImageButton ffwdButton;
    ImageButton rewButton;
    ImageButton nextButton;
    ImageButton prevButton;
    Handler handler = new MessageHandler(this);


    public PlayerController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        root = null;
        this.context = context;
        useFastForward = true;
        fromXml = true;
    }

    public PlayerController(Context context, boolean useFastForward)
    {
        super(context);
        this.context = context;
        this.useFastForward = useFastForward;
    }

    public PlayerController(Context context)
    {
        this(context, true);
    }

    @Override
    public void onFinishInflate()
    {
        super.onFinishInflate();
        if (root != null) {
            initControllerView(root);
        }
    }

    public void setMediaPlayer(MediaPlayerControl player)
    {
        playerControl = player;
        updatePausePlay();
    }

    public void setAnchorView(ViewGroup view)
    {
        anchor = view;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView()
    {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflate.inflate(R.layout.controller_player, null);
        initControllerView(root);
        return root;
    }

    private void initControllerView(View v)
    {
        pauseButton = (ImageButton) v.findViewById(R.id.ib_contr_play);
        if (pauseButton != null) {
            pauseButton.requestFocus();
            pauseButton.setOnClickListener(pauseListener);
        }

        ffwdButton = (ImageButton) v.findViewById(R.id.ib_contr_fwd);
        if (ffwdButton != null) {
            ffwdButton.setOnClickListener(ffwdListener);
            if (!fromXml) {
                ffwdButton.setVisibility(useFastForward ? View.VISIBLE : View.GONE);
            }
        }

        rewButton = (ImageButton) v.findViewById(R.id.ib_contr_rew);
        if (rewButton != null) {
            rewButton.setOnClickListener(rewListener);
            if (!fromXml) {
                rewButton.setVisibility(useFastForward ? View.VISIBLE : View.GONE);
            }
        }

        nextButton = (ImageButton) v.findViewById(R.id.ib_contr_next);
        if (nextButton != null) {
            nextButton.setOnClickListener(nextListener);
            if (!fromXml) {
                rewButton.setVisibility(useFastForward ? View.VISIBLE : View.GONE);
            }
        }

        prevButton = (ImageButton) v.findViewById(R.id.ib_contr_prev);
        if (prevButton != null) {
            prevButton.setOnClickListener(prevListener);
            if (!fromXml) {
                rewButton.setVisibility(useFastForward ? View.VISIBLE : View.GONE);
            }
        }

        progressBar = (ProgressBar) v.findViewById(R.id.sb_contr_prog);
        if (progressBar != null) {
            if (progressBar instanceof SeekBar) {
                SeekBar seeker = (SeekBar) progressBar;
                seeker.setOnSeekBarChangeListener(seekListener);
                seeker.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
            progressBar.setMax(1000);
        }

        endTime = (TextView) v.findViewById(R.id.tv_contr_full_time);
        currentTime = (TextView) v.findViewById(R.id.tv_contr_curr);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    public void show()
    {
        show(TIMEOUT);
    }

    private void disableUnsupportedButtons()
    {
        if (playerControl == null) {
            return;
        }

        try {
            if (pauseButton != null && !playerControl.canPause()) {
                pauseButton.setEnabled(false);
            }

            if (rewButton != null && !playerControl.canSeekBackward()) {
                rewButton.setEnabled(false);
            }

            if (ffwdButton != null && !playerControl.canSeekForward()) {
                ffwdButton.setEnabled(false);
            }

            if (prevButton != null && !playerControl.canSeekForward()) {
                prevButton.setEnabled(false);
            }

            if (nextButton != null && !playerControl.canSeekForward()) {
                nextButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            ex.printStackTrace();
        }

    }

    public void show(int timeout)
    {
        if (!showing && anchor != null) {
            setProgress();
            if (pauseButton != null) {
                pauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

            anchor.addView(this, tlp);
            showing = true;
        }
        updatePausePlay();
        handler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = handler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            handler.removeMessages(FADE_OUT);
            handler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing()
    {
        return showing;
    }

    public void hide()
    {
        if (anchor == null) {
            return;
        }
        try {
            anchor.removeView(this);
            handler.removeMessages(SHOW_PROGRESS);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        showing = false;
    }

    private String stringForTime (int timeMs)
    {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (playerControl == null || dragging) {
            return 0;
        }

        int position = playerControl.getCurrentPosition();
        int duration = playerControl.getDuration();
        if (progressBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                progressBar.setProgress( (int) pos);
            }
            int percent = playerControl.getBufferPercentage();
            progressBar.setSecondaryProgress(percent * 10);
        }

        if (endTime != null)
            endTime.setText(stringForTime(duration));
        if (currentTime != null)
            currentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(TIMEOUT);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(TIMEOUT);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (playerControl == null) {
            return true;
        }

        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(TIMEOUT);
                if (pauseButton != null) {
                    pauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !playerControl.isPlaying()) {
                playerControl.start();
                updatePausePlay();
                show(TIMEOUT);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && playerControl.isPlaying()) {
                playerControl.pause();
                updatePausePlay();
                show(TIMEOUT);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(TIMEOUT);
        return super.dispatchKeyEvent(event);
    }

    private View.OnClickListener pauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(TIMEOUT);
        }
    };


    public void updatePausePlay() {
        if (root == null || pauseButton == null || playerControl == null) {
            return;
        }

        if (playerControl.isPlaying()) {
            pauseButton.setImageResource( R.drawable.ic_media_pause);
        } else {
            pauseButton.setImageResource( R.drawable.ic_media_play  );
        }
    }

    private void doPauseResume() {
        if (playerControl == null) {
            return;
        }

        if (playerControl.isPlaying()) {
            playerControl.pause();
        } else {
            playerControl.start();
        }
        updatePausePlay();
    }


    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.

    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            dragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            handler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (playerControl == null) {
                return;
            }

            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = playerControl.getDuration();
            long newposition = (duration * progress) / 1000L;
            playerControl.seekTo( (int) newposition);
            if (currentTime != null)
                currentTime.setText(stringForTime( (int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            dragging = false;
            setProgress();
            updatePausePlay();
            show(TIMEOUT);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (pauseButton != null) {
            pauseButton.setEnabled(enabled);
        }
        if (ffwdButton != null) {
            ffwdButton.setEnabled(enabled);
        }
        if (rewButton != null) {
            rewButton.setEnabled(enabled);
        }
        if (nextButton != null) {
            nextButton.setEnabled(enabled);
        }
        if (prevButton != null) {
            prevButton.setEnabled(enabled);
        }
        if (progressBar != null) {
            progressBar.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(PlayerController.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(PlayerController.class.getName());
    }

    private View.OnClickListener rewListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (playerControl == null) {
                return;
            }

            int pos = playerControl.getCurrentPosition();
            pos -= 5000; // milliseconds
            playerControl.seekTo(pos);
            setProgress();

            show(TIMEOUT);
        }
    };

    private View.OnClickListener ffwdListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (playerControl == null) {
                return;
            }

            int pos = playerControl.getCurrentPosition();
            pos += 5000; // milliseconds
            playerControl.seekTo(pos);
            setProgress();

            show(TIMEOUT);
        }
    };

    private View.OnClickListener nextListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (playerControl == null) {
                return;
            }

            int pos = playerControl.getCurrentPosition();
            pos += 10000; // milliseconds
            playerControl.seekTo(pos);
            setProgress();

            show(TIMEOUT);
        }
    };
    private View.OnClickListener prevListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (playerControl == null) {
                return;
            }

            int pos = playerControl.getCurrentPosition();
            pos -= 10000; // milliseconds
            playerControl.seekTo(pos);
            setProgress();

            show(TIMEOUT);
        }
    };

    public interface MediaPlayerControl {
        void    start();
        void    pause();
        int     getDuration();
        int     getCurrentPosition();
        void    seekTo(int pos);
        boolean isPlaying();
        int     getBufferPercentage();
        boolean canPause();
        boolean canSeekBackward();
        boolean canSeekForward();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<PlayerController> mView;

        MessageHandler(PlayerController view) {
            mView = new WeakReference<PlayerController>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            PlayerController view = mView.get();
            if (view == null || view.playerControl == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = view.setProgress();
                    if (!view.dragging && view.showing && view.playerControl.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

}
