package nz.gen.geek_central.MemoryHog;

public class Main extends android.app.Activity
  {

    android.os.Handler Runner;
    android.widget.TextView Message;
    byte[][] Grabbed;
    int GrabCount;

    private class Grabber implements Runnable
      {
        public void run()
          {
            try
              {
                if (GrabCount == Grabbed.length)
                  {
                    final byte[][] NewGrabbed = new byte[Grabbed.length * 2][];
                    for (int i = 0; i < GrabCount; ++i)
                      {
                        NewGrabbed[i] = Grabbed[i];
                      } /*for*/
                    Grabbed = NewGrabbed;
                  } /*if*/
                Grabbed[GrabCount++] = new byte[1048576];
                Message.setText(String.format("Grabbed %dMiB", GrabCount));
                Runner.post(new Grabber());
              }
            catch (OutOfMemoryError RanOut)
              {
                Message.setText(Message.getText() + " That’s all, folks.");
              } /*catch*/
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
        Grabbed = null;
      } /*onCreate*/

    @Override
    public void onResume()
      {
        super.onResume();
        Grabbed = new byte[128][];
        GrabCount = 0;
        Runner.post(new Grabber());
      } /*onResume*/

    @Override
    public void onPause()
      {
        Grabbed = null;
        super.onPause();
      } /*onPause*/

  } /*Main*/
