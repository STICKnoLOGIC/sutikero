package ph.STICKnoLOGIC.aerial.sutikero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.arthenica.ffmpegkit.*;
import com.facebook.drawee.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;

public class DevActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	
	private LinearLayout linear1;
	private ImageView imageview2;
	private TextView textview2;
	private LinearLayout linear2;
	private ScrollView vscroll2;
	private LinearLayout linear3;
	private ImageView imageview3;
	private ImageView imageview4;
	private ImageView imageview5;
	private TextView textview4;
	private Button button3;
	private Button button4;
	
	private Intent go = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.dev);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear1 = findViewById(R.id.linear1);
		imageview2 = findViewById(R.id.imageview2);
		textview2 = findViewById(R.id.textview2);
		linear2 = findViewById(R.id.linear2);
		vscroll2 = findViewById(R.id.vscroll2);
		linear3 = findViewById(R.id.linear3);
		imageview3 = findViewById(R.id.imageview3);
		imageview4 = findViewById(R.id.imageview4);
		imageview5 = findViewById(R.id.imageview5);
		textview4 = findViewById(R.id.textview4);
		button3 = findViewById(R.id.button3);
		button4 = findViewById(R.id.button4);
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				go.setData(Uri.parse("https://www.facebook.com/STICKnoLOGIC"));
				go.setAction(Intent.ACTION_VIEW);
				startActivity(Intent.createChooser(go, "Open with"));
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				go.setAction(Intent.ACTION_VIEW);
				go.setData(Uri.parse("https://www.github.com/STICKnoLOGIC"));
				startActivity(Intent.createChooser(go, "Open with"));
			}
		});
		
		imageview5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "coming soon!!");
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				go.setData(Uri.parse("https://www.buymeacoffee.com/STICKnoLOGIC"));
				go.setAction(Intent.ACTION_VIEW);
				startActivity(Intent.createChooser(go, "Open with"));
			}
		});
		
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				go.setAction(Intent.ACTION_VIEW);
				go.setData(Uri.parse("https://www.paypal.me/yhalSTICKnoLOGIC"));
				startActivity(Intent.createChooser(go, "Open with"));
			}
		});
	}
	
	private void initializeLogic() {
		setTitle("About Developer");
		textview2.setText("STICKKnoLOGIC");
		textview4.setText("This Developer is Lazy Enough To Edit His Profile.\n\nPlease Understand his circumstance and Judge Him By His Work Thru Clicking The FACEBOOK and GITHUB widget above, underneath his name.\n\nFUN FACT:\nThis Developer is Coffeeholic and This APP were created using android phone,so DONT LOSE HOPE FOR THOSE ASPIRING DEVELOPERS THAT STRUGGLING BECAUSE OF THEIR RESOURCES!");
		imageview2.setImageResource(R.drawable.dev_logo);
		imageview3.setImageResource(R.drawable.fB);
		imageview4.setImageResource(R.drawable.gH);
		imageview5.setImageResource(R.drawable.discord);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}