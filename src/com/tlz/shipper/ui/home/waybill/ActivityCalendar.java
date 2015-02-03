package com.tlz.shipper.ui.home.waybill;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;

public class ActivityCalendar extends ThemeActivity {
	private CalendarPickerView calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_waybill_calendar_picker);
		final boolean isSend = getIntent().getBooleanExtra("isSend", false);
		if (isSend) {
			mActionBar.setTitle(R.string.waybill_calendar_title);
		} else {
			mActionBar.setTitle(R.string.waybill_calendar_title1);
		}
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);
		final Calendar month = Calendar.getInstance();
		month.set(Calendar.DAY_OF_MONTH, 1);
		
//		final Calendar lastYear = Calendar.getInstance();
//		lastYear.add(Calendar.YEAR, -1);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		if(!isSend&&Myself.Send!=null)
		{
			calendar.init(Myself.Send, nextYear.getTime())
			.inMode(SelectionMode.SINGLE) 
			.withSelectedDate(new Date());
		}
		else
		{
			calendar.init(month.getTime(), nextYear.getTime()) 
			.inMode(SelectionMode.SINGLE) 
			.withSelectedDate(new Date());
		}

		findViewById(R.id.done_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
//						ToastUtils.show(CalendarActivity.this, calendar
//								.getSelectedDate().getTime());
						Intent intent = new Intent();
						SimpleDateFormat dateformat = new SimpleDateFormat(
								"yyyy年MM月dd日", Locale.CHINA);
						if (isSend) {
							Myself.SendString = dateformat.format(new Date(calendar
									.getSelectedDate().getTime()));
							intent.putExtra("content", Myself.SendString);
							Myself.Send=calendar.getSelectedDate();
						} else {

							Myself.ArriveString = dateformat.format(new Date(calendar
									.getSelectedDate().getTime()));

							intent.putExtra("content", Myself.ArriveString);
						}

						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}
}
