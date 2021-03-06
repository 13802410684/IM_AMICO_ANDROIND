package com.htmessage.yichatopen.activity.addfriends.add.next;

import android.app.ProgressDialog;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htmessage.yichatopen.R;
import com.htmessage.yichatopen.HTConstant;
import com.htmessage.yichatopen.utils.OkHttpUtils;
import com.htmessage.yichatopen.utils.Param;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：HTOpen
 * 类描述：AddFriendNextPrestener 描述:
 * 创建人：songlijie
 * 创建时间：2017/7/7 17:23
 * 邮箱:814326663@qq.com
 */
public class AddFriendNextPrestener implements AddFriendNextBasePrestener {
    private AddFriendNextView nextView;

    public AddFriendNextPrestener(AddFriendNextView nextView) {
        this.nextView = nextView;
        this.nextView.setPresenter(this);
    }

    @Override
    public void searchUser() {
        if (TextUtils.isEmpty(nextView.getInputString())){
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(nextView.getBaseContext());
        dialog.setMessage(nextView.getBaseContext().getString(R.string.are_finding_contact));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        List<Param> paramList=new ArrayList<>();
        paramList.add(new Param("userName", nextView.getInputString()));
        new OkHttpUtils(nextView.getBaseContext()).post(paramList, HTConstant.URL_Search_User, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog.dismiss();
                String errorNo = jsonObject.getString("errorNo");
                if (errorNo.equals("0")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(jsonArray.size()>0){
                        nextView.onSearchSuccess( jsonArray.getJSONObject(0));
                    }
                }else if (errorNo.equals("100003")) {
                    nextView.onSearchFailed(nextView.getBaseContext().getString(R.string.User_does_not_exis));
                }else{
                    nextView.onSearchFailed(nextView.getBaseContext().getString(R.string.server_is_busy_try_again));
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                nextView.onSearchFailed(errorMsg);
            }
        });
    }

    @Override
    public void start() {

    }
}
