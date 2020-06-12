package com.im.client.msg.handler;

import com.im.client.msg.pojo.MyDataInfo1;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: MsgHandler <br>
 * date: 2020/6/11 17:56 <br>
 * author: sunfei <br>
 * version: 1.0 <br>
 */
@Slf4j
public class MsgHandler extends SimpleChannelInboundHandler <MyDataInfo1.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo1.MyMessage myMessage) throws Exception {

        MyDataInfo1.MyMessage.DataType dataType = myMessage.getDataType();



        if (dataType == MyDataInfo1.MyMessage.DataType.oneType) {
            log.info("****************************************进入点对点会话****************************************");
//            为one to one会话
            MyDataInfo1.OneSession oneSession = myMessage.getOneSession();

            Integer targetId = oneSession.getTargetId();


        } else if (dataType == MyDataInfo1.MyMessage.DataType.groupTypeout) {
//            为group会话
            MyDataInfo1.GroupSession groupSession = myMessage.getGroupSession();

        } else {
            log.error("************************************没有指定消息类型************************************");
        }


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String s = ctx.channel().id().asShortText();
        log.warn(s+":激活后读取离线信息*******************************");
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("************************************chanel注册加入chanelgroup************************************");
        MyDataInfo1.OneSession build = MyDataInfo1.OneSession.newBuilder()
                .setMsg("用户xxxxx,登录im")
                .setSourceId(22)
                .setMsgStatus(1)
                .setTargetId(11)
                .setName("11  nike")
                .setTimestamp(Long.toString((new Date()).getTime())).build();
        MyDataInfo1.MyMessage.newBuilder().setDataType(MyDataInfo1.MyMessage.DataType.oneType).setOneSession(build);

//        ctx.channel().writeAndFlush()
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause.getMessage());
    }


}

