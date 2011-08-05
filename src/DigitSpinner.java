package nz.gen.geek_central.MemoryHog;
/*
    Memory-hog test app for Android--custom spinner showing digits 0 .. 9.

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

public class DigitSpinner extends android.view.View
  {
    protected android.content.Context Context;
    protected int CurDigit;
    protected android.widget.TextView SampleView;

    protected void Init
      (
        android.content.Context Context
      )
      /* common code for all constructors */
      {
        this.Context = Context;
        SampleView = (android.widget.TextView)inflate(Context, R.layout.digit_item, null);
        CurDigit = 0;
      } /*Init*/

    public DigitSpinner
      (
        android.content.Context Context
      )
      {
        super(Context);
        Init(Context);
      } /*DigitSpinner*/

    public DigitSpinner
      (
        android.content.Context Context,
        android.util.AttributeSet Attributes
      )
      {
        this(Context, Attributes, 0);
      } /*DigitSpinner*/

    public DigitSpinner
      (
        android.content.Context Context,
        android.util.AttributeSet Attributes,
        int DefaultStyle
      )
      {
        super(Context, Attributes, DefaultStyle);
        Init(Context);
      } /*DigitSpinner*/

    @Override
    protected void onMeasure
      (
        int MeasureWidth,
        int MeasureHeight
      )
      {
        setMeasuredDimension(50, 50);
          /* nice to get these from resource */
      } /*onMeasure*/

    private static void DrawCenteredText
      (
        android.graphics.Canvas Draw,
        String TheText,
        float x,
        float y,
        android.graphics.Paint UsePaint
      )
      /* draws text at position x, vertically centred around y. */
      {
        final android.graphics.Rect TextBounds = new android.graphics.Rect();
        UsePaint.getTextBounds(TheText, 0, TheText.length(), TextBounds);
        Draw.drawText
          (
            TheText,
            x, /* depend on UsePaint to align horizontally */
            y - (TextBounds.bottom + TextBounds.top) / 2.0f,
            UsePaint
          );
      } /*DrawCenteredText*/

    @Override
    public void onDraw
      (
        android.graphics.Canvas Dest
      )
      {
        final android.graphics.drawable.Drawable Background = SampleView.getBackground();
        if (Background != null)
          {
            Background.draw(Dest);
          } /*if*/
        final android.graphics.Paint How = new android.graphics.Paint();
        How.setTextSize(SampleView.getTextSize());
        How.setColor(SampleView.getCurrentTextColor());
        DrawCenteredText
          (
            Dest,
            Integer.toString(CurDigit),
            getWidth() / 2.0f,
            getHeight() / 2.0f,
            How
          );
          /* better styling TBD */
      } /*onDraw*/

    @Override
    public boolean onTouchEvent
      (
        android.view.MotionEvent TheEvent
      )
      {
        switch (TheEvent.getAction())
          {
        case android.view.MotionEvent.ACTION_DOWN:
              {
                final android.widget.ArrayAdapter<String> Digits =
                    new android.widget.ArrayAdapter<String>(Context, R.layout.digit_item);
                for (int d = 0; d < 10; ++d)
                  {
                    Digits.add(Integer.toString(d));
                  } /*for*/
                final android.widget.ListView DigitsDisplay = new android.widget.ListView(Context);
                DigitsDisplay.setAdapter(Digits);
                DigitsDisplay.setChoiceMode(DigitsDisplay.CHOICE_MODE_SINGLE);
                DigitsDisplay.setSelection(CurDigit);
                DigitsDisplay.setBackgroundDrawable(SampleView.getBackground());
                final android.widget.PopupWindow ThePopup =
                    new android.widget.PopupWindow(DigitsDisplay);
                DigitsDisplay.setOnItemClickListener
                  (
                    new android.widget.AdapterView.OnItemClickListener()
                      {
                        public void onItemClick
                          (
                            android.widget.AdapterView<?> Parent,
                            android.view.View ItemView,
                            int ItemPosition,
                            long ItemId
                          )
                          {
                            SetDigit(ItemPosition);
                            ThePopup.dismiss();
                          } /*onItemClick*/
                      } /*OnItemClickListener*/
                  );
                ThePopup.setWidth(100); /* TBD temp */
                ThePopup.setHeight(300); /* TBD temp */
                ThePopup.setFocusable(true);
                ThePopup.setOnDismissListener
                  (
                    new android.widget.PopupWindow.OnDismissListener()
                      {
                        public void onDismiss()
                          {
                            SetDigit(DigitsDisplay.getCheckedItemPosition());
                          } /*onDismiss*/
                      } /*OnDismissListener*/
                  );
                ThePopup.showAsDropDown(DigitSpinner.this);
              }
        break;
      /* ACTION_CANCEL, ACTION_UP ignored */
          } /*switch*/
        return
            true;
      } /*onTouchEvent*/

    public int GetDigit()
      {
        return
            CurDigit;
      } /*GetDigit*/

    public void SetDigit
      (
        int NewDigit
      )
      {
        if (NewDigit != CurDigit && NewDigit >= 0 && NewDigit < 10)
          {
            CurDigit = NewDigit;
            invalidate();
          } /*if*/
      } /*SetDigit*/

  } /*DigitSpinner*/
