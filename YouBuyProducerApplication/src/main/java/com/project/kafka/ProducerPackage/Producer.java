package com.project.kafka.ProducerPackage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
public class Producer {
//List of brokers in Kafka cluster
private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    
    //Method to create producer object
    private static KafkaProducer<String, String> createProducer()
    {
        //Create Configuration properties object.
        Properties propsClickStream = new Properties();
        
        //Kafka broker topic details.
        propsClickStream.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        
        propsClickStream.put(ProducerConfig.CLIENT_ID_CONFIG, "ClickStreamProducer");
        
        //Serialization class details.
        propsClickStream.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());                
        propsClickStream.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
                
        return new KafkaProducer<String, String>(propsClickStream);
    }
    
    public static void main(String[] args) throws InterruptedException, FileNotFoundException
    {
    
        final KafkaProducer<String, String> clickStreamProducer = createProducer();
        
        //Read the messages from a file
        Scanner streamInput = new Scanner(new File(args[0]));
        
        //Read the records from a file and publish to Kafka topic.
        while (streamInput.hasNextLine()) {
            
            String Record = streamInput.nextLine();
        
            String[] columns = Record.split(Pattern.quote(","));
            
            //message written to kafka partition will be typically in <key, Value> structure.
            //args[1] - Topic name.
            //columns[0] - first field <CustomerID> is used as Key.
            //Record - complete record as value.
            final ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                    args[1], columns[0], Record);
            
            clickStreamProducer.send(record);
                        
    }
        System.out.println("Completed producing records");
        }
    }
