package com.zy.nut.web.test.avro;

import com.zy.nut.web.avro.User;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/1/30.
 */
public class TestAvro {

    public static void serializeGeneric(){
        try {
            File file = new File("D:\\work\\test_project\\nut-web\\src\\main\\avro\\com\\zy\\nut\\avro\\user.avsc");
            /*InputStream inputStream = TestAvro.class.getResourceAsStream("user.avsc");
            Enumeration<URL> enumeration = TestAvro.class.getClassLoader().getResources("user.avsc");

            URL url = enumeration.nextElement();
            while (enumeration.hasMoreElements()){
                url = enumeration.nextElement();
                System.out.println(url);
            }
            System.out.println(url);*/

            Schema schema = new Schema.Parser().parse(file);
            GenericRecord genericRecord1 = new GenericData.Record(schema);
            genericRecord1.put("name","zy2");
            genericRecord1.put("favorite_number",1);
            genericRecord1.put("favorite_color","blue");


            GenericRecord genericRecord2 = new GenericData.Record(schema);
            genericRecord2.put("name","zy2");
            genericRecord2.put("favorite_number",2);
            genericRecord2.put("favorite_color","red");

            DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
            DataFileWriter dataFileWriter = new DataFileWriter(datumWriter);
            dataFileWriter.create(schema, new File("users_generic.avsc"));
            dataFileWriter.append(genericRecord1);
            dataFileWriter.append(genericRecord2);
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserializeGeneric(){
        File file = new File("D:\\work\\test_project\\nut-web\\src\\main\\avro\\com\\zy\\nut\\avro\\user2.avsc");
        try {
            Schema  schema = new Schema.Parser().parse(file);
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File("users.avsc"), datumReader);
            while (dataFileReader.hasNext()){
                GenericRecord genericRecord = dataFileReader.next();
                System.out.println(genericRecord.get("name")+" "
                        +genericRecord.get("fn")+" "
                        +genericRecord.get("favorite_color") + " "
                        //+genericRecord.get("intersts") + " "
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize(){
        User user = User.newBuilder().setName("zy").setFavoriteColor("red").setFavoriteNumber(82).build();
        DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter(datumWriter);
        try {
            dataFileWriter.create(user.getSchema(), new File("users.avsc"));
            dataFileWriter.append(user);
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize(){
        DatumReader<User> datumReader = new SpecificDatumReader<>(User.class);
        try {
            DataFileReader<User> dataFileReader = new DataFileReader<User>(new File("users.avsc"), datumReader);
            User user = null;
            while (dataFileReader.hasNext()){
                user = (User)dataFileReader.next();
                System.out.println("user:"+user);
            }
            dataFileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]){
        //serialize();
        //deserialize();

        //serializeGeneric();
        deserializeGeneric();
    }
}
