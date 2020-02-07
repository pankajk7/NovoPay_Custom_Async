package com.pankaj.nptest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CustomAsyncTask<T extends Serializable> {


//    private MyThread thread;
    private otherThread otherThread;
    private CustomAsyncListener listener;
    private Handler uiHandler = new Handler();
    T obj;

    CustomAsyncTask(T obj, CustomAsyncListener listener) {
//        thread = new MyThread();
//        thread.start();
        otherThread = new otherThread("otherThread");
        otherThread.start();
        this.obj = obj;
        this.listener = listener;
    }

    public void execute() {
        listener.onPreExecute();
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", obj);
//        Message msg = thread.handler.obtainMessage();
//        msg.setData(bundle);
//        thread.handler.sendMessage(msg);


        Message msg = otherThread.handler.obtainMessage();
        msg.setData(bundle);
        otherThread.handler.sendMessage(msg);

        uiHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                listener.onPostExecute(msg.obj);
                otherThread.getLooper().quitSafely();
            }
        };
    }

    interface CustomAsyncListener<T> {
        void onPreExecute();
        T doInBackground(T obj);
        void onPostExecute(T obj);
    }

//    class MyThread<T extends Serializable> extends Thread {
//
//        Handler handler = new Handler();
//
//        @Override
//        public void run() {
//            Looper.prepare();
//            handler = new Handler() {
//                @Override
//                public void handleMessage(@NonNull Message msg) {
//                    Bundle bundle = msg.getData();
//                    T val1 = (T) bundle.getSerializable("result");
//                    T value = (T) listener.doInBackground(val1);
//                    Message outMsg = new Message();
//                    outMsg.obj = value;
//                    uiHandler.sendMessage(outMsg);
//                }
//            };
//            Looper.loop();
//        }
//    }

    class otherThread extends HandlerThread {

        Handler handler = new Handler();

        public otherThread(String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            initHandler();
        }

        void initHandler() {
            handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Bundle bundle = msg.getData();
                    T val1 = (T) bundle.getSerializable("result");
                    T value = (T) listener.doInBackground(val1);
                    Message outMsg = new Message();
                    outMsg.obj = value;
                    uiHandler.sendMessage(outMsg);
                }
            };
        }

    }
}
