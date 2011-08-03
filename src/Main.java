package nz.gen.geek_central.MemoryHog;

import android.graphics.Bitmap;

public class Main extends android.app.Activity
  {

    android.os.Handler Runner;
    android.widget.TextView Message;

    protected native int GrabMore();
    protected native void FreeAll();

    static
      {
        System.loadLibrary("hogger");
      } /*static*/

    private class Grabber implements Runnable
      {
        public void run()
          {
            final int GrabCount = GrabMore();
            if (GrabCount > 0)
              {
                Message.setText(String.format("Grabbed %dMiB", GrabCount));
                Runner.post(new Grabber());
              }
            else
              {
                Message.setText(Message.getText() + " That’s all, folks.");
              } /*if*/
          } /*run*/
      } /*Grabber*/

    @Override
    public void onCreate
      (
        android.os.Bundle savedInstanceState
      )
      {
        super.onCreate(savedInstanceState);
        Message = new android.widget.TextView(this);
        Message.setText("Starting...");
        setContentView(Message);
        Runner = new android.os.Handler();
      } /*onCreate*/

    @Override
    public void onResume()
      {
        super.onResume();
        Runner.post(new Grabber());
      } /*onResume*/

    @Override
    public void onPause()
      {
        FreeAll();
        super.onPause();
      } /*onPause*/

  } /*Main*/
