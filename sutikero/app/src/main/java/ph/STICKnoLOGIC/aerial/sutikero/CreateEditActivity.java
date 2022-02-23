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
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.content.Intent;
import android.content.ClipData;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import com.arthenica.ffmpegkit.*;
import com.facebook.drawee.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;

import ph.STICKnoLOGIC.aerial.sutikero.provider.*;

public class CreateEditActivity extends AppCompatActivity {
	
	public final int REQ_CD_OICK = 101;
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private String url = "";
	private boolean use = false;
	private  StickerPack pack;
	private double version = 0;
	private String file = "";
	
	private ScrollView vscroll2;
	private LinearLayout linear5;
	private ImageView imageview1;
	private Button button2;
	private TextView textview3;
	private EditText edittext1;
	private TextView textview4;
	private EditText edittext2;
	private CheckBox checkbox1;
	private Button button1;
	
	private Intent oick = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog.Builder showDialog;
	private Intent pass = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.create_edit);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
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
		vscroll2 = findViewById(R.id.vscroll2);
		linear5 = findViewById(R.id.linear5);
		imageview1 = findViewById(R.id.imageview1);
		button2 = findViewById(R.id.button2);
		textview3 = findViewById(R.id.textview3);
		edittext1 = findViewById(R.id.edittext1);
		textview4 = findViewById(R.id.textview4);
		edittext2 = findViewById(R.id.edittext2);
		checkbox1 = findViewById(R.id.checkbox1);
		button1 = findViewById(R.id.button1);
		oick.setType("image/*");
		oick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		showDialog = new AlertDialog.Builder(this);
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(oick, REQ_CD_OICK);
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_save();
			}
		});
	}
	
	private void initializeLogic() {
		showDialog=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DarkDialog));
		String tmp=getIntent().getStringExtra("use");
		use = tmp.equals("edit");
		version = 1;
		setTitle(use?"Edit Sticker Pack":"Create StickerPack");
		if (use) {
			pack=getIntent().getParcelableExtra("pack");
			edittext1.setText(pack.name);
			edittext2.setText(pack.publisher);
			button2.setText("Update");
			button1.setText("Update Sticker Pack");
			version = Integer.parseInt(pack.imageDataVersion);
			file = pack.trayImageFile;
			checkbox1.setChecked(pack.animatedStickerPack);
			imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pack.identifier+"/"+pack.trayImageFile, 1024, 1024));
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_OICK:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				url = _filePath.get((int)(0));
				file = _filePath.get((int)(0));
				if (url.endsWith(".gif")) {
					FileContentProvider.pop(CreateEditActivity.this,"Tray Icon doesn't support gif/animated Images");
					imageview1.setImageResource(R.drawable.default_image);
					url = "";
					return;
				}
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(url, 1024, 1024));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	
	@Override
	public void onBackPressed() {
		showDialog.setTitle("Leaving this Page");
		showDialog.setMessage("Do you want to save changes?");
		showDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				_save();
				pass.putExtra("pack", pack);
				pass.putExtra("use", "");
				if (use) {
					pass.putExtra("pos", getIntent().getIntExtra("pos",0));
					pass.putExtra("use", "edit");
				}
				setResult(BuildConfig.CATCH_PACK,pass);
				finish();
			}
		});
		showDialog.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				finish();
			}
		});
		StickerPack p= new StickerPack(edittext1.getText().toString().replace(" ","_").replace(".","_"),edittext1.getText().toString(),edittext2.getText().toString(),url,BuildConfig.email,BuildConfig.website,BuildConfig.policy,BuildConfig.term,String.valueOf((long)(version)),false,checkbox1.isChecked());
		p.setAndroidPlayStoreLink(BuildConfig.ps);
		p.setIosAppStoreLink(BuildConfig.ios);
		if (pack!=null&&pack.equals(p)) {
			super.onBackPressed();
		}
		else {
			if (pack!=null&&!pack.equals(p)) {
				showDialog.create().show();
			}
			else {
				if (pack==null) {
					super.onBackPressed();
				}
			}
		}
	}
	public void _save() {
		if (edittext1.getText().toString().trim().equals("")) {
			SketchwareUtil.showMessage(getApplicationContext(), "Title cant be empty");
			return;
		}
		if (edittext2.getText().toString().trim().equals("")) {
			SketchwareUtil.showMessage(getApplicationContext(), "Author name cant be empty");
			return;
		}
		if (file.trim().equals("")) {
			SketchwareUtil.showMessage(getApplicationContext(), "please add your tray Icon");
			return;
		}
		try{
			String vers = String.valueOf((long)(version));
			String name = edittext1.getText().toString().replace(".","_").replace(" ","_");
			if (pack==null) {
				pack= new StickerPack(edittext1.getText().toString().replace(" ","_").replace(".","_"),edittext1.getText().toString(),edittext2.getText().toString(),name+".png",BuildConfig.email,BuildConfig.website,BuildConfig.policy,BuildConfig.term,vers,false,checkbox1.isChecked());
				pack.setAndroidPlayStoreLink(BuildConfig.ps);
				pack.setIosAppStoreLink(BuildConfig.ios);
				
				(new File(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pack.identifier)).mkdir();
			}
			else {
				if (pack.animatedStickerPack!=checkbox1.isChecked()) {
					if (pack.stickers.size()>0) {
						SketchwareUtil.showMessage(getApplicationContext(), "You can't change the pack from "+(pack.animatedStickerPack?"Animated pack to Static pack ":"Static pack to Animated pack ")+"\nPlease remove first the stickers...");
						return;
					}
					else {
						pack.animatedStickerPack=checkbox1.isChecked();
					}
				}
				pack.name=edittext1.getText().toString();
				pack.identifier=name;
				pack.trayImageFile=name+".png";
				pack.publisher=edittext2.getText().toString();
			}
			if (!url.equals("")) {
				FileUtil.resizeBitmapFileRetainRatio(url, FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+pack.identifier+"/"+name+".png", 96);
			}
			pass.putExtra("pack", pack);
			pass.putExtra("use", "");
			if (use) {
				pass.putExtra("pos", getIntent().getIntExtra("pos",0));
				pass.putExtra("use", "edit");
			}
			setResult(BuildConfig.CATCH_PACK,pass);
			finish();
		}catch(Exception e){
			SketchwareUtil.showMessage(getApplicationContext(), e.toString());
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