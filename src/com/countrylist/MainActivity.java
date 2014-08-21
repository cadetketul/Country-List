package com.countrylist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText contNameInput;
	public static String CONTINENT_NAME = "COUNTRIES";
	String url1 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.countries%20where%20place%3D%22";
	String url2 = "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	JSONProcessor jProcessor = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		EditText contNameInput = (EditText) findViewById(R.id.editText1);
		contNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		ListView lview = (ListView) findViewById(R.id.listView1);
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1, list);
		lview.setAdapter(adapter);
		contNameInput = (EditText) findViewById(R.id.editText1);
		Button submitBtn = (Button) findViewById(R.id.button1);
		submitBtn.setOnClickListener(submitBtnClicked);
	}

	public OnClickListener submitBtnClicked = new OnClickListener(){

		public void onClick(View v) {
			contNameInput = (EditText) findViewById(R.id.editText1);			
            String cname = contNameInput.getText().toString();
            if (cname.contains(" ")) {
				cname = cname.replace(" ", "%20");
			}
            final String finalURL = url1 + cname + url2;
            jProcessor = new JSONProcessor(finalURL);
            jProcessor.fetchJSON();
            while(!jProcessor.parsingComplete);
            list.clear();
            for(int i=0; i<jProcessor.getCountryCount(); i++){
            	list.add(jProcessor.getCountry(i));
            }           
            adapter.notifyDataSetChanged();
		}		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
