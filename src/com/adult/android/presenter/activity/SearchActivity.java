package com.adult.android.presenter.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.adult.android.R;
import com.adult.android.utils.SharedPreferencesUtil;
import com.adult.android.utils.ToastUtil;

/**
 * 搜索页面
 * 
 * */

public class SearchActivity extends BaseActivity implements TextWatcher,
		OnClickListener, OnItemClickListener {

	private static final String TAG = "SearchActivity";
	private static ArrayList<String> list;
	private EditText input;
	private ListView mSearching;

	private String input_text;
	private Button cancelBtn;
	private View lineone;
	private View linetwo;
	private TextView texttwo;
	private TextView textone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/** 初始化 */
	private void initView() {
		lineone = findViewById(R.id.search_line_one);
		linetwo = findViewById(R.id.search_line_two);
		textone = (TextView) findViewById(R.id.search_hot_text);
		texttwo = (TextView) findViewById(R.id.search_history_text);

		textone.setOnClickListener(this);
		texttwo.setOnClickListener(this);

		lineone.setBackgroundResource(R.color.purple);
		linetwo.setBackgroundResource(R.color.lightgray);
		textone.setTextColor(getResources().getColor(R.color.purple));
		texttwo.setTextColor(getResources().getColor(R.color.black));

		input = (EditText) findViewById(R.id.search_input);
		input.addTextChangedListener(this);
		input.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					input_text = input.getText().toString().trim();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0); // 强制隐藏键盘
					if (input_text != null && !input_text.equals("")) {
						saveHistory(input_text);
						Intent intent = new Intent(SearchActivity.this,
								ProductListActivity.class);
						intent.putExtra("search_from", "keyword");
						intent.putExtra("keyword", input_text);
						startActivity(intent);
					} else {
						ToastUtil
								.showToastShort(SearchActivity.this, "请输入商品名称");
					}
					return true;
				}
				return false;
			}
		});
		cancelBtn = (Button) findViewById(R.id.search_cancel);
		cancelBtn.setOnClickListener(this);

		mSearching = (ListView) findViewById(R.id.searching_list);
		mSearching.setVisibility(View.GONE);
	}

	@Override
	public void afterTextChanged(Editable s) {
		input_text = input.getText().toString().trim();
		if (TextUtils.isEmpty(input_text)) {
			mSearching.setVisibility(View.GONE);
			// findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
			// findViewById(R.id.line_layout).setVisibility(View.VISIBLE);
			// findViewById(R.id.search_container).setVisibility(View.VISIBLE);
		} else {
			// new SuggestionModel().getSuggestionList(input_text,
			// SearchActivity.this);
			mSearching.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(input.getText().toString().trim())) {
			mSearching.setVisibility(View.GONE);
		} else {
			mSearching.setVisibility(View.VISIBLE);
			findViewById(R.id.search_layout).setVisibility(View.GONE);
			findViewById(R.id.line_layout).setVisibility(View.GONE);
			findViewById(R.id.search_container).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_cancel:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(input.getWindowToken(), 0); // 强制隐藏键盘
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 保存历史搜索记录
	 */
	public static void saveHistory(String text) {
		String save_Str = SharedPreferencesUtil.getSharedPreferences(
				"history_search", "history", "");
		String[] historys = save_Str.split(",");
		list = new ArrayList<String>();
		for (int i = 0; i < historys.length; i++) {
			if (!TextUtils.isEmpty(historys[i])) {
				list.add(historys[i]);
			}
		}
		int length = list.size();

		if (length < 10) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(text)) {
					list.remove(i);
				}
			}
			list.add(text);
		} else if (length == 10) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(text)) {
					list.remove(i);
				}
			}
			if (list.size() == 10) {
				list.remove(0);
			}

			list.add(text);
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i) + ",");
		}
		save_Str = sb.toString();
		Log.i("ws", "拿到的history=" + save_Str);
		SharedPreferencesUtil.setSharedPreferences("history_search", "history",
				save_Str);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		/*
		 * saveHistory(suggestionList.get(position).getKeyword()); Intent intent
		 * = new Intent(); intent.setClass(this, ProductListActivity.class);
		 * intent.putExtra("search_from", "keyword"); intent.putExtra("keyword",
		 * suggestionList.get(position).getKeyword()); startActivity(intent);
		 */
	}

}
