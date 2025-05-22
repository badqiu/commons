package com.github.rapid.common.demo;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * mqtt server: 
 * EMQX : 支持百万级连接、集群部署，提供Web管理界面
 * HiveMQ : 企业级安全、高可用集群，支持MQTT 5.0和插件扩展
 * Eclipse Mosquitto: 轻量级、易部署，支持MQTT 3.1/5.0
 * 
 * 
 * test server:
 * tcp://broker.hivemq.com:1883
 * tcp://mqtt.eclipseprojects.io:1883
 */
public class MqttDemo {
    private static final String ECLIPSE_BROKER_SERVER_URI = "tcp://mqtt.eclipseprojects.io:1883"; // 公共测试服务器
    private static final String HIVEMQ_BROKER_SERVER_URI = "tcp://broker.hivemq.com:1883"; // 公共测试服务器
    private static final String MOSQUITTO_BROKER_SERVER_URI = "tcp://test.mosquitto.org:1883"; // 公共测试服务器
    
    
    private static final String CLIENT_ID = "JavaMqttClient";
    private static final String TOPIC = "demo/topic";
    
    static int QOS_0_FAST_AND_LOST_DATA = 0;
    static int QOS_1_SAFE = 1;
    static int QOS_2_VERY_SAFE = 2;

    public static void main(String[] args) throws Exception {
        testWithBrokerAddress(ECLIPSE_BROKER_SERVER_URI);
        testWithBrokerAddress(HIVEMQ_BROKER_SERVER_URI);
        testWithBrokerAddress(MOSQUITTO_BROKER_SERVER_URI);
    }

	private static void testWithBrokerAddress(String serverURI)
			throws MqttException, MqttSecurityException, MqttPersistenceException, InterruptedException {
		System.out.println("\n\n--------------------------------------------------------------------------");
		// 1. 创建MQTT客户端
        MqttClient client = new MqttClient(serverURI, CLIENT_ID, new MemoryPersistence());
        
        // 2. 设置连接选项
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true); // 清除旧会话
        options.setConnectionTimeout(10); // 超时时间10秒
        
        //遗嘱消息​​：设备异常离线时发送通知：
        options.setWill("device/status", "offline".getBytes(), 1, true);

        // 3. 连接服务器
        System.out.println("Connecting to broker: " + serverURI);
        client.connect(options);
        System.out.println("Connected!");

        if (!client.isConnected()) {
            throw new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED);
        }
        
        // 4. 订阅主题（QoS=1）
        client.subscribe(TOPIC, 1);
        System.out.println("Subscribed to topic: " + TOPIC);

        // 5. 设置消息回调
        client.setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
				System.out.println("MqttCallback.messageArrived() Received: Topic=" + topic + ", Payload=" + payload+" messageId:"+message.getId()+" isRetained:"+message.isRetained());
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause.getMessage());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        for(int i = 0; i < 10; i++) {
	        // 6. 发布消息（QoS=1）
	        MqttMessage message = new MqttMessage( (i + " Hello MQTT!").getBytes());
	        
	        /*
	         * 
				​​QoS 0​​（最多一次）：快速传输但可能丢失，适用于传感器数据采集；
				​​QoS 1​​（至少一次）：通过ACK确认保证必达，适合设备控制指令；
				​​QoS 2​​（精确一次）：四次握手确保唯一性，用于支付等高可靠性场景         
	         */
	        message.setQos(QOS_2_VERY_SAFE);
	        client.publish(TOPIC, message);
	        System.out.println("Message published!");
        }
        

        // 7. 保持程序运行以接收消息
        Thread.sleep(5000);
        client.disconnect();
	}
}