/*
    Memory-hog test app for Android. See src/Main.java for
    an overview. Native code is needed because Java code
    is strictly limited in how much memory it can allocate.

    This code tries to gracefully return an out-of-memory
    indication; unfortunately, instead of malloc returning
    a failure, the system simply kills the process.

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

#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>

static void *
    Grabbed = 0;
static long
    GrabSize = 0,
    GrabbedSoFar;

static jlong GetGrabbedSoFar
  (
    JNIEnv * env,
    jclass cthis
  )
  {
    return
        GrabbedSoFar;
  } /*GetGrabbedSoFar*/

static jboolean GrabIt
  (
    JNIEnv * env,
    jclass cthis,
    jlong HowMuch
  )
  {
    bool Success;
    free(Grabbed);
    GrabSize = HowMuch;
    Grabbed = malloc(GrabSize);
    Success = Grabbed != 0;
    if (Success)
      {
      /* ensure memory really is allocated */
        unsigned char * NextGrab = Grabbed;
        const unsigned char * const GrabEnd = (const unsigned char *)Grabbed + GrabSize;
        const unsigned long MaxGrabStep = 64 * 1024;
        GrabbedSoFar = 0;
        while (NextGrab != GrabEnd) /* do in pieces to provide progress update */
          {
            unsigned long ThisGrabStep = GrabEnd - NextGrab;
            if (ThisGrabStep > MaxGrabStep)
              {
                ThisGrabStep = MaxGrabStep;
              } /*if*/
            memset(NextGrab, -1, ThisGrabStep);
            NextGrab += ThisGrabStep;
            GrabbedSoFar += ThisGrabStep;
          } /*while*/
      } /*if*/
    return
        Success;
  } /*GrabIt*/

static void FreeIt
  (
    JNIEnv * env,
    jclass cthis
  )
  {
    free(Grabbed);
    Grabbed = 0;
    GrabSize = 0;
  } /*FreeIt*/

jint JNI_OnLoad
  (
    JavaVM * vm,
    void * reserved
  )
  {
    JNIEnv * env;
    int result = JNI_ERR;
    JNINativeMethod methods[] =
        {
            {
                .name = "GrabIt",
                .signature = "(J)Z",
                .fnPtr = GrabIt,
            },
            {
                .name = "GetGrabbedSoFar",
                .signature = "()J",
                .fnPtr = GetGrabbedSoFar,
            },
            {
                .name = "FreeIt",
                .signature = "()V",
                .fnPtr = FreeIt,
            },
        };
    do /*once*/
      {
        if ((**vm).GetEnv(vm, (void **)&env, JNI_VERSION_1_6) != JNI_OK)
            break;
        if
          (
                (**env).RegisterNatives
                  (
                    env,
                    (**env).FindClass(env, "nz/gen/geek_central/MemoryHog/Hogger"),
                    methods,
                    sizeof methods / sizeof(JNINativeMethod)
                  )
            !=
                0
          )
            break;
      /* all done */
        result = JNI_VERSION_1_6;
      }
    while (false);
    return
        result;
  } /*JNI_OnLoad*/
