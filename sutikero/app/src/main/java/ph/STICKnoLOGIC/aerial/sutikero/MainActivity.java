package ph.STICKnoLOGIC.aerial.sutikero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.google.gson.Gson;
import com.arthenica.ffmpegkit.*;
import com.facebook.drawee.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.*;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;
import ph.STICKnoLOGIC.aerial.sutikero.ui.*;
import ph.STICKnoLOGIC.aerial.sutikero.provider.*;
import ph.STICKnoLOGIC.aerial.sutikero.adapter.* ;

public class MainActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private DrawerLayout _drawer;
	private HashMap<String, Object> Whole = new HashMap<>();
	private String fileUri = "";
	private  Content content;
	
	private RecycleView recyclerview1;
	private LinearLayout _drawer_linear2;
	private ImageView _drawer_imageview1;
	private TextView _drawer_textview1;
	private TextView _drawer_textview2;
	private LinearLayout _drawer_linear3;
	private Button _drawer_button2;
	private Button _drawer_button3;
	
	private AlertDialog.Builder dial;
	private SharedPreferences memory;
	private Intent d = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		_drawer = findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = findViewById(R.id._nav_view);
		
		recyclerview1 = findViewById(R.id.recyclerview1);
		_drawer_linear2 = _nav_view.findViewById(R.id.linear2);
		_drawer_imageview1 = _nav_view.findViewById(R.id.imageview1);
		_drawer_textview1 = _nav_view.findViewById(R.id.textview1);
		_drawer_textview2 = _nav_view.findViewById(R.id.textview2);
		_drawer_linear3 = _nav_view.findViewById(R.id.linear3);
		_drawer_button2 = _nav_view.findViewById(R.id.button2);
		_drawer_button3 = _nav_view.findViewById(R.id.button3);
		dial = new AlertDialog.Builder(this);
		memory = getSharedPreferences("memory", Activity.MODE_PRIVATE);
		
		_drawer_linear2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		_drawer_button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dial=new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.DarkDialog));
				dial.setIcon(R.drawable.sutikero);
				dial.setTitle("About Sutikero");
				dial.setMessage("Yet another WhatsApp Sticker App for Android that is based on WhatsApp/Sticker gitthub repo.\n\nTL:DR\nThis App(Sutikero) is not Affiliate of WhatsApp or its company\n\nWhat is Sutikero?\n[] Sutikero is an app that is dedicated to add Stickers/Sticker Pack to WhatsApp or Convert image from either file or internet url to Stickers to be added in Sticker Pack.\n\nWhy Sutikero?\n[] Sutikero is a ui and user friendly\n[] Sticker Pack and Sticker are easy to manage\n[] you can add your custom images as Stickers\n[] you can add stickers from File (Storage or Album)\n[] you can add stickers from a URL\n[] open-source (of course!)\n[] no Ads\n[] F-R-E-E-!\n\nWhy not Sutikero?\n[] minimum of Android 6 (Marshmallow) for operating system\n[] not for Iphone/Apple/Mac\n[] its in Beta phase\n[] you may face some bugs that is different for each phone (send us your bug report/open an issue in our github)\n[] the quality of sticker will be 20% or less than the original quality (since there is a limit size)\n[] if you're planning to add you're taken picture as sticker, the picture will be resized but keep the aspect ratio (it can resize at max 512x512)(we recommend to edit/square crop the image or add image that have 1:1 aspect ratio)\n[] once you uninstall the app, the stickers you've added in WhatsApp will be removed (you can't backup your sticker)\n\nThird Party Library used:\n[] Facebook Fresco\n[] FFmpeg toolkit-Android\n[] AndroidX\n[] Google Gson\n\nTODO:\n[] Add backup/import feature\n[] optimize the code for fast source loading\n[] image quality changer\n\nLove my work?\n[] you can buy me a Coffee or sweets\n[] no money? you can send me a (love) Letter (just kidding)\n[] you are free to fork this repo\n\nHate my work?\n[] you may open an issue in gitthub repo\n[] please avoid hate speech only constructive criticism\n[] C-O-N-S-T-R-U-C-T-I-V-E C-R-I-T-I-C-I-S-M !\n");
				dial.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dial.create().show();
			}
		});
		
		_drawer_button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				d.setClass(getApplicationContext(), DevActivity.class);
				startActivity(d);
			}
		});
	}
	
	private void initializeLogic() {
		_drawer_textview1.setText("Sutikero");
		_drawer_imageview1.setImageResource(R.drawable.sutikero);
		dial=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DarkDialog));
		if (StickerContentProvider.getContent(getApplicationContext()).exists()) {
			content = new Gson().fromJson(StickerContentProvider.readContent(this),Content.class);
		}
		else {
			try{
				StickerContentProvider.getContent(getApplicationContext()).createNewFile();
				content=new  Content(BuildConfig.ps,BuildConfig.ios,new ArrayList<StickerPack>());
				StickerContentProvider.saveContent(getApplicationContext(),new Gson().toJson(content));
			}catch(Exception e){
				SketchwareUtil.showMessage(getApplicationContext(), e.toString());
			}
		}
		recyclerview1.setLayoutManager(new LinearLayoutManager(this));
		_initialize_packs();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		try{
				if ((_requestCode == BuildConfig.TRANSFER_PACK) && (_resultCode == BuildConfig.CATCH_PACK)) {
						String s=_data.getStringExtra("use");
						StickerPack pks=_data.getParcelableExtra("pack");
						
						if(pks==null)
						{
								FileContentProvider.pop(MainActivity.this,"Cant add Invalid Sticker Pack...");
								return;
						}
						
						if(content.packs.contains(pks))
						{
									FileContentProvider.pop(MainActivity.this,"Sticker Pack Already exist...");
								return;
						}
						if (s.equals("edit")) {
								File n=(new File(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get(_data.getIntExtra("pos",0)).identifier));
								if(!n.exists())
								n.mkdir();
								n.renameTo(new File(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pks.identifier
								));
								(new File(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pks.identifier+"/"+content.packs.get(_data.getIntExtra("pos",0)).trayImageFile)).renameTo(new File(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pks.identifier+"/"+pks.trayImageFile
								));
								
								content.packs.remove(_data.getIntExtra("pos",0));
								content.packs.add(_data.getIntExtra("pos",0),pks);
						}
						else {
								content.packs.add(pks);
						}
						StickerContentProvider.saveContent(this,new Gson().toJson(content));
						_initialize_packs();
				}
				if (_requestCode == BuildConfig.SEND_TWA) {
						if (_data!=null) {
								final String validationError = _data.getStringExtra("validation_error");
								                    if (validationError != null&&!validationError.equals(""))
								FileContentProvider.pop(MainActivity.this,validationError);
						}
				}
		}catch(Exception e){
				SketchwareUtil.showMessage(getApplicationContext(), e.toString());
		}
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		StickerContentProvider.saveContent(this,new Gson().toJson(content));
		com.facebook.drawee.backends.pipeline.Fresco.getImagePipeline().clearCaches();
	}
	
	@Override
	public void onBackPressed() {
		dial.setTitle("sutikero");
		dial.setMessage("Do you want to Close this app?");
		dial.setPositiveButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				StickerContentProvider.saveContent(MainActivity.this,new Gson().toJson(content));
				finishAffinity();
			}
		});
		dial.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		dial.create().show();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (StickerContentProvider.getContent(getApplicationContext()).exists()) {
			content = new Gson().fromJson(StickerContentProvider.readContent(this),Content.class);
		}
		else {
			try{
				StickerContentProvider.getContent(getApplicationContext()).createNewFile();
				content=new  Content(BuildConfig.ps,BuildConfig.ios,new ArrayList<StickerPack>());
				StickerContentProvider.saveContent(getApplicationContext(),new Gson().toJson(content));
			}catch(Exception e){
				SketchwareUtil.showMessage(getApplicationContext(), e.toString());
			}
		}
		_initialize_packs();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main,menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int _id = item.getItemId();
		final String _title = (String) item.getTitle();
		Intent i=new Intent(MainActivity.this,CreateEditActivity.class);
		i.putExtra("use","create");
		startActivityForResult(i,BuildConfig.TRANSFER_PACK);
		return super.onOptionsItemSelected(item);
	}
	public void _initialize_packs() {
		if (content==null||content.packs.size()<=0) {
			SketchwareUtil.showMessage(getApplicationContext(), "No packs found");
			recyclerview1.setAdapter(null);
			return;
		}
		recyclerview1.setAdapter(new Packs(content.packs,new Packs.Bridge()
		{
			@Override
			public void Options(int pos,View vs)
			{
				pop(vs,pos);
			}
			
			@Override
			public void Tapped(int pos)
			{
				StickerContentProvider.saveContent(MainActivity.this,new Gson().toJson(content));
							d.setAction(Intent.ACTION_VIEW);
							d.putExtra("pos", pos);
							d.setClass(getApplicationContext(), StickersActivity.class);
							startActivity(d);
			}
		}));
	}
	
	
	public void _misc() {
		ImageView im = new ImageView(this);
		SketchwareUtil.showMessage(getApplicationContext(), new Gson().toJson(content));
	}
	
	
	private void pop(View v,int pos) {
			Context wrapper = new ContextThemeWrapper(this, R.style.yhal_pm);
		    PopupMenu popup = new PopupMenu(wrapper, v);
		    // Inflate the menu from xml
		    popup.inflate(R.menu.item);
		    // Setup menu item selection
		    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
				            switch (item.getItemId()) {
					            case R.id.add:
					              _set(pos);
					              return true;
					            case R.id.delete:
					              
								  
					dial.setTitle("Remove Sticker Packs?");
					dial.setMessage("you can't undo this action, do you want to remove it?");
					dial.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get((int)pos).identifier);
							
							
							content.packs.remove(pos);
							_initialize_packs();
						}
					});
					dial.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					dial.create().show();
					return true;
								case R.id.edit:
					Intent i=new Intent(MainActivity.this,CreateEditActivity.class);
					i.putExtra("use","edit");
					i.putExtra("pos",pos);
					i.putExtra("pack",content.packs.get(pos));
					startActivityForResult(i,BuildConfig.TRANSFER_PACK);
					
					              
								  return true;
					            default:
					              return false;
					            }
				        }
			    });
		    // Handle dismissal with: popup.setOnDismissListener(...);
		    // Show the menu
		    popup.show();
		
	}
	
	
	public void _set(final double _pos) {
		StickerContentProvider.saveContent(this,new Gson().toJson(content));
		
		String result = content.packs.get((int)_pos).validate(this);
		if (result.equals("success")) {
			StickerContentProvider.addTWA(this,content.packs.get((int)_pos).identifier,content.packs.get((int)_pos).name);
		}
		else {
			Toast.makeText(this,result,3000).show();
			
			
		}
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