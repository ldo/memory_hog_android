LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := hogger

LOCAL_LDLIBS := -L${SYSROOT}/usr/lib -llog

LOCAL_SRC_FILES := hogger.c

include $(BUILD_SHARED_LIBRARY)
