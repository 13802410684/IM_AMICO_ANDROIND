package com.htmessage.yichatopen.activity.main.details;

import com.alibaba.fastjson.JSONObject;
import com.htmessage.yichatopen.HTApp;
import com.htmessage.yichatopen.HTConstant;
import com.htmessage.yichatopen.domain.User;
import com.htmessage.yichatopen.domain.UserDao;
import com.htmessage.yichatopen.manager.ContactsManager;
import com.htmessage.yichatopen.utils.CommonUtils;
import com.htmessage.yichatopen.utils.OkHttpUtils;
import com.htmessage.yichatopen.utils.Param;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：yichat0504
 * 类描述：UserDetailsPrester 描述:
 * 创建人：songlijie
 * 创建时间：2017/7/10 11:40
 * 邮箱:814326663@qq.com
 */
public class UserDetailsPrester implements UserDetailsBasePrester {

    private UserDetailsView detailsView;

    public UserDetailsPrester(UserDetailsView detailsView) {
        this.detailsView = detailsView;
        this.detailsView.setPresenter(this);
    }

    @Override
    public void onDestory() {
        detailsView = null;
    }

    @Override
    public void refreshInfo(final String userId, final boolean backTask) {
        if (!backTask){
            detailsView.showDialog();
        }
        List<Param> parms = new ArrayList<>();
        parms.add(new Param("userId", userId));
        new OkHttpUtils(detailsView.getBaseContext()).post(parms, HTConstant.URL_Get_UserInfo, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String errorNo = jsonObject.getString("errorNo");
                if (!backTask){
                    detailsView.hintDialog();
                }
                if(errorNo.equals("0")){
                    JSONObject json = jsonObject.getJSONObject("data");
                    if (isFriend(userId)) {
                        User user = CommonUtils.Json2User(json);
                        UserDao dao = new UserDao(detailsView.getBaseContext());
                        dao.saveContact(user);
                        ContactsManager.getInstance().getContactList().put(user.getUsername(), user);
                    }
                    detailsView.showUi(json);
                }else if(errorNo.equals("100003")){
                    detailsView.hintDialog();
                }else{
                    detailsView.hintDialog();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                detailsView.onRefreshFailed(errorMsg);
                if (!backTask){
                    detailsView.hintDialog();
                }
            }
        });
    }

    @Override
    public boolean isMe(String userId) {
        return HTApp.getInstance().getUsername().equals(userId);
    }

    @Override
    public boolean isFriend(String userId) {
        return ContactsManager.getInstance().getContactList().containsKey(userId);
    }

    @Override
    public void start() {

    }
}
