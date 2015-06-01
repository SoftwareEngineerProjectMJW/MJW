package cn.edu.nankai.mjw.activity;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.edu.nankai.mjw.R;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.icode.chengcheng.internet.WebAccessUtils;
//import com.icode.chengcheng.po.Users;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// ����1���̳и���Android.app.Activity
@SuppressLint("NewApi")
public class LoginActivity extends Activity {
	
	// ����4��������ҳ���еĽ��������
	private EditText txtAccount, txtPassword;
	private Button btnRegister, btnLogin;

	// ����2����д�����е�һ������OnCreate()  Ctrl+Shift+S,V	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// ���ڷ��ʻ���������˽�������ʷ��뵽���߳��н���
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		// ��չ�����ظô���ı�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����3������������Ӧ�Ĳ����ļ����а�
		this.setContentView(R.layout.activity_login);
		// ����5��ʵ�������������
		this.txtAccount = (EditText) this.findViewById(R.id.txtAccount);
		this.txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		this.btnRegister = (Button) this.findViewById(R.id.btnRegister);
		this.btnLogin = (Button) this.findViewById(R.id.btnLogin);
		
		//���غ����ó�ʼ�˺�ֵ
		Intent intent=getIntent(); 
        String uccid=intent.getStringExtra("uccid"); 
        txtAccount.setText(uccid);
		
		// ����7������󶨼�����
		this.btnRegister.setOnClickListener(new ViewOcl());
		this.btnLogin.setOnClickListener(new ViewOcl());
	}
	
	// ����6������һ���Զ�����ڲ��������Linstener
	private class ViewOcl implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String message=null;
			switch (v.getId()) {
			case R.id.btnRegister:
				message = "��ӭע��ǳ���!"; 
				showCustomerToast(message);
				// ������ת
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.btnLogin:

				// ����1����ȡ���ݲ���װ���󣬽�����������л���JSON��
				// ����1-1����ȡ�˺ź�����

				String account = txtAccount.getText().toString().trim();
				String password = txtPassword.getText().toString().trim();
				// ����1-2�������װ
				Users user = new Users();
				user.setUccid(account);
				user.setUpwd(password);
				// ����1-3�����л�
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
				String user_data = gson.toJson(user);
				
				// ����2����������������ϲ����÷��������緢����������			
				
				// ����2-1������һ����������
				List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
				lstNameValuePairs.add(new BasicNameValuePair("user_data", user_data));
				
				// ����2-2�����÷�ʽʵ������ķ���
				String response = WebAccessUtils.httpRequest("LoginServlet", lstNameValuePairs);				
				// ����3������JSON����
				// ����3-1�������л����ݷ�װ��һ������
				Users u = null;
				u = gson.fromJson(response, Users.class);				
				if(u != null){
					message = "��ӭ��½�ǳ�����"+u.getUname()+"!";
					showCustomerToast(message);
					Intent intent2 = new Intent();
					SharedPreferences settings = getSharedPreferences("users", MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("uccid", account);
					editor.commit();
					txtAccount.setText(settings.getString("username",account));
				    txtPassword.setText(settings.getString("password", password));  
					intent2.setClass(LoginActivity.this, TabHostActivity.class);
					startActivity(intent2);
					finish();
				}else{
					message = "�˺Ż��������!";
					showCustomerToast(message);
				}
				
				break;
			default:
				break;
			}
		}
		
	}

	//�Զ���Toast��Ϣ��ʾ
	private void showCustomerToast(final String message){
		
		//��ȡLayoutInflater����
		LayoutInflater inflater = getLayoutInflater();
		//ʹ��inflater�����е�inflate�������Զ���Toast�Ĳ����ļ���ͬʱָ��ò����ļ��еĸ����ֽ��
		View layout = inflater.inflate(R.layout.toast_customer, (ViewGroup)findViewById(R.id.toast_layout_root));
		//��ȡ�ò�������е�TextView�����Ϊ�䶯̬��ֵ
		TextView toastMessage = (TextView) layout.findViewById(R.id.txtMessage);
		//����textview�е��ı�����
		toastMessage.setText(message);
		//ʵ����Toast����
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
		
		
	}
	
	
}