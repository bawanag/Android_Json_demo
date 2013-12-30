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
				// AsyncTask�첽����ʼ
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
			// �����Ƿ�������IP����Ҫд��localhost�ˣ���ʹ�ڱ�������ҲҪд�ϱ�����IP��ַ��localhost�ᱻ����ģ���������
			String address = "http://192.168.1.107:8080/ServerJsonDemo/servlet/JsonServlet";
			HttpPost hp = new HttpPost(address);
			// ��װJSON����
			JSONObject jsonObj = new JSONObject();
			String age = null, id = null;
			try {
				jsonObj.put("name", params[0]);
				// ������Entity
				hp.setEntity(new StringEntity(jsonObj.toString()));
				// ��������
				HttpResponse response = hc.execute(hp);
				// ����200������ɹ�
				if (response.getStatusLine().getStatusCode() == 200) {
					// ��ȡ��Ӧ�е����ݣ���Ҳ��һ��JSON��ʽ������
					mStrResult = EntityUtils.toString(response.getEntity());
					// �����ؽ������JSON����
					JSONObject result = new JSONObject(mStrResult);
					// ������ȡ��Ҫ��ֵ
					age = result.getString("age");
					id = result.getString("id");
					System.out.println("id:" + id + "age:" + age);
					System.out.println("result" + mStrResult);
				} else {
					System.out.println("����ʧ��");
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
			return "Ա����:" + id + " ����:" + age;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mTvResult.setText(result);
		}
	}
}