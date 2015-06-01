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

// 步骤1：继承父类Android.app.Activity
@SuppressLint("NewApi")
public class LoginActivity extends Activity {
	
	// 步骤4：声明该页面中的交互类组件
	private EditText txtAccount, txtPassword;
	private Button btnRegister, btnLogin;

	// 步骤2：重写父类中的一个方法OnCreate()  Ctrl+Shift+S,V	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 由于访问互联网，因此将网络访问放入到子线程中进行
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		// 扩展：隐藏该窗体的标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 步骤3：将窗体与相应的布局文件进行绑定
		this.setContentView(R.layout.activity_login);
		// 步骤5：实例化操作类组件
		this.txtAccount = (EditText) this.findViewById(R.id.txtAccount);
		this.txtPassword = (EditText) this.findViewById(R.id.txtPassword);
		this.btnRegister = (Button) this.findViewById(R.id.btnRegister);
		this.btnLogin = (Button) this.findViewById(R.id.btnLogin);
		
		//跳回后设置初始账号值
		Intent intent=getIntent(); 
        String uccid=intent.getStringExtra("uccid"); 
        txtAccount.setText(uccid);
		
		// 步骤7：组件绑定监听器
		this.btnRegister.setOnClickListener(new ViewOcl());
		this.btnLogin.setOnClickListener(new ViewOcl());
	}
	
	// 步骤6：创建一个自定义的内部类监听器Linstener
	private class ViewOcl implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String message=null;
			switch (v.getId()) {
			case R.id.btnRegister:
				message = "欢迎注册城程网!"; 
				showCustomerToast(message);
				// 界面跳转
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.btnLogin:

				// 步骤1：获取数据并封装对象，将对象进行序列化（JSON）
				// 步骤1-1：获取账号和密码

				String account = txtAccount.getText().toString().trim();
				String password = txtPassword.getText().toString().trim();
				// 步骤1-2：对象封装
				Users user = new Users();
				user.setUccid(account);
				user.setUpwd(password);
				// 步骤1-3：序列化
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
				String user_data = gson.toJson(user);
				
				// 步骤2：设置请求参数集合并调用方法向网络发送请求数据			
				
				// 步骤2-1：创建一个参数集合
				List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
				lstNameValuePairs.add(new BasicNameValuePair("user_data", user_data));
				
				// 步骤2-2：调用方式实现请求的发送
				String response = WebAccessUtils.httpRequest("LoginServlet", lstNameValuePairs);				
				// 步骤3：处理JSON数据
				// 步骤3-1：反序列化数据封装成一个对象
				Users u = null;
				u = gson.fromJson(response, Users.class);				
				if(u != null){
					message = "欢迎登陆城程网，"+u.getUname()+"!";
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
					message = "账号或密码错误!";
					showCustomerToast(message);
				}
				
				break;
			default:
				break;
			}
		}
		
	}

	//自定义Toast信息显示
	private void showCustomerToast(final String message){
		
		//获取LayoutInflater对象
		LayoutInflater inflater = getLayoutInflater();
		//使用inflater对象中的inflate方法绑定自定义Toast的布局文件，同时指向该布局文件中的跟布局结点
		View layout = inflater.inflate(R.layout.toast_customer, (ViewGroup)findViewById(R.id.toast_layout_root));
		//获取该布局组件中的TextView组件并为其动态赋值
		TextView toastMessage = (TextView) layout.findViewById(R.id.txtMessage);
		//设置textview中的文本内容
		toastMessage.setText(message);
		//实例化Toast对象
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
		
		
	}
	
	
}
