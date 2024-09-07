package com.project.KafkaPackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
public class GetDataFromMySQL {
	private final static String TOPIC = "MySQLStoreData";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";
		
	private static Connection	connection;
	private static ResultSet	rs	= null;
	private static Statement	statement	= null;
	
	
	private static Producer<String, String> createProducer()
	{
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "MySQLToKafka");
		
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		// props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
		// StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		
		return new KafkaProducer(props);
	}
	
	static void runProducer() throws Exception
	{
		
		final Producer<String, String> producer = createProducer();
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		connection = DriverManager.getConnection("jdbc:mysql://localhost/YouBuyy?user=root&password=Infy@123");
		
		statement = connection.createStatement();
		
		rs = statement.executeQuery("select * from YouBuyy.StoreData");
		
		while(rs.next())
		{
			final ProducerRecord<String, String> record = new ProducerRecord(TOPIC, rs.getString(1), rs.getString(2));
			RecordMetadata metadata = producer.send(record).get();
			
			
			System.out.println("Offset: " + metadata.offset() + "Partition: " + metadata.partition());
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		try
			{
			runProducer();
			}
		catch (InterruptedException e)
			{
				System.out.println("Exception occurred while running Consumer");
				e.printStackTrace();
			}
	}
	
}
