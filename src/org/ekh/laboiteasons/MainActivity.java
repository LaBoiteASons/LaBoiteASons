package org.ekh.laboiteasons;

import org.ekh.adapter.ImageAdapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnGestureListener {
	
	GridView gridView1;
	GridView gridView2;
	static final String[] MOBILE_OS_1 = new String[] { 
		"shoot", "hello" };
	static final String[] MOBILE_OS_2 = new String[] { 
		"Windows", "Blackberry" };
	
	MediaPlayer mp = null;
	
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	
	private GestureDetector detector;
	private ViewFlipper view;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		detector = new GestureDetector(this,this);
		view = (ViewFlipper)findViewById(R.id.flipper);

		gridView1 = (GridView) findViewById(R.id.gridView1);
		gridView2 = (GridView) findViewById(R.id.gridView2);

		gridView1.setAdapter(new ImageAdapter(this, MOBILE_OS_1));
		gridView2.setAdapter(new ImageAdapter(this, MOBILE_OS_2));

		gridView1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((TextView) v.findViewById(R.id.grid_item_label))
						.getText(), Toast.LENGTH_SHORT).show();
				playThatSound("hello");
			}
		});
		gridView2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((TextView) v.findViewById(R.id.grid_item_label))
						.getText(), Toast.LENGTH_SHORT).show();
				playThatSound("hello");

			}
		});
		
		gridView1.setOnTouchListener(new OnTouchListener(){
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        return detector.onTouchEvent(event);
		    }
		});
		gridView2.setOnTouchListener(new OnTouchListener(){
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        return detector.onTouchEvent(event);
		    }
		});
		
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if(Math.abs(e1.getY()-e2.getY()) > 250) return false;
		
		if((e1.getX()-e2.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
			view.setInAnimation(slideLeftIn);
			view.setOutAnimation(slideLeftOut);
			view.showNext();
		}else if((e2.getX()-e1.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
			view.setInAnimation(slideRightIn);
			view.setOutAnimation(slideRightOut);
			view.showPrevious();
		}
			
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void playThatSound(String theSoundString) {
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        if (theSoundString == "shoot")
            mp = MediaPlayer.create(this, R.raw.shoot);
        else if (theSoundString == "hello")
        	mp = MediaPlayer.create(this, R.raw.hello);
        mp.start();
    }
}
