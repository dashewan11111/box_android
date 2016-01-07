package com.adult.android.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.adult.android.R;

public class MyEditText2 extends LinearLayout implements TextWatcher, OnEditorActionListener, View.OnClickListener {
	private OnEditNumberListener mOnAddReduceClickListener;
	private OnEditTextClickListener mOnEditTextClickListener;
	private EditText mEditText;

	public EditText getEditText() {
		return mEditText;
	}

	private ImageView bAdd;
	private ImageView bReduce;
	private String number = "1";
	private int maxNumber = -1;
	private Object tag;

	private int position1;
	private int position2;

	// 这里的构造一定是两个参数。
	public MyEditText2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean isValidNumber() {
		if (!TextUtils.isEmpty(number)) {
			try {
				Integer.parseInt(number);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	private void overrideSetText(String text) {
		mEditText.setText(text);
		mEditText.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.mynumedittext, this);
		mEditText = (EditText) findViewById(R.id.edt_num);
		mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEditText.setOnEditorActionListener(this);
		mEditText.setOnClickListener(this);
		mEditText.setFocusable(false);
		mEditText.setFocusableInTouchMode(false);

		bAdd = (ImageView) findViewById(R.id.btn_num_product_plus);
		bReduce = (ImageView) findViewById(R.id.btn_num_product_reduce);
		overrideSetText(number);
		mEditText.addTextChangedListener(this);
		bReduce.setEnabled(false);

		bAdd.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				if (null != v.getTag()) {
//					bReduce.setEnabled(false);
//					return;
//				}
				int tempNumber = Integer.parseInt(number);
				if (isValidNumber()) {
					if (tempNumber > 1) {
						bReduce.setEnabled(true);
					}
					if (-1 != maxNumber && tempNumber >= maxNumber) {
						if (mOnAddReduceClickListener != null) {
							mOnAddReduceClickListener.onMaxQty(position1, position2, v, maxNumber);
						}
						return;
					}

					tempNumber++;
					//number = String.valueOf(tempNumber);

					// overrideSetText(number);
					if (mOnAddReduceClickListener != null) {
						mOnAddReduceClickListener.onAddClick(position1, position2, v, tempNumber - 1, tempNumber,
								v.getTag());
					}
				} else {
					number = "1";
					// overrideSetText(number);
					if (mOnAddReduceClickListener != null) {
						mOnAddReduceClickListener.onAddClick(position1, position2, v, 0, 1, v.getTag());
					}
				}
				checkNum(number);
			}
		});

		bReduce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (null != v.getTag()) {
				// bReduce.setEnabled(false);
				// return;
				// }
				if (isValidNumber()) {
					int tempNumber = Integer.parseInt(number);
					if (tempNumber <= 1) {
						return;
					}
					if (tempNumber == 2) {
						bReduce.setEnabled(false);
					}
					tempNumber--;
					//number = String.valueOf(tempNumber);
					// overrideSetText(number);
					if (mOnAddReduceClickListener != null) {
						mOnAddReduceClickListener.onReduceClick(position1, position2, v, tempNumber + 1, tempNumber,
								v.getTag());
					}
				} else {
					number = "";
					// overrideSetText(number);
				}
				checkNum(number);
			}
		});
	}

	public OnEditNumberListener getOnAddReduceClickListener() {
		return mOnAddReduceClickListener;
	}

	@Override
	public void setTag(Object tag) {
		super.setTag(tag);
		this.tag = tag;
		bAdd.setTag(tag);
		bReduce.setTag(tag);
	}

	public void setOnAddReduceClickListener(OnEditNumberListener mOnAddReduceClickListener, int position1, int position2) {
		this.mOnAddReduceClickListener = mOnAddReduceClickListener;
		this.position2 = position2;
		this.position1 = position1;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			if (s == null || TextUtils.isEmpty(s.toString())) {
				number = "";
			} else {
				number = s.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			number = "";
		}
		if (isValidNumber()) {
			int tempNumber = Integer.parseInt(number);
			bAdd.setEnabled(true);
			if (tempNumber > 1) {
				bReduce.setEnabled(true);
			} else {
				bReduce.setEnabled(false);
			}
		} else {
			bAdd.setEnabled(false);
			bReduce.setEnabled(false);
		}
	}

	private void checkNum(CharSequence num) {
		if (num.toString().startsWith("0")) {
			number = num.toString().substring(1);
			overrideSetText(number);
			return;
		}
		if (0 < maxNumber) {
			if (!TextUtils.isEmpty(num.toString()) && maxNumber < Integer.parseInt(num.toString())) {
				number = "" + maxNumber;
			} else if (9999 < Integer.parseInt(num.toString())) {
				number = "9999";
			} else {
				number = num.toString();
			}
		} else {

		}
	}

	public void setNums(int num) {
		if (num < 0)
			return;
		number = String.valueOf(num);
		bAdd.setEnabled(true);
		if (num == 1 || num == 0) {
			bReduce.setEnabled(false);
		} else {
			bReduce.setEnabled(true);
		}
		overrideSetText(number);
	}

	public void setOnEditTextClickListener(OnEditTextClickListener l) {
		mOnEditTextClickListener = l;
	}

	public int getNums() {
		return isValidNumber() ? Integer.parseInt(number) : -1;
	}

	public void setEditable(boolean b) {
		bReduce.setEnabled(b);
		bAdd.setEnabled(b);
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	public static interface OnEditNumberListener {
		void onAddClick(int position1, int positio2, View v, int previousNum, int currentNum, Object tag);

		void onReduceClick(int position1, int position2, View v, int previousNum, int currentNum, Object tag);

		void onMaxQty(int position1, int position2, View v, int max);

		void onInputNumberDone(int position1, int position2, int number, int max, Object tag);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			if (isValidNumber() && mOnAddReduceClickListener != null) {
				mOnAddReduceClickListener.onInputNumberDone(position1, position2, Integer.parseInt(number), maxNumber,
						this.tag);
			}
			return true;
		}
		return false;
	}

	public static interface OnEditTextClickListener {
		void onClick(EditText editText, MyEditText2 myEditText);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edt_num:
			if (mOnEditTextClickListener != null) {
				mOnEditTextClickListener.onClick(mEditText, MyEditText2.this);
			}
			break;
		}
	}
}