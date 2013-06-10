package nz.gen.geek_central.MemoryHog;
/*
    Memory-hog test app for Android: container class for native methods.
    Putting these here allows the Main activity to detect and recover from
    an exception when trying to run on an architecture for which I do not
    have the right native code.

    Copyright 2013 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

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

public class Hogger
  {
    public static native boolean GrabIt
      (
        long HowMuch
      );
    public static native long GetGrabbedSoFar();
    public static native void FreeIt();

    static
      {
        System.loadLibrary("hogger");
      } /*static*/

  } /*Hogger*/;
