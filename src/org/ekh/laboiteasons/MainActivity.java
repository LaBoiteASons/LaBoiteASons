package org.ekh.laboiteasons;

import org.ekh.adapter.ImageAdapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
	
	GridView gridView;
	static final String[] SOUNDS = new String[] { 
		"shoot", "hello", "Windows", "Blackberry" };
	
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
	
	private int nb_page;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		detector = new GestureDetector(this,this);
		view = (ViewFlipper)findViewById(R.id.flipper);
		
		//Count the pages
		int nb_sounds_per_page = 2;
		nb_page = SOUNDS.length/nb_sounds_per_page;
		int reste = SOUNDS.length%nb_sounds_per_page;
		if(reste > 0){
			++nb_page;
		}

		for(int i=0; i<nb_page; ++i){
			
			int current_length;
			int nb_sounds_in = SOUNDS.length;
			
			//Calculate the current length of the sounds array of the current page
			if(nb_page == 1){
				current_length = SOUNDS.length; 
			}
			else if(i == nb_page){
				current_length = nb_sounds_in;
			}
			else{
				current_length = nb_sounds_per_page; 
				nb_sounds_in -= nb_sounds_per_page;
			}
			
			//Fill the sounds array
			String[] sounds = new String[current_length];
			for(int j=0; j<current_length; ++j){
				sounds[j] = SOUNDS[j*(i+1)];
			}
			
			//Call the correct gridView in other words the correct page
			switch(i){
				case 0:
					gridView = (GridView) findViewById(R.id.gridView1);
					break;
				case 1:
					gridView = (GridView) findViewById(R.id.gridView2);
					break;
			}

			//Fill the gridView
			gridView.setAdapter(new ImageAdapter(this, sounds));
			
			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(
							getApplicationContext(),
							((TextView) v.findViewById(R.id.grid_item_label))
							.getText(), Toast.LENGTH_SHORT).show();
					playThatSound("hello");
				}
			});
			
			gridView.setOnTouchListener(new OnTouchListener(){
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        return detector.onTouchEvent(event);
			    }
			});
		}
		
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
		if(nb_page>1){
			if(Math.abs(e1.getY()-e2.getY()) > 250) return false;
			
			if((e1.getX()-e2.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
				view.setInAnimation(slideLeftIn);
				view.setOutAnimation(slideLeftOut);
				view.showNext();
			}
			else if((e2.getX()-e1.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
				view.setInAnimation(slideRightIn);
				view.setOutAnimation(slideRightOut);
				view.showPrevious();
			}
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
