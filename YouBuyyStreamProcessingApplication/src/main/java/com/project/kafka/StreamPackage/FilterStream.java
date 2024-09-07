package com.project.kafka.StreamPackage;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueMapper;
//import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
public class FilterStream {
     public static void main(final String[] args) throws Exception {
         
            //Create configuration object with all parameters set.
            Properties clickStreamConfig = new Properties();
            clickStreamConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "ClickStreamAnalysis");
            clickStreamConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            clickStreamConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            clickStreamConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            
            //Create a stream builder
            StreamsBuilder clickStreamBuilder = new StreamsBuilder();
            
            //Stream the messages from the specified Topic
            KStream<String, String> StreamData = clickStreamBuilder.stream(args[0]);
            
            //Perform filter operation on SessionTime parameter
            KStream<String, String> FilteredRecords = StreamData.filter(
                    
                    new Predicate<String, String>() {
                        
                        //Filter the records where SessionTime is less than 30 seconds
                        public boolean test(String Key, String Value) {
                            
                            String[] SplitValues = Value.split(",");
                            
                            int SessionTime = Integer.parseInt(SplitValues[4]);
                            
                            return SessionTime > 30;
                        }
                        }
                    
                    );
            
            //Write filtered records to a new topic
            FilteredRecords.to(args[1]);
                       
            System.out.println("Cleansing the records is complete");
            
            //Create KafkaStreams object and start the process
            KafkaStreams streams = new KafkaStreams(clickStreamBuilder.build(),clickStreamConfig);
            
            streams.start();
        }
    }
