package com.squaddigital.securityApp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.util.TimeView.TimeTextView;


public abstract class DateSlider extends Dialog {

	
	protected OnDateSetListener onDateSetListener;
	protected Calendar mTime;
	protected TextView mTitleText;
	protected List<ScrollLayout> mScrollerList = new ArrayList<ScrollLayout>();
	protected LinearLayout mLayout;
	
	public DateSlider(Context context, OnDateSetListener l, Calendar calendar) {
		super(context);
		this.onDateSetListener = l;
		mTime = Calendar.getInstance();
		mTime.setTimeInMillis(calendar.getTimeInMillis());
	}
	
	/**
	 * Set up the dialog with all the views and their listeners
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState!=null) {
			long time = savedInstanceState.getLong("time", mTime.getTimeInMillis());
			mTime.setTimeInMillis(time);
		}
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dateslider);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialogtitle);
		
		mTitleText = (TextView) this.findViewById(R.id.dateSliderTitleText);
		mLayout = (LinearLayout)findViewById(R.id.dateSliderMainLayout);

		Button okButton = (Button) findViewById(R.id.dateSliderOkButton);
		okButton.setOnClickListener(okButtonClickListener);

		Button cancelButton = (Button) findViewById(R.id.dateSliderCancelButton);
		cancelButton.setOnClickListener(cancelButtonClickListener);
		
		arrangeScroller(null);
	}
	
	private android.view.View.OnClickListener okButtonClickListener = new android.view.View.OnClickListener() {
		public void onClick(View v) {
			if (onDateSetListener!=null)
				onDateSetListener.onDateSet(DateSlider.this, mTime);
			dismiss();
		}
	};
	
	private android.view.View.OnClickListener cancelButtonClickListener = new android.view.View.OnClickListener() {
		public void onClick(View v) {
			dismiss();
		}
	};
	
	/**
	 * Sets the Scroll listeners for all ScrollLayouts in "mScrollerList"
	 */
	protected void setListeners() {
		for (final ScrollLayout sl: mScrollerList) {
			sl.setOnScrollListener(
					new ScrollLayout.OnScrollListener() {		
						public void onScroll(long x) {
							mTime.setTimeInMillis(x);
							arrangeScroller(sl);
						}
					});
		}
		
	}
	
	@Override
	public Bundle onSaveInstanceState() {
		Bundle savedInstanceState = super.onSaveInstanceState();
		if (savedInstanceState==null) savedInstanceState = new Bundle();
		savedInstanceState.putLong("time", mTime.getTimeInMillis());
		return savedInstanceState;	  
	}
	
	protected void arrangeScroller(ScrollLayout source) {
		setTitle();
		if (source!=null) {
			for (ScrollLayout scroller: mScrollerList) {
				if (scroller==source) continue;
				scroller.setTime(mTime.getTimeInMillis(),0);
			}
		}
	}
	
	/**
	 * This method sets the title of the dialog
	 */
	protected void setTitle() {
		if (mTitleText != null) {
			mTitleText.setText(getContext().getString(R.string.dateSliderTitle) + 
					String.format(": %te. %tB %tY", mTime,mTime,mTime)); 
		}
	}
	
	
	/**
	 * Defines the interface which defines the methods of the OnDateSetListener
	 */
	public interface OnDateSetListener {
		/**
		 * this method is called when a date was selected by the user
		 * @param view			the caller of the method
		 * 
		 */
		public void onDateSet(DateSlider view, Calendar selectedDate);
	}
	
	/**
	 * This class has the purpose of telling the corresponding scroller, which values make up
	 * a single TimeTextView element.   
	 *
	 */
	public abstract class Labeler {		

		/**
		 * gets called once, when the scroller gets initialised
		 * @param time
		 * @return the TimeObject representing "time"
		 */
		public TimeObject getElem(long time) { 
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			return timeObjectfromCalendar(c);
		}
		
		/**
		 * returns a new TimeTextView instance, is only called a couple of times in the
		 * initialisation process
		 * 
		 * @param context
		 * @param isCenterView is true when the view is the central view
		 * @return 
		 */
		public TimeView createView(Context context, boolean isCenterView) {
			return new TimeTextView(context, isCenterView, 25);
		}
		
		/**
		 * This method will be called constantly, whenever new date information is required
		 * it receives a timestamps and adds "val" time units to that time and returns it as
		 * a TimeObject
		 * 
		 * @param time
		 * @param val
		 * @return
		 */
		public abstract TimeObject add(long time, int val);
		protected abstract TimeObject timeObjectfromCalendar(Calendar c);
	}
	
	/**
	 * Very simple helper class that defines a time unit with a label (text) its start-
	 * and end date 
	 *
	 */
	public static class TimeObject {
		public final CharSequence text;
		public final long startTime, endTime;
		public TimeObject(final CharSequence text, final long startTime, final long endTime) {
			this.text = text;
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}
}
