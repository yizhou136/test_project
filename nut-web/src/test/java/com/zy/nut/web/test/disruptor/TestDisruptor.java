package com.zy.nut.web.test.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Random;
import java.util.stream.IntStream;
/**
 * Created by zhougb on 2016/12/19.
 */
public class TestDisruptor {


    /**
     * Created by zhougb on 2016/12/19.
     */
    public static class My {

        private static class MyEvent{
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public static EventFactory<MyEvent> eventEventFactory = new EventFactory<MyEvent>() {
                @Override
                public MyEvent newInstance() {
                    return new MyEvent();
                }
            };
        }


        public static int bitCount(int i){
            int c = 0;
            while (i>0){
                i  &= (i-1);
                c++;
            }
            return c;
        }

        public static void main(String argv[]){
            int testI = 16;
            System.out.println(testI +" bitCount:"+bitCount(testI)+" "+Integer.bitCount(testI));

            Random random = new Random();
            final IntStream intStream = random.ints();

            Disruptor<MyEvent> disruptor = new Disruptor<MyEvent>(
                    ()->{return new MyEvent();},16, DaemonThreadFactory.INSTANCE);

            EventHandler<MyEvent>  eventHandler = (event, sequence, endOfBatch)->{
                int sleepInt = 0;//new Random().nextInt(4000);
                Thread.sleep(sleepInt);
                System.out.println("eventHandler sleepint:"+sleepInt+" event:"+event.getName()
                        +" sequence:"+sequence
                        +" endOfBatch:"+endOfBatch
                        +" currentThread:"+Thread.currentThread().getName());
            };

            EventHandler<MyEvent>  eventHandler2 = (event, sequence, endOfBatch)->{
                int sleepInt = 0;//new Random().nextInt(4000);
                //Thread.sleep(sleepInt);
                System.out.println("eventHandler2 sleepint:"+sleepInt+" event:"+event.getName()
                        +" sequence:"+sequence
                        +" endOfBatch:"+endOfBatch
                        +" currentThread:"+Thread.currentThread().getName());
            };

            WorkHandler<MyEvent> workHandler = event -> {
                int sleepInt = 0;//new Random().nextInt(4000);
                //Thread.sleep(sleepInt);
                System.out.println("workHandler sleepint:"+sleepInt+" event:"+event.getName()
                        +" currentThread:"+Thread.currentThread().getName());
            };
            //disruptor.after()
            disruptor.handleEventsWith(eventHandler,eventHandler);
            //disruptor.handleEventsWith(eventHandler);
            //disruptor.handleEventsWithWorkerPool(workHandler,workHandler,workHandler);

            disruptor.after(eventHandler).then(eventHandler2);


            disruptor.start();

            for (int i=0;i<10;i++)
                disruptor.publishEvent((event, sequence) -> {
                    event.setName("name:"+sequence);
                    System.out.println("publishEvent sequence:"+sequence);
                });

            //disruptor.shutdown();
            try {
                Thread.sleep(1000*40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
