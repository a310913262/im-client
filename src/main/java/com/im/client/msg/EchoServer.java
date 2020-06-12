package com.im.client.msg;

import com.im.client.msg.handler.MsgHandler;
import com.im.client.msg.pojo.MyDataInfo1;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * description: EchoServer <br>
 * date: 2020/6/10 16:21 <br>
 * author: sunfei <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class EchoServer implements ApplicationRunner {


    EventLoopGroup eventLoopGroup=new NioEventLoopGroup();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("*************************************MSG NETTY START*************************************");

        try{
            Bootstrap bootstrap=new Bootstrap();
            /**
             * handler()里面一定要填对，上次就是因为看错了，写成了Server的Initizlizer，一定用客户端的
             */
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                    handler(new TestServerInitializer());
            /**
             * 这里和之前例子不一样，他获得的是一个Channel
             * 表示和服务器端连接
             */
            Channel channel =bootstrap.connect("localhost",8899).sync().channel();

            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            for(; ;){
                //读取一行数据，回车即读取
                channel.writeAndFlush(br.readLine()+"\r\n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

class TestServerInitializer extends ChannelInitializer <SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(MyDataInfo1.MyMessage.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new MsgHandler());
    }
}
