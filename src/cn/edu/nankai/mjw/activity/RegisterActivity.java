package cn.edu.nankai.mjw.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.icode.chengcheng.internet.WebAccessUtils;
//import com.icode.chengcheng.po.Hobbies;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
    private EditText editText , transUccid;
    private final static int DIALOG=1;
   // private ArrayList<Integer> tagList;
    boolean[] flags=new boolean[]{false,false,false,false,false,false,false,false,false,false,false
    		,false,false,false,false,false,false,false,false,false,false,false,false,false,false};//初始复选情况
    String[] items=null;
    ArrayList<Integer> tagList = new ArrayList<Integer>();
    String uccid = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerh);

        transUccid= (EditText) this.findViewById(R.id.transUccid);
        Intent intent=getIntent(); 
        uccid=intent.getStringExtra("uccid"); 
        //transUccid.setText("您的城程号:"+uccid);        
        items=getResources().getStringArray(R.array.hobby);
        editText=(EditText)findViewById(R.id.editText);
        Button buttonHobby = (Button) findViewById(R.id.buttonHobby);
        buttonHobby.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                // 显示对话框
                showDialog(DIALOG);
            }
        });
    }
    
    /**
     * 创建复选框对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) {
        case DIALOG:
            Builder builder=new android.app.AlertDialog.Builder(this);
            //设置对话框的图标
           // builder.setIcon(R.drawable.header);
            //设置对话框的标题
            builder.setTitle("选择您的爱好");
            builder.setMultiChoiceItems(R.array.hobby, flags, new DialogInterface.OnMultiChoiceClickListener(){
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    flags[which]=isChecked;
                    String result = "您选择了：";
                    for (int i = 0; i < flags.length; i++) {
                        if(flags[i]){
                        	 int j=i+1;   
                            result=result+items[i]+"、";
                            tagList.add(j);
                            List<Integer> listTemp= new ArrayList<Integer>();  
                            Iterator<Integer> it=tagList.iterator();  
                            while(it.hasNext()){  
                             int a=it.next();  
                             if(listTemp.contains(a)){  
                              it.remove();  
                             }  
                             else{  
                              listTemp.add(a);  
                             }  
                            }  
                        }
                    }
                    editText.setText(result.substring(0, result.length()-1));
                }
            });
            
            //添加一个确定按钮
            builder.setPositiveButton(" 确 定 ", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    
                }
            });
            //创建一个复选框对话框
            dialog=builder.create();
            break;
        }
        return dialog;
    }
    
  //实现actionbar中的选项按钮，重写父类中的一个onCreateOptionMenu
  		@Override
  		public boolean onCreateOptionsMenu(Menu menu) {
  			// TODO Auto-generated method stub
  			//动态引入改Menu菜单的布局文件 
  			MenuInflater inflater =getMenuInflater();
  			//使用inflater中的inflate方法锁定布局文件
  			inflater.inflate(R.menu.actionbar_menu_registerh, menu);
  			return super.onCreateOptionsMenu(menu);
  		}

  		
  		//实现actionbar中的选项单击事件，重写父类中的方法		
  		@Override
  		public boolean onOptionsItemSelected(MenuItem item) {
  			// TODO Auto-generated method stub
  			//判断系统回去的item
  			switch (item.getItemId()) {
  			case R.id.ab_next:
  			// 步骤1：获取数据并封装对象，将对象进行序列化（JSON）
				// 步骤1-1：获取用户注册时填写的信息

				String account = uccid;        //账号
				
				
				// 步骤1-2：对象封装
				Hobbies hobby = new Hobbies();
				System.out.println(tagList);
				for(Integer htag :tagList){
				hobby.setHuser(account);
				hobby.setHtag(htag);
				// 步骤1-3：序列化
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
				String user_data = gson.toJson(hobby);
				
				// 步骤2：设置请求参数集合并调用方法向网络发送请求数据			
				
				// 步骤2-1：创建一个参数集合
				List<NameValuePair> lstNameValuePairs = new ArrayList<NameValuePair>();
				lstNameValuePairs.add(new BasicNameValuePair("user_data", user_data));
				
				// 步骤2-2：调用方式实现请求的发送
				WebAccessUtils.httpRequest("RegisterhServlet", lstNameValuePairs);	}			
				
				
  				Intent intent = new Intent();
  				intent.putExtra("uccid", account); 
  				intent.setClass(RegisterActivity.this, LoginActivity.class);
  				startActivity(intent);
  				finish();
  				break;

  			default:
  				break;
  			}
  			return true;
  		}
}