package org.ekh.laboiteasons;

import org.ekh.adapter.ImageAdapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	GridView gridView;
	MediaPlayer mp        = null;

	static final String[] MOBILE_OS = new String[] { 
		"shoot", "hello","Windows", "Blackberry" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView)findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

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
