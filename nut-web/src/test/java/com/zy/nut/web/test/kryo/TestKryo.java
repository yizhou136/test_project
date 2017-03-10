package com.zy.nut.web.test.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.stream.Stream;

/**
 * Created by zhougb on 2017/3/1.
 */
public class TestKryo {

    public static void testCompatibleFieldSerializer(){
        String methodName = getCurrentMethodName();
        String file = String.format("%s.kryo.bin", methodName);
        Kryo kryo = new Kryo();
        //kryo.setReferences(false);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        /*TaggedFieldSerializer serializer = new TaggedFieldSerializer(kryo,Person.class);
        serializer.setIgnoreUnknownTags(true);
        kryo.register(Person.class, serializer);*/
        //kryo.setDefaultSerializer(CompatibleFieldSerializer.class);*/

        if (true) {
            Output output = null;
            try {
                B b = new B(10, "abc", 10);
                output = new Output(new FileOutputStream(file));
                //kryo.writeObject(output, person);
                kryo.writeClassAndObject(output, b);
                //kryo.writeClassAndObject(output,n);
                //kryo.writeClassAndObject(output,b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }

        if (false) {
            Input input = null;
            try {
                input = new Input(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Person person2 = kryo.readObject(input, Person.class);
            B person2 = (B) kryo.readClassAndObject(input);
            System.out.println(person2.getC());
        }
    }

    public static void testTagedFieldSerialize(){
        String methodName = getCurrentMethodName();
        String file = String.format("%s.kryo.bin", methodName);
        Kryo kryo = new Kryo();
        //kryo.setReferences(false);
        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        /*TaggedFieldSerializer serializer = new TaggedFieldSerializer(kryo,Person.class);
        serializer.setIgnoreUnknownTags(true);
        kryo.register(Person.class, serializer);*/
        //kryo.setDefaultSerializer(CompatibleFieldSerializer.class);*/

        if (false) {
            Output output = null;
            try {
                B b = new B(10, "abc", 10);
                output = new Output(new FileOutputStream(file));
                //kryo.writeObject(output, person);
                kryo.writeClassAndObject(output, b);
                //kryo.writeClassAndObject(output,n);
                //kryo.writeClassAndObject(output,b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }

        if (true) {
            Input input = null;
            try {
                input = new Input(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Person person2 = kryo.readObject(input, Person.class);
            B person2 = (B) kryo.readClassAndObject(input);
            System.out.println(person2.getC());
        }
    }

    public static void testFieldSerialize(){
        String methodName = getCurrentMethodName();
        String file = String.format("%s.kryo.bin", methodName);
        Kryo kryo = new Kryo();
        //kryo.setReferences(false);
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        /*TaggedFieldSerializer serializer = new TaggedFieldSerializer(kryo,Person.class);
        serializer.setIgnoreUnknownTags(true);
        kryo.register(Person.class, serializer);*/
        //kryo.setDefaultSerializer(CompatibleFieldSerializer.class);*/

        if (false) {
            Output output = null;
            try {
                B b = new B(10, "abc", 10);
                N n = new N(10, "ab", 10);
                PersoN person = new PersoN(10, "abc");
                //person.setN(n);
                //person.setBb(b);
                //n.setPersoN(person);

                output = new Output(new FileOutputStream(file));
                //kryo.writeObject(output, person);
                kryo.writeClassAndObject(output, person);
                //kryo.writeClassAndObject(output,n);
                //kryo.writeClassAndObject(output,b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }

        Input input = null;
        try {
            input = new Input(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Person person2 = kryo.readObject(input, Person.class);
        PersoN person2 = (PersoN) kryo.readClassAndObject(input);
        System.out.println(person2.getC());

    }

    public static void testVersionFieldSerialize(){
        String methodName = getCurrentMethodName();
        String file = String.format("%s.kryo.bin", methodName);
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(VersionFieldSerializer.class);
        /*TaggedFieldSerializer serializer = new TaggedFieldSerializer(kryo,Person.class);
        serializer.setIgnoreUnknownTags(true);
        kryo.register(Person.class, serializer);*/
        //kryo.setDefaultSerializer(CompatibleFieldSerializer.class);*/

        Output output = null;
        try {
            output = new Output(new FileOutputStream(file));
            PersoN person = new PersoN(10,"zy");
            kryo.writeObject(output, person);
            //kryo.writeClassAndObject(output, person);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            output.close();
        }


        Input input = null;
        try {
            input = new Input(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Person person2 = kryo.readObject(input, Person.class);
        PersoN person2 = (PersoN) kryo.readObject(input, PersoN.class);
        System.out.println(person2.getC());


        input.close();
    }

    public static void main(String argv[]) throws FileNotFoundException {
        testCompatibleFieldSerializer();
        //testVersionFieldSerialize();
        //testFieldSerialize();
        //testTagedFieldSerialize();
        if (true)
            return;
        String file = "person.bin2";
        Kryo kryo = new Kryo();
        //kryo.setDefaultSerializer(TaggedFieldSerializer.class);

        /*TaggedFieldSerializer serializer = new TaggedFieldSerializer(kryo,Person.class);
        serializer.setIgnoreUnknownTags(true);
        kryo.register(Person.class, serializer);*/

        //kryo.setDefaultSerializer(CompatibleFieldSerializer.class);

        /*Output output = new Output(new FileOutputStream(file));
        Person person = new Person(10,"zy");
        //kryo.writeObject(output, person);
        kryo.writeClassAndObject(output, person);
        output.close();*/

        Input input = new Input(new FileInputStream(file));
        //Person person2 = kryo.readObject(input, Person.class);
        PersoN person2 = (PersoN) kryo.readClassAndObject(input);
        //System.out.println(person2.getName());


        input.close();
    }

    public static String getCurrentMethodName(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        //Stream.of(stackTraceElements).forEach(System.out::println);
        if (stackTraceElements.length >= 3)
        return stackTraceElements[2].getMethodName();
        return "null";
    }
}
