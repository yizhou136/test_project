package com.zy.nut.web.test.java;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by zhougb on 2017/2/3.
 */
public class TestReference {



    public static void testWeakReference() throws InterruptedException {
        Person person = new Person(1,"zy1");
        ReferenceQueue<Person> referenceQueue = new ReferenceQueue<>();
        WeakReference<Person> weakReference = new WeakReference<Person>(person,referenceQueue);
        //SoftReference<Person> softReference = new SoftReference<Person>(person, referenceQueue);
        System.out.println("before gc:"+weakReference.get()
                +" referenceQueue.poll:"+ referenceQueue.poll()
                +" Reference:"+weakReference
                );
        person = null;
        System.gc();
        Thread.sleep(1000);
        System.out.println("after gc:"+weakReference.get()
                +" referenceQueue.poll:"+ referenceQueue.poll()
                +" Reference:"+weakReference
        );
    }

    public static void main(String args[]) throws InterruptedException {
        testWeakReference();
    }
}
