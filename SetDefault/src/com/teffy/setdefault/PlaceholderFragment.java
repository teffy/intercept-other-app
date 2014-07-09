
package com.teffy.setdefault;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.teffy.setdefault.set.BindActions;

public class PlaceholderFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{
    private View rootView;
//    public void setsmstodefault(){
//        final String packageName = this.getPackageName();
//        final Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
//        startActivityForResult(intent,REQUEST_CODE_DEFAULT_APPLICATION);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        switch(requestCode){
//        case REQUEST_CODE_DEFAULT_APPLICATION:
//            if(MmsConfig.isSmsEnabled(this) || MmsConfig.isSmsPromoDismissed(this)){
//                Toast toast = Toast.makeText(this, getResources().getString(R.string.banner_rongxin_setting_success), 500);
//                toast.show();
//                updatePromoView();
//            }
//            break;
//        }
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(inflater, container, savedInstanceState);
        return rootView;
    }

    private void initView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView.findViewById(R.id.bt_set_call).setOnClickListener(this);
        rootView.findViewById(R.id.bt_set_mms).setOnClickListener(this);
        rootView.findViewById(R.id.bt_set_contacts).setOnClickListener(this);
        // TODO 初始化值
        
        SharedPreferences sp = getActivity().getSharedPreferences(BindActions.SCREEN_BIND_ACTIONS, Context.MODE_PRIVATE);
        boolean _call = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Call", true);
        boolean _contacts = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Contacts", true);
        boolean _mms = sp.getBoolean(BindActions.BIND_ICON_ACTION_+"Mms", true);
        ((Switch)rootView.findViewById(R.id.sw_call)).setChecked(_call);
        ((Switch)rootView.findViewById(R.id.sw_contacts)).setChecked(_contacts);
        ((Switch)rootView.findViewById(R.id.sw_mms)).setChecked(_mms);
        ((Switch)rootView.findViewById(R.id.sw_call)).setOnCheckedChangeListener(this);
        ((Switch)rootView.findViewById(R.id.sw_contacts)).setOnCheckedChangeListener(this);
        ((Switch)rootView.findViewById(R.id.sw_mms)).setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = getActivity().getSharedPreferences(BindActions.SCREEN_BIND_ACTIONS, Context.MODE_PRIVATE);
        Intent i = new Intent(BindActions.BIND_ACTION);
        switch (buttonView.getId()) {
            case R.id.sw_call:
                sp.edit().putBoolean(BindActions.BIND_ICON_ACTION_+"Call", buttonView.isChecked()).commit();
                break;
            case R.id.sw_contacts:
                sp.edit().putBoolean(BindActions.BIND_ICON_ACTION_+"Contacts", buttonView.isChecked()).commit();
                break;
            case R.id.sw_mms:
                sp.edit().putBoolean(BindActions.BIND_ICON_ACTION_+"Mms", buttonView.isChecked()).commit();
                break;
            default:
                break;
        }
        getActivity().sendBroadcast(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_set_call:
                
                break;
            case R.id.bt_set_mms:
                
                break;
            case R.id.bt_set_contacts:
                
                break;

            default:
                break;
        }
    }
}
