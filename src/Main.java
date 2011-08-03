package nz.gen.geek_central.MemoryHog;
/*
    Memory-hog test app for Android. The purpose of this is to
    grab enough memory to force paused activities in the back
    stack to be killed after their onSaveInstanceState methods
    are called. This way you can make sure they correctly restore
    their UI state as they are restarted when the Back key is pressed.

    Unfortunately, this app does not work gracefully; instead
    of reporting that it has run out of memory, the system kills it.
    But that's OK, you were going to return to the previous activity
    anyway...

    Written by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.
*/

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
