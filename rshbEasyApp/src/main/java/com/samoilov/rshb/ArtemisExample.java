package com.samoilov.rshb;

import jakarta.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ArtemisExample {
    public static void main(String[] args) throws JMSException {

        // Set up connection to Artemis broker
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616?protocols=AMQP");
        Connection connection = factory.createConnection("admin", "test");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Put a message into queue q1
        Queue queue1 = session.createQueue("q1");
        MessageProducer producer = session.createProducer(queue1);
        BytesMessage message1 = session.createBytesMessage();
        message1.writeInt(42);
        message1.writeUTF("Hello, Artemis!");
        producer.send(message1);
        System.out.println("Sent message to q1: " + message1.getBodyLength() + " bytes");

        // Receive a message from queue q2
        Queue queue2 = session.createQueue("q2");
        MessageConsumer consumer = session.createConsumer(queue2);
        connection.start();
        Message message2 = consumer.receive();

        if (message2 instanceof BytesMessage) {

            BytesMessage bytesMessage = (BytesMessage) message2;
            byte[] data = new byte[(int) bytesMessage.getBodyLength()];
            bytesMessage.readBytes(data);
            System.out.println("Received message from q2: " + new String(data));

        } else {

            System.out.println("Received message from q2: " + message2);

        }

        // Clean up resources
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }
}
