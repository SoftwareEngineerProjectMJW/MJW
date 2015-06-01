package cn.edu.nankai.mjw.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.icode.chengcheng.internet.WebAccessUtils;
import com.icode.chengcheng.po.Activities;
import com.icode.chengcheng.po.ShowActInIndex;
import com.icode.chengcheng.po.Users;


import com.icode.chengcheng.other.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private List<Map<String, ?>> lstData;
	private ListView lstMessages;
	private Button btnActivity;
	private boolean doubleBackToExitPressedOnce = false; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		this.lstMessages = (ListView) this.findViewById(R.id.lstMessages);
		this.lstData = fetchData();
	
        MyAdapter adapter = new MyAdapter(this);
	
		this.lstMessages.setAdapter(adapter);
	
		this.lstMessages.setOnItemClickListener(new ItemOcl());
		this.btnActivity=(Button) this.findViewById(R.id.btnActivity);
		btnActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, ActivitySquare.class);
			
				startActivity(intent);
			}
		});
	
	}

	@Override 
	protected void onResume() { 
	    super.onResume(); 
	    this.doubleBackToExitPressedOnce = false; 
	} 
	 
	@Override 
	public void onBackPressed() { 
	    if (doubleBackToExitPressedOnce) { 
	        super.onBackPressed(); 
	        MyApplication.getInstance().exit();
	    } 
	    this.doubleBackToExitPressedOnce = true; 
	    Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show(); 
	    new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 doubleBackToExitPressedOnce = false; 

			}
		},2000);
	} 
	private List<Map<String, ?>> fetchData() {
		// TODO Auto-generated method stub
	    SharedPreferences preferences = getSharedPreferences("users", MODE_PRIVATE);	 
		Users user = new Users();
		user.setUccid(preferences.getString("uccid", "0"));
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-ddhh:mm:ss").create();
		String user_data = gson.toJson(user);
		List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
		lstNameValuePairs.add(new BasicNameValuePair("user_data", user_data));
		String response = WebAccessUtils.httpRequest("ShowInIndexServletA", lstNameValuePairs);				
		Type ListMessages = new TypeToken<ArrayList<ShowActInIndex>>() {
		}.getType();
		
		List<Map<String, ?>> lst = new ArrayList<Map<String, ?>>();
	
		List<ShowActInIndex> lstVMessages = gson.fromJson(response, ListMessages);
	
		for (ShowActInIndex act : lstVMessages) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("txtActId",act.getAid());
		    item.put("imgPhoto", R.drawable.p2);
			item.put("txtUccid",act.getUccid());
		    item.put("txtNickName",act.getUname());
		    item.put("txtOwner",act.getOwner_id());
		    item.put("txtPtype",act.getPtype());
		
		    item.put("txtActParticipate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(act.getPdate()));
		
		    item.put("txtActName", act.getAname());
		    item.put("txtActDeadline", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(act.getAdeadline()));
		    item.put("txtActTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(act.getAdate()));
		    item.put("txtActTopic", act.getAtopic());
		    item.put("txtActPraise", act.getAproise_num());
		    item.put("txtActShare", act.getAshare_num());
	
		    lst.add(item);			
		}
		return lst;
	}

	private class ItemOcl implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub

			Map<String, ?> selectedItem = lstData.get(position);
			int aid= (Integer) selectedItem.get("txtActId");
			Intent intent = new Intent(MainActivity.this,ActivitySquare.class);
			intent.putExtra("aid", aid);
			startActivity(intent);
		}
		
	}
	private class MyAdapter extends BaseAdapter{
	
		private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 
      
		public MyAdapter(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.mInflater = LayoutInflater.from(context);

		}
        public void getData(){
        	lstData = fetchData();
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fetchData().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 final ViewHolder holder;	

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listitem_main, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.txtNickName= (TextView) convertView
						.findViewById(R.id.txtNickName);
				holder.txtActParticipate = (TextView) convertView
						.findViewById(R.id.txtActParticipate);
				holder.txtActName= (TextView) convertView
						.findViewById(R.id.txtActName);
				holder.txtActDeadline= (TextView) convertView
						.findViewById(R.id.txtActDeadline);
				holder.txtActTime= (TextView) convertView
						.findViewById(R.id.txtActTime);
				holder.txtActTopic= (TextView) convertView
						.findViewById(R.id.txtActTopic);
				
				holder.txtPtype= (TextView) convertView
						.findViewById(R.id.txtActPtype);
				holder.imgPhoto = (ImageButton) convertView.findViewById(R.id.imgPhoto);
				holder.btnZan = (Button) convertView.findViewById(R.id.btnZan);
				holder.btnSha = (Button) convertView.findViewById(R.id.btnSha);
				holder.btnPar = (Button) convertView.findViewById(R.id.btnPar);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/* 设置TextView显示的内容，即我们存放在动态数组中的数据 */
		
			Map<String, ?> datap = lstData.get(position);
			holder.txtNickName.setText(datap.get("txtNickName")
					.toString());
			holder.txtActParticipate.setText(datap.get("txtActParticipate")
					.toString());
			holder.txtActName.setText(datap.get("txtActName")
					.toString());
			holder.txtActDeadline.setText(datap.get("txtActDeadline")
					.toString());
			holder.txtActTopic.setText(datap.get("txtActTopic")
					.toString());
			holder.txtActTime.setText(datap.get("txtActTime")
					.toString());
			final String txtOwner = datap.get("txtOwner").toString();
			final String txtActId = datap.get("txtActId").toString();
			final String txtUccid = datap.get("txtUccid").toString();
			final String txtActPraise = datap.get("txtActPraise").toString();
			final String txtActShare = datap.get("txtActShare").toString();
			if(datap.get("txtPtype").toString().equals("0"))
			      holder.txtPtype.setText("发布了");
			else if(datap.get("txtPtype").toString().equals("1"))
				 holder.txtPtype.setText("参与了");
			else  holder.txtPtype.setText("转发了");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
			String now = format.format(new Date());
			String deadline = datap.get("txtActDeadline").toString();
			String time = datap.get("txtActTime").toString();
			final Users user = new Users();
			user.setUccid("111");
			String uccid = user.getUccid();
			String uccidx = datap.get("txtUccid").toString();
			if(time.compareTo(now)<0){
				holder.btnPar.setText("活动结束");
			}else if(deadline.compareTo(now)<0){
				holder.btnPar.setText("报名结束");
			}else if(!uccid.equals(uccidx)&&!txtOwner.equals(uccid)||(uccid.equals(uccidx)&&datap.get("txtPtype").toString().trim().equals("2"))){
				holder.btnPar.setText("参与活动");
			}else if(uccid.equals(uccidx)&&datap.get("txtPtype").toString().trim().equals("0")){
				holder.btnPar.setText("取消活动");
			}else if (uccid.equals(uccidx)&&datap.get("txtPtype").toString().trim().equals("1")){
				holder.btnPar.setText("退出活动");
			}
			
          
			holder.btnZan.setText("赞"+txtActPraise);
        
 			holder.btnSha.setText("转"+txtActShare);
			/** 为Button添加点击事件 */
			holder.btnZan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Activities act = new Activities();
					act.setAid(	Integer.parseInt(txtActId.toString().trim()));
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-ddhh:mm:ss").create();
					String act_data = gson.toJson(act);
					List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
					lstNameValuePairs.add(new BasicNameValuePair("act_data", act_data));
			        WebAccessUtils.httpRequest("AddZanServletA", lstNameValuePairs);				
			    	getData();
					notifyDataSetInvalidated();
			
				}
			});
			final ShowActInIndex showActInIndex = new ShowActInIndex();
			showActInIndex.setUccid(uccid);
			showActInIndex.setAid(Integer.parseInt(txtActId));
			holder.btnSha.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-ddhh:mm:ss").create();
					String actu_data = gson.toJson(showActInIndex);
					List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
					lstNameValuePairs.add(new BasicNameValuePair("actu_data", actu_data));
					String response = WebAccessUtils.httpRequest("ShareServletA", lstNameValuePairs);
					int msgcode = gson.fromJson(response, Integer.class);
					if(msgcode==1){
						getData();
					    notifyDataSetInvalidated();
					}
					else
						Toast.makeText(getApplicationContext(),"您已经转发或参加过该活动了", Toast.LENGTH_LONG).show();
				}
			});

			holder.btnPar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-ddhh:mm:ss").create();
				
					String actu_data = gson.toJson(showActInIndex);
					List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
					lstNameValuePairs.add(new BasicNameValuePair("actu_data", actu_data));
					if(holder.btnPar.getText().toString().equals("参与活动")){
					    String response = WebAccessUtils.httpRequest("ParticipateServletA", lstNameValuePairs);		
					    int msgcode = gson.fromJson(response, Integer.class);
						if(msgcode==1){
							getData();
					        notifyDataSetInvalidated();
						}else
							Toast.makeText(getApplicationContext(),"您已经参加过该活动了", Toast.LENGTH_LONG).show();
					}else if(holder.btnPar.getText().toString().equals("取消活动")){
						WebAccessUtils.httpRequest("CancelServletA", lstNameValuePairs);
						getData();
						notifyDataSetChanged();
					}else if(holder.btnPar.getText().toString().equals("退出活动")){
                        WebAccessUtils.httpRequest("InParticipateServletA", lstNameValuePairs);	
                    	getData();
                        notifyDataSetChanged();
					}
				}
			});
			holder.imgPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,ActivitySquare.class);
					intent.putExtra("uccid",txtUccid);
					startActivity(intent);
				}
			});
			
		
			return convertView;
	}

	/** 存放控件 */
	public final class ViewHolder {
		public ImageButton imgPhoto;
		
		public TextView txtNickName;
		public TextView txtActParticipate;
	
		public TextView txtActName;
		public TextView txtActDeadline;
		public TextView txtActTime;
		public TextView txtActTopic;
		
		public TextView txtPtype;
		public Button btnZan;
		public Button btnSha;
		public Button btnPar;
	}
	}
}
