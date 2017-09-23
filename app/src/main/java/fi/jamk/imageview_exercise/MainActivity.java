package fi.jamk.imageview_exercise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // ImageView object
    private ImageView imageView;

    // TextView Object
    private TextView textView;

    // Progress Bar Object
    private ProgressBar progressBar;

    // example image names
    private String[] images = {
            "http://cdn3-www.dogtime.com/assets/uploads/gallery/pembroke-welsh-corgi-dog-breed-pictures/prance-8.jpg",
            "https://i.pinimg.com/736x/bf/b6/19/bfb6192a36bb33728209ac1040f2f574--pembroke-welsh-corgi-dog-training.jpg",
            "https://www.rover.com/blog/wp-content/uploads/2017/06/corgi-flowers-960x540.jpg"};

    // Which image is currently visible
    private int imageIndex;

    // async task to load a new image
    private DownloadImageTask task;

    // Swipe down and up values
    private float x1, x2;

    private VelocityTracker mVelocityTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views from UI
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Begin showing images
        imageIndex = 0;

        showImage();
    }


    public void showImage(){
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        task.execute(images[imageIndex]);
    }

    // This is the async task class
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        // This is done in UI thread
        @Override
        protected void onPreExecute()
        {
            // show progress bar to indicate loading
            progressBar.setVisibility(View.VISIBLE);
        }

        // This is the background thread, load an image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls){
            URL imageUrl;
            Bitmap bitmap = null;

            try{
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e){
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }

            return bitmap;
        }

        // This receives the bitmap and shows it in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if(x1 < x2) {  // left to right which is previous
                        imageIndex--;
                        if(imageIndex < 0) imageIndex = images.length-1;
                    }
                    else {
                    imageIndex++;
                    if( imageIndex > (images.length-1)) imageIndex = 0;
                }
                showImage();
                break;
        }
        return false;
    }
}
