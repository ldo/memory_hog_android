package nz.gen.geek_central.MemoryHog;
/*
    Custom spinner showing digits 0 .. 9.

    Copyright 2011, 2013 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

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

import nz.gen.geek_central.android.useful.BundledSavedState;

public class DigitSpinner extends android.widget.RelativeLayout
  {
    protected android.content.Context Context;
    protected int CurDigit;
    protected android.widget.TextView RenderView;

    protected void Init
      (
        android.content.Context Context
      )
      /* common code for all constructors */
      {
        this.Context = Context;
        RenderView = (android.widget.TextView)inflate(Context, R.layout.digit_item, null);
        setGravity(CENTER_IN_PARENT);
        addView(RenderView);
        requestLayout();
        CurDigit = -1;
        SetDigit(0);
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
                DigitsDisplay.setBackgroundDrawable(RenderView.getBackground());
                final android.widget.PopupWindow ThePopup =
                    new android.widget.PopupWindow(DigitsDisplay);
                ThePopup.setBackgroundDrawable
                  (
                    new android.graphics.drawable.ColorDrawable(0xc0000000)
                  );
                  /* if I don't do this, popup cannot be dismissed with back key! */
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
                ThePopup.setWidth(RenderView.getWidth());
                ThePopup.setHeight(RenderView.getHeight() * 5); /* quick hack to ensure it's not taller than screen */
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
            RenderView.setText(Integer.toString(CurDigit));
          } /*if*/
      } /*SetDigit*/

  /*
    Implementation of saving/restoring instance state. Doing this
    allows me to transparently restore widget setting if system
    needs to kill me while I'm in the background, or on an orientation
    change while I'm in the foreground.
  */

    @Override
    public android.os.Parcelable onSaveInstanceState()
      {
        final android.os.Bundle MyState = new android.os.Bundle();
        MyState.putInt("CurDigit", CurDigit);
        return
            new BundledSavedState(super.onSaveInstanceState(), MyState);
      } /*onSaveInstanceState*/

    @Override
    public void onRestoreInstanceState
      (
        android.os.Parcelable SavedState
      )
      {
        super.onRestoreInstanceState(((BundledSavedState)SavedState).SuperState);
        final android.os.Bundle MyState = ((BundledSavedState)SavedState).MyState;
        SetDigit(MyState.getInt("CurDigit", 0));
      } /*onRestoreInstanceState*/

  } /*DigitSpinner*/
