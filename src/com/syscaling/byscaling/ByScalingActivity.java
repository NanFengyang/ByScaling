package com.syscaling.byscaling;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ByScalingActivity extends Activity implements OnPageChangeListener, OnTouchListener {

	private CustomViewPager viewPager;
	private List<ImageView> img = null;
	private RelativeLayout viewPagerContainer;
	private int mWidth;

	private TextView pageText;
	private int len = 0;
	private Handler mHandler = new Handler();
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mian_activity);
		viewPager = (CustomViewPager) findViewById(R.id.viewpager);
		Button but = (Button) findViewById(R.id.button1);
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem((index + 1) % 3);
			}
		});
		img = new ArrayList<ImageView>();
		for (int i = 0; i < 3; i++) {
			ImageView ii = new ImageView(this);
			ii.setBackgroundResource(R.drawable.xiaochaoren);
			img.add(ii);

		}

		viewPagerContainer = (RelativeLayout) findViewById(R.id.page_layout);
		setTime();
		viewPager.setAdapter(new Scaling());
		// 1.设置幕后item的缓存数目
		viewPager.setOffscreenPageLimit(3);
		viewPager.setCurrentItem(index);
		// 2.设置页与页之间的间距
		viewPager.setPageMargin(20);
		viewPagerContainer.setOnTouchListener(this);
		viewPager.setOnPageChangeListener(this);

		// timer.schedule(task, 2000, 2000);

	}

	private void setTime() {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScrolle scroller = new FixedSpeedScrolle(viewPager.getContext(),
					new AccelerateInterpolator());
			field.set(viewPager, scroller);
			scroller.setmDuration(1000);
		} catch (Exception e) {

		}
	}

	Timer timer = new Timer();
	TimerTask task = new TimerTask() {

		public void run() {
			// execute the task
			viewPager.setCurrentItem(++len % img.size());
		}

	};

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		// TODO Auto-generated method stub
		super.onWindowAttributesChanged(params);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		// mWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		// if (hasFocus) {
		// mWidth = (int) (mWidth / 3);
		// RelativeLayout.LayoutParams linearParams =
		// (RelativeLayout.LayoutParams) viewPager
		// .getLayoutParams();
		// linearParams.width = mWidth;
		// viewPager.setLayoutParams(linearParams);
		// }
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return viewPager.dispatchTouchEvent(event);
	}

	class Scaling extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// ((ViewPager) container).removeView(img.get(img.size()));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub

			try {
				ImageView imageView = img.get(position % 3);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				((ViewPager) container).addView(imageView);
				viewPager.setObjectForPosition(imageView, position);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return img.get(position % 3);
		}

	}

	public static Bitmap GetLocalOrNetBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(), 2 * 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 2 * 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		Log.i("TAG_page", "" + arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		if (viewPagerContainer != null) {
			viewPagerContainer.invalidate();
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		len = arg0;
		index = arg0;
	}

}
