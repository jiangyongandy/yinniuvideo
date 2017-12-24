package org.cocos2dx.lua.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.ui.BaseActivity;
import org.cocos2dx.lua.ui.BrowserActivity;
import org.cocos2dx.lua.ui.HomeActivity;
import org.cocos2dx.lua.ui.PersonActivity;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.tencent.smtt.sdk.TbsReaderView.TAG;
import static org.cocos2dx.lua.APPAplication.api;


/**
 * Created by JIANG on 2017/9/16.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.iv_banner)
    ImageView mIvBanner;
    @BindView(R.id.ll_jiaoliu)
    LinearLayout mLlJiaoliu;
    @BindView(R.id.ll_communication)
    LinearLayout mLlCommunication;
    @BindView(R.id.ll_member_center)
    LinearLayout mLlMemberCenter;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.gv_list)
    NoScrollGridView mGvList;
    @BindView(R.id.sv_container)
    ScrollView mSvContainer;
    private Unbinder mUnbinder;
    private final String mAboutUrl = "file:///android_asset/about/index.html";

    private void wechatLogin(int position) {
        VipHelperUtils.getInstance().changeCurrentSite(position);
        if (VipHelperUtils.getInstance().isValidVip()) {
            Intent intent = new Intent(getActivity(), BrowserActivity.class);
            getActivity().startActivity(intent);

        }else if(VipHelperUtils.getInstance().isWechatLogin()) {

            Toast.makeText(
                    getActivity(),
                    "Vip已经过期啦，需要重新激活哦",
                    Toast.LENGTH_SHORT).show();

            //test
            if(CommonConstant.buildConfig.isDebug) {

                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                getActivity().startActivity(intent);
            }

        } else {

            new AlertDialog.Builder(getActivity())
                    .setTitle("需要登陆微信")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // send oauth request
                                    final SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "none";
                                    boolean sendReq = api.sendReq(req);
                                    if (sendReq) {
                                        Log.v(TAG, "sendReq  sendReq ---------------true");
                                    }
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(
                                            getActivity(),
                                            "拒绝登录无法观看...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .setOnCancelListener(
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(
                                            getActivity(),
                                            "取消...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).show();

        }
    }

    @OnClick({R.id.ll_jiaoliu, R.id.ll_communication, R.id.ll_member_center, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_jiaoliu:
                break;
            case R.id.ll_communication:
                break;
            case R.id.ll_member_center:
                Intent intent1 = new Intent(mActivity, PersonActivity.class);
                intent1.putExtra("URL", "file:///android_asset/about/index.html");
                HomeFragment.this.startActivity(intent1);
                break;
            case R.id.ll_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "这里是分享内容");
                intent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(intent, "分享到"));
                break;
        }
    }

    @Override
    protected void initData() {
        mUnbinder = ButterKnife.bind(this, mRootView);
        ListAdapter listAdapter = new ListAdapter(mActivity, VipHelperUtils.getInstance().getNames());
        mGvList.setAdapter(listAdapter);
        mGvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wechatLogin(position);
            }
        });
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_home;
    }

    public class ListAdapter extends BaseAdapter {

        private final String[] names;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, String[] names) {
            this.context = context;
            this.names = names;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_menu_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            holder.textView.setText(VipHelperUtils.getInstance().getNames()[position]);
            Drawable drawable = context.getResources().getDrawable(VipHelperUtils.getInstance().getIcons()[position]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            holder.ivLogo.setImageDrawable(drawable);
            return rootView;
        }

        class ViewHolder {
            TextView textView;
            ImageView ivLogo;
        }

    }
}
