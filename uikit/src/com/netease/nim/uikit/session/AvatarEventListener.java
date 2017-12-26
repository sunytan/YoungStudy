package com.netease.nim.uikit.session;

import android.content.Context;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;


/**
 * Created by edz on 2017/12/26.
 */

public class AvatarEventListener implements SessionEventListener {
    @Override
    public void onAvatarClicked(Context context, IMMessage message) {
        if (message.getDirect() == MsgDirectionEnum.In){
            Toast.makeText(context,"对方头像点击",Toast.LENGTH_SHORT).show();
        }else if (message.getDirect() == MsgDirectionEnum.Out){
            Toast.makeText(context,"自己头像点击",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAvatarLongClicked(Context context, IMMessage message) {
        if (message.getDirect() == MsgDirectionEnum.In){
            Toast.makeText(context,"对方头像长按",Toast.LENGTH_SHORT).show();
        }else if (message.getDirect() == MsgDirectionEnum.Out){
            Toast.makeText(context,"自己头像长按",Toast.LENGTH_SHORT).show();
        }
    }
}
