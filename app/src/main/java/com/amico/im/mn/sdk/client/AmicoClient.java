package com.amico.im.mn.sdk.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.amico.im.mn.sdk.consts.ActionEvents;
import com.amico.im.mn.sdk.consts.Actions;
import com.amico.im.mn.sdk.consts.RequsetParamsName;
import com.amico.im.service.IMService;
import com.htmessage.sdk.client.HTClient;
import com.htmessage.sdk.client.HTOptions;
import com.htmessage.sdk.manager.ConversationManager;
import com.htmessage.sdk.manager.DBManager;
import com.htmessage.sdk.manager.HTChatManager;
import com.htmessage.sdk.manager.MessageManager;
import com.htmessage.sdk.manager.PreferenceManager;


public class AmicoClient {
    private static Context appContext ;
    private static AmicoClient amicoClient;
    private static  AmicoOptions amicoOptions;
    private static MessageManager messageManager;
    private static ConversationManager conversationManager;
    private static HTChatManager htChatManager;
    private static DBManager dbManager;
    private AmicoCallBack amicoCallBack;
    private static final String TAG = AmicoClient.class.getSimpleName();
    private ConnectionBroadcastReceiver connectionBroadcastReceiver;
    // 如果有新消息甩给client处理
    private HTClient.MessageLisenter messageLisenter ;


    private AmicoClient(Context appContext, AmicoOptions amicoOptions) {
        this.appContext = appContext;
        this.amicoOptions = amicoOptions;
        initReceiver(appContext);
    }

    private void initAllManager(HTOptions var1) {
        PreferenceManager.init(appContext);
        if (PreferenceManager.getInstance().getUser() != null) {
            DBManager.init(appContext);
            messageManager = new MessageManager(appContext);
            conversationManager = new ConversationManager(appContext);
            htChatManager = new HTChatManager(appContext);
        }
    }

    public HTClient.MessageLisenter getMessageLisenter() {
        return messageLisenter;
    }

    public void setMessageLisenter(HTClient.MessageLisenter messageLisenter) {
        this.messageLisenter = messageLisenter;
    }

    //登录
    public void login(String userId, String userToken,AmicoCallBack amicoCallBack) {
        this.amicoCallBack = amicoCallBack;
        Intent intent = new Intent(appContext, IMService.class);
        intent.setAction(Actions.CHAT_BASE_BIZ);
        intent.putExtra(RequsetParamsName.ACTION_EVENT,ActionEvents.IM_LOGIN);
        intent.putExtra(RequsetParamsName.USER_ID, userId);
        intent.putExtra(RequsetParamsName.USER_TOKEN, userToken);
        appContext.startService(intent);
    }

    public void sendAddFriend(String userId, String friendId,String desc,AmicoCallBack amicoCallBack) {
        this.amicoCallBack = amicoCallBack;
        Intent intent = new Intent(appContext, IMService.class);
        intent.setAction(Actions.CHAT_BASE_BIZ);
        intent.putExtra(RequsetParamsName.ACTION_EVENT,ActionEvents.IM_LOGIN);
        intent.putExtra(RequsetParamsName.USER_ID, userId);
        intent.putExtra(RequsetParamsName.FRIEND_ID, friendId);
        intent.putExtra(RequsetParamsName.DESC,desc);
        appContext.startService(intent);
    }

    public static AmicoClient newInstance(){
        return amicoClient;
    }

    public static void init(Context context, AmicoOptions amicoOptions) {
        if (amicoClient == null) {
            amicoClient = new AmicoClient(context, amicoOptions);
        }
    }

    public interface AmicoCallBack {
        void onSuccess();

        void onError();
    }




    private void initReceiver(Context context) {
        this.connectionBroadcastReceiver = new AmicoClient.ConnectionBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.ACTION_LOGIN);
        intentFilter.addAction(Actions.ACTION_ADD_FRIEND);
        LocalBroadcastManager.getInstance(context).registerReceiver(this.connectionBroadcastReceiver, intentFilter);
    }

    class ConnectionBroadcastReceiver extends BroadcastReceiver {
        ConnectionBroadcastReceiver() {
        }

        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (action.equals(Actions.ACTION_LOGIN)){
                String errorNo = intent.getExtras().getString("errorNo");
                if("0".equals(errorNo)){
                    initAllManager(null);
                    amicoCallBack.onSuccess();
                }else{
                    amicoCallBack.onError();
                }
            }
            if (action.equals(Actions.ACTION_ADD_FRIEND)){
                String errorNo = intent.getExtras().getString("errorNo");
                if("0".equals(errorNo)){
                    amicoCallBack.onSuccess();
                }else{
                    amicoCallBack.onError();
                }
            }
        }
    }


}
