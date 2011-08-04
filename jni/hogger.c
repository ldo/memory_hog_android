/*
    Memory-hog test app for Android. See src/Main.java for
    an overview. Native code is needed because Java code
    is strictly limited in how much memory it can allocate.

    This code tries to gracefully return an out-of-memory
    indication; unfortunately, instead of malloc returning
    a failure, the system simply kills the process.

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

#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <jni.h>

static void **
    Grabbed = 0;
static int
    GrabCount = 0,
    GrabMax = 0;

static jint GrabMore
  (
    JNIEnv * env,
    jobject this
  )
  {
    int result = -1;
    enum
      {
        GrabIncr = 1048576,
      };
    fprintf(stderr, "Hogger: GrabMore\n");
    do /*once*/
      {
        if (GrabCount == GrabMax)
          {
            const int NewGrabMax = GrabMax == 0 ? 128 : 2 * GrabMax;
            Grabbed = realloc(Grabbed, NewGrabMax * sizeof(void *));
            if (Grabbed == 0)
                break;
            GrabMax = NewGrabMax;
          } /*if*/
          {
            int i;
            void * const NewGrab = malloc(GrabIncr);
            if (NewGrab == 0)
                break;
            for (i = 0; i < GrabIncr; ++i)
              {
              /* ensure memory is touched */
                ((unsigned char *)NewGrab)[i] = -1;
              } /*for*/
            Grabbed[GrabCount++] = NewGrab;
          }
        result = GrabCount;
      }
    while (false);
    return
        result;
  } /*GrabMore*/

static void FreeAll
  (
    JNIEnv * env,
    jobject this
  )
  {
    fprintf(stderr, "Hogger: FreeAll\n");
    if (Grabbed != 0)
      {
        int i;
        for (i = 0; i < GrabCount; ++i)
          {
            free(Grabbed[i]);
          } /*for*/
        free(Grabbed);
        Grabbed = 0;
      } /*if*/
    GrabCount = 0;
    GrabMax = 0;
  } /*FreeAll*/

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
                .name = "GrabMore",
                .signature = "()I",
                .fnPtr = GrabMore,
            },
            {
                .name = "FreeAll",
                .signature = "()V",
                .fnPtr = FreeAll,
            },
        };
    fprintf(stderr, "Hogger: JNI_Onload\n");
    do /*once*/
      {
        if ((**vm).GetEnv(vm, (void **)&env, JNI_VERSION_1_6) != JNI_OK)
          {
            fprintf(stderr, "Hogger: JNI_Onload: could not request JNI version %d\n", JNI_VERSION_1_6);
            break;
          } /*if*/
        if
          (
                (**env).RegisterNatives
                  (
                    env,
                    (**env).FindClass(env, "nz/gen/geek_central/MemoryHog/Main"),
                    methods,
                    sizeof methods / sizeof(JNINativeMethod)
                  )
            !=
                0
          )
          {
            fprintf(stderr, "Hogger: JNI_Onload: failed to RegisterNatives\n");
            break;
          } /*if*/
      /* all done */
        result = JNI_VERSION_1_6;
      }
    while (false);
    return
        result;
  } /*JNI_OnLoad*/
