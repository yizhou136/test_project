package com.zy.nut.web.test.java;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhougb on 2016/12/8.
 */
public class TestFiles {

    public static void printSubDir(String path){
        //Files.fil
        File file = new File(path);
        /*File files[] = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });*/
        File files[] = file.listFiles((f)->{
            return f.isFile();
        });
        System.out.println(Arrays.toString(files));

        String fileNames[] = file.list((f,n)->{
            if (f.exists()){return n.endsWith(".txt");}return false;});
        System.out.println(Arrays.toString(fileNames));


        Arrays.sort(files, (File f, File s)->{return f.getName().compareTo(s.getName());});

        System.out.println(Arrays.toString(files));
    }

    public static void summaryStaticses(){
        try(Stream<String> lineStream = Files.lines(Paths.get("E:\\workspace\\web\\test_project\\pom.xml"))){
            Stream<String> wordsStream = lineStream.flatMap(s -> {return Stream.of(s.split(" "));});
            //System.out.print(wordsStream.count());
            /*IntSummaryStatistics intSummaryStatistics = wordsStream.collect(Collectors.summarizingInt(ss->1));
            System.out.print(intSummaryStatistics);*/
            Map<String,Integer> map = wordsStream.collect(
                    Collectors.toMap((t) ->{return t;},
                            (v)->{return 1;}, (nv,ov)->{return ov+1;}));

            System.out.print(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]){
        //printSubDir("E:\\workspace\\web\\test_project");
        summaryStaticses();
    }
}
