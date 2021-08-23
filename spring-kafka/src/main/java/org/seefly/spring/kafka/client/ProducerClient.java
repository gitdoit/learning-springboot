package org.seefly.spring.kafka.client;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 手动来一个kafka的客户端
 * @author liujianxin
 * @date 2021/8/21 10:19
 */
public class ProducerClient {

    public static void main(String[] args) {
        String str = "0000000000000000000劬";
        System.out.println(str.getBytes(StandardCharsets.UTF_8).length);
    }

    public static void mai22n(String[] args) {
        Map<String, Object> props = new HashMap<>();
        // 集群地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.5.4.147:9092,10.5.4.148:9092,10.5.4.149:9092");

        // 客户端id
        // 发出请求时传递给服务器的 ID 字符串。这样做的目的是为了在服务端的请求日志中能够通过逻辑应用名称来跟踪请求的来源，而不是只能通过IP和端口号跟进。
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "TEMP-KAFKA-PRODUCE");

        // key序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // value序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // acks=0 如果设置为0，则 producer 不会等待服务器的反馈。该消息会被立刻添加到 socket buffer 中并认为已经发送完成。在这种情况下，服务器是否收到请求是没法保证的，并且参数retries也不会生效（因为客户端无法获得失败信息）。每个记录返回的 offset 总是被设置为-1。
        // acks=1 如果设置为1，leader节点会将记录写入本地日志，并且在所有 follower 节点反馈之前就先确认成功。在这种情况下，如果 leader 节点在接收记录之后，并且在 follower 节点复制数据完成之前产生错误，则这条记录会丢失。
        // acks=all 如果设置为all，这就意味着 leader 节点会等待所有同步中的副本确认之后再确认这条记录是否发送完成。只要至少有一个同步副本存在，记录就不会丢失。这种方式是对请求传递的最有效保证。acks=-1与acks=all是等效的。
        props.put(ProducerConfig.ACKS_CONFIG, "1");

        // Producer 用来缓冲等待被发送到服务器的记录的总字节数。如果记录发送的速度比发送到服务器的速度快， Producer 就会阻塞，如果阻塞的时间超过 max.block.ms 配置的时长，则会抛出一个异常。
        // 这个配置与 Producer 的可用总内存有一定的对应关系，但并不是完全等价的关系，因为 Producer 的可用内存并不是全部都用来缓存。一些额外的内存可能会用于压缩(如果启用了压缩)，以及维护正在运行的请求。
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        // Producer 生成数据时可使用的压缩类型。默认值是none(即不压缩)。可配置的压缩类型包括：none, gzip, snappy, 或者 lz4 。
        // 压缩是针对批处理的所有数据，所以批处理的效果也会影响压缩比(更多的批处理意味着更好的压缩)。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");

        // 若设置大于0的值，则客户端会将发送失败的记录重新发送，尽管这些记录有可能是暂时性的错误。请注意，这种 retry 与客户端收到错误信息之后重新发送记录并无区别。
        // 允许 retries 并且没有设置max.in.flight.requests.per.connection 为1时，记录的顺序可能会被改变。比如：当两个批次都被发送到同一个 partition ，
        // 第一个批次发生错误并发生 retries 而第二个批次已经成功，则第二个批次的记录就会先于第一个批次出现。
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        // 当将多个记录被发送到同一个分区时， Producer 将尝试将记录组合到更少的请求中。这有助于提升客户端和服务器端的性能。这个配置控制一个批次的默认大小（以字节为单位）。
        // 当记录的大小超过了配置的字节数， Producer 将不再尝试往批次增加记录。
        // 发送到 broker 的请求会包含多个批次的数据，每个批次对应一个 partition 的可用数据
        // 小的 batch.size 将减少批处理，并且可能会降低吞吐量(如果 batch.size = 0的话将完全禁用批处理)。
        // 很大的 batch.size 可能造成内存浪费，因为我们一般会在 batch.size 的基础上分配一部分缓存以应付额外的记录。
        // [直观一点] 16384个byte的缓冲池大概能放七八千个中文
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16 * 1024);

        // 配合批次大小那个配置，如果长时间都没有到达指定的批次大小那岂不是一直都不会发送数据？
        // 为了解决这种情况，这里设定最大延迟，如果在超过了LINGER_MS 还没满足BATCH_SIZE，那也会立即发送
        // 默认为0，表示BATCH_SIZE没用了，生产一个消息就立即发送
        props.put(ProducerConfig.LINGER_MS_CONFIG,0);

        // 在此配置指定的毫秒数之后，关闭空闲连接。
        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG,54 * 1000);

        // 该配置控制 KafkaProducer.send()和KafkaProducer.partitionsFor() 允许被阻塞的时长。这些方法可能因为缓冲区满了或者元数据不可用而被阻塞。
        // 用户提供的序列化程序或分区程序的阻塞将不会被计算到这个超时。
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,60 * 1000);

        // 请求的最大字节数。这个设置将限制 Producer 在单个请求中发送的记录批量的数量，以避免发送巨大的请求。
        // 这实际上也等同于批次的最大记录数的限制。请注意，服务器对批次的大小有自己的限制，这可能与此不同。
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,1024 * 1024);

        // 分区器
        // 算法：指定了分区用指定的，没指定有key，把key散列你懂的，都没有就客户端侧轮询分区
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"org.apache.kafka.clients.producer.internals.DefaultPartitioner");


        props.put(ProducerConfig.RECEIVE_BUFFER_CONFIG,"-1");
    }
}
