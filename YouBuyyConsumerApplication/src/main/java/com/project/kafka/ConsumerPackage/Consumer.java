package com.project.kafka.ConsumerPackage;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.Iterator;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
public class Consumer {
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private static KafkaConsumer<String, String> createConsumer()
        {
            final Properties consumerProps = new Properties();
            
            //Consumer Configuration properties
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            
            // Create the consumer using props.
            KafkaConsumer<String, String> ClickStreamConsumer = new KafkaConsumer<String, String>(consumerProps);  
            
                        
            // Subscribe to the specified topic.                
           ClickStreamConsumer.subscribe(Arrays.asList("CleansedYouBuyyClickStreamData"));
                    
            return ClickStreamConsumer;
        }
    //Method to poll Kafka topic and stream the messages.
    static void runConsumer() throws InterruptedException {
        
        KafkaConsumer<String, String> streamConsumer = createConsumer();
                
            
            final int NoOfTries = 5;   int RecordsCount = 0;
            //Initiate the consumer
            streamConsumer.poll(0);
            
            //Start consuming messages from beginning. Set offset to first message.
            streamConsumer.seekToBeginning(streamConsumer.assignment());
            
            //poll and consume messages. Polling happens 5 times.
            while (true) {
                final ConsumerRecords<String, String> consumerRecords =
                        streamConsumer.poll(30000);
                //Try for 5 times, If no messages then exit.
                if (consumerRecords.count()==0) {
                    RecordsCount++;
                            if (RecordsCount > NoOfTries)
                                break;
                            else
                                continue;
                }
           
                ConsumerRecord <String,String> CSRecord = null; 
                
                //loop through the messages, print Key, Value, Partition number and offset of each message.
                for ( Iterator <ConsumerRecord<String,String>> record = consumerRecords.iterator(); record.hasNext();)
                    {
                    CSRecord = record.next();
                                        
                        
                        System.out.printf("Consumer Record: Key - " + CSRecord.key() + " Value - " + CSRecord.value()
                         + " Partition - " + CSRecord.partition() + " Offset - " + CSRecord.offset() + "\n");
                        
                        
                        
                    }
                CSRecord = null; // garbage collection assist. 
                
                streamConsumer.commitAsync();
            }
            
            //close the consumer once the consumption is over.
            streamConsumer.close();
            System.out.println("Consuming records is completed");
        }
    public static void main(String[] args)
        {
            try
                {
                    runConsumer();
                }
            catch (InterruptedException e)
                {
                    System.out.println("Exception occurred while running Consumer");
                    e.printStackTrace();
                }
        }
}
