package org.ekh.laboiteasons;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ekh.adapter.ImageAdapter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.res.AssetManager;
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
	String[] SOUNDS;
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
	
	private static final String TAG = "LaBoiteASons";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		parseXML();
		detector = new GestureDetector(this,this);
		view = (ViewFlipper)findViewById(R.id.flipper);

		int nb_sounds_per_page = 9;
		int current_length = SOUNDS.length;
		int nb_sounds_remaining = SOUNDS.length;
		
		//Count the pages
		nb_page = SOUNDS.length/nb_sounds_per_page;
		int reste = SOUNDS.length%nb_sounds_per_page;
		if(reste > 0){
			++nb_page;
		}
		
		for(int i=0; i<nb_page; ++i){
			//Calculate the current length of the sounds array of the current page
			//if it's the last page
			if(i == (nb_page-1)){
				current_length = nb_sounds_remaining;
				Log.v(TAG, "else if");
				
			}
			else if(nb_page > 1){
				current_length = nb_sounds_per_page; 
				nb_sounds_remaining -= nb_sounds_per_page;
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
					String soundString = ((TextView) v.findViewById(R.id.grid_item_label)).getText().toString();
					playThatSound(soundString);
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
	
	public static int getResId(String variableName, Class<?> c) {
	    Field field = null;
	    int resId = 0;
	    try {
	        field = c.getField(variableName);
	        try {
	            resId = field.getInt(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return resId;
	}
	
	protected void playThatSound(String theSoundString) {
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        if (Arrays.asList(SOUNDS).contains(theSoundString))
            mp = MediaPlayer.create(this, getResId(theSoundString, R.raw.class));
        mp.start();
    }
	
	private void parseXML() {
		AssetManager assetManager = getBaseContext().getAssets();
		try {
			InputStream is = assetManager.open("sound.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			XMLParser myXMLPasrer = new XMLParser();
			xr.setContentHandler(myXMLPasrer);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);
			
			detector = new GestureDetector(this,this);
			view = (ViewFlipper)findViewById(R.id.flipper);

			//TextView tv = new TextView(this);

			ArrayList<SoundInfo> soundList = myXMLPasrer.getSoundList();
			SOUNDS = new String[soundList.size()];
			int i = 0;
			for(SoundInfo soundInfo: soundList){			
				SOUNDS[i] = soundInfo.getNameSound();
				i++;
			}


			slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
			slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
			slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
			slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
			
			
			is.close();

		} catch (Exception e) {
			e.printStackTrace(); 
		}


	}

}
