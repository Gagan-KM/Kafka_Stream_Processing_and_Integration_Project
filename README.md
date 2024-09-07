# **Kafka Stream Processing and Integration**

## **Overview**
This project, developed in **Java** using **Eclipse**, demonstrates a Kafka-based data pipeline for real-time data processing. It involves data ingestion, streaming, and consumption using Apache Kafka.

## **Key Components**

### **1. Producer API**
- Reads data from CSV/MySQL and publishes to a Kafka topic.

### **2. Stream API**
- Filters Kafka topic messages based on session time (>30 seconds) and writes filtered data to another topic.

### **3. Consumer API**
- Consumes and logs messages from Kafka and sends them to MySQL.

### **4. MySQL-Kafka Integration**
- Transfers MySQL data into Kafka for real-time processing.
