package com.amico.im.service;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.amico.im.client.net.IMClientService;
import com.amico.im.client.net.listener.ConnectListener;
import com.amico.im.client.net.process.req.ClientPacket;
import com.amico.im.mn.sdk.consts.ActionEvents;
import com.amico.im.mn.sdk.consts.Actions;
import com.amico.im.mn.sdk.consts.RequsetParamsName;
import com.amico.service.im.net.rps.dto.AddFriendRpsDto;
import com.amico.service.im.net.rps.dto.LoginRpsDto;
import com.htmessage.sdk.client.HTAction;
import com.htmessage.sdk.manager.PreferenceManager;
import com.htmessage.sdk.service.MessageService;
import com.htmessage.yichatopen.HTConstant;

import org.smartboot.ioc.transport.NioSession;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import rx.functions.ActionN;

public class IMService extends Service {

    //聊天客户端服务器
    private IMClientService imClient;

    private LocalBroadcastManager localBroadcastManager;
    /***
     * 创建连接对象监听
     */
    private class IMListener implements  ConnectListener{

        @Override
        public void onConnected(NioSession<ClientPacket> session) {
            IMClientService imClient = session.getAttachment();
            imClient.loginIMService(imClient.getUserId(),imClient.getToken());
        }

        @Override
        public void onClose(NioSession<ClientPacket> session) {

        }

        @Override
        public void onSuccess(NioSession<ClientPacket> session, Object result) {
                //如果登录成功
                if(result instanceof LoginRpsDto){
                    LoginRpsDto loginRpsDto = (LoginRpsDto)result;
                    PreferenceManager.getInstance().setUser(loginRpsDto.getUserId(), loginRpsDto.getToken());
                    //发送登录消息
                    Intent intent = new Intent(Actions.ACTION_LOGIN);
                    intent.putExtra("errorNo", "0");
                    localBroadcastManager.sendBroadcast(intent);
                }
                if(result instanceof AddFriendRpsDto){
                    //发送加好友结果
                    AddFriendRpsDto addFriendRpsDto = (AddFriendRpsDto)result;
                    Intent intent = new Intent(Actions.ACTION_ADD_FRIEND);
                    intent.putExtra("errorNo", addFriendRpsDto.getErrorNo());
                    intent.putExtra("errorInfo", addFriendRpsDto.getErrorInfo());
                    localBroadcastManager.sendBroadcast(intent);
                }
        }

        @Override
        public void onError(NioSession<ClientPacket> session, String errorNo, String errorInfo) {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getApplicationContext());
    }

    /**
     * 连接服务器后直接登陆
     * @param userId
     * @param userToken
     */
    protected  void connectIMService(String userId,String userToken){
        imClient = new IMClientService(new IMListener());
        try {
            imClient.setUserId(userId);
            imClient.setToken(userToken);
            imClient.connect(HTConstant.HOST_IM,HTConstant.HOST_PORT);
            Log.i(this.getClass().getName(),"im connecting service please wait !");
        } catch (IOException e) {
            Log.i(this.getClass().getName(),e.getMessage());
        } catch (ExecutionException e) {
            Log.i(this.getClass().getName(),e.getMessage());
        } catch (InterruptedException e) {
            Log.i(this.getClass().getName(),e.getMessage());
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //接收所有对应的聊天信息
        if (intent != null) {
            String action = intent.getAction();
            if(TextUtils.isEmpty(action)==false){
                //如果是基础业务
                String actionEvent = intent.getExtras().getString(RequsetParamsName.ACTION_EVENT);
                if(Actions.CHAT_BASE_BIZ.equals(action)){
                    if(ActionEvents.IM_LOGIN.equals(actionEvent)){
                        //准备登陆
                        String userId = intent.getExtras().getString(RequsetParamsName.USER_ID);
                        String userToken = intent.getExtras().getString(RequsetParamsName.USER_TOKEN);
                        connectIMService(userId,userToken);
                    }
                    //加好友
                    if(ActionEvents.IM_FRIEND_ADD.equals(actionEvent)){
                        String userId = intent.getExtras().getString(RequsetParamsName.USER_ID);
                        String friendId = intent.getExtras().getString(RequsetParamsName.FRIEND_ID);
                        String desc = intent.getExtras().getString(RequsetParamsName.DESC);
                        imClient.sendAddFriends(userId,friendId,desc);
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



}
