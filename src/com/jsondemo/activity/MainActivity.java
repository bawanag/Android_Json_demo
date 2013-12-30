package com.jsondemo.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText mEtName;
	private Button mBtnLogin;
	private TextView mTvResult;
	private String mStrName, mStrResult;
	private MyTask mTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mEtName = (EditText) findViewById(R.id.et_hello);
		mTvResult = (TextView) findViewById(R.id.tv_result);
		mBtnLogin = (Button) findViewById(R.id.btn_login);

		mBtnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mStrName = (mEtName).getText().toString();
				// AsyncTask异步任务开始
				mTask = new MyTask();
				mTask.execute(mStrName);
			}
		});
	}

	private class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient hc = new DefaultHttpClient();
			// 这里是服务器的IP，不要写成localhost了，即使在本机测试也要写上本机的IP地址，localhost会被当成模拟器自身的
			String address = "http://192.168.1.107:8080/ServerJsonDemo/servlet/JsonServlet";
			HttpPost hp = new HttpPost(address);
			// 封装JSON对象
			JSONObject jsonObj = new JSONObject();
			String age = null, id = null;
			try {
				jsonObj.put("name", params[0]);
				// 绑定请求到Entity
				hp.setEntity(new StringEntity(jsonObj.toString()));
				// 发送请求
				HttpResponse response = hc.execute(hp);
				// 返回200即请求成功
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获取响应中的数据，这也是一个JSON格式的数据
					mStrResult = EntityUtils.toString(response.getEntity());
					// 将返回结果生成JSON对象
					JSONObject result = new JSONObject(mStrResult);
					// 从中提取需要的值
					age = result.getString("age");
					id = result.getString("id");
					System.out.println("id:" + id + "age:" + age);
					System.out.println("result" + mStrResult);
				} else {
					System.out.println("连接失败");
				}
 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "员工号:" + id + " 年龄:" + age;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mTvResult.setText(result);
		}
	}
}