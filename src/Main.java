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

    Then you discover that everything prior to that has also been
    killed, including the launcher. Bit of a sledgehammer, eh wot...

    Copyright 2011 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you
    may not use this file except in compliance with the License. You may
    obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
    implied. See the License for the specific language governing
    permissions and limitations under the License.
*/

public class Main extends android.app.Activity
  {

    protected android.os.Handler Runner;
    protected android.widget.TextView Message;
    protected android.widget.ArrayAdapter<String> Digits;
    protected DigitSpinner Hundreds, Tens, Units;

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
        setContentView(R.layout.main);
        Message = (android.widget.TextView)findViewById(R.id.message);
        Hundreds = (DigitSpinner)findViewById(R.id.hundreds);
        Tens = (DigitSpinner)findViewById(R.id.tens);
        Units = (DigitSpinner)findViewById(R.id.units);
        ((android.widget.Button)findViewById(R.id.doit)).setOnClickListener
          (
            new android.view.View.OnClickListener()
              {
                public void onClick
                  (
                    android.view.View TheView
                  )
                  {
                    System.err.printf
                      (
                        "MemoryHog.Main: doit clicked, selected digits are %d, %d, %d\n",
                        Hundreds.GetDigit(),
                        Tens.GetDigit(),
                        Units.GetDigit()
                      );
                  /* TBD */
                  } /*onClick*/
              } /*OnClickListener*/
          );
        Message.setText("Choose Size");
        Runner = new android.os.Handler();
      } /*onCreate*/

    @Override
    public void onResume()
      {
        super.onResume();
      /* Runner.post(new Grabber()); */
      /* TBD */
      } /*onResume*/

    @Override
    public void onPause()
      {
      /* TBD */
        FreeAll();
        super.onPause();
      } /*onPause*/

  } /*Main*/
