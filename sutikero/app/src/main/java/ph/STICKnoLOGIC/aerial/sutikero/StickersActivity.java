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
import android.content.Intent;
import android.content.ClipData;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import com.google.gson.Gson;
import com.arthenica.ffmpegkit.*;
import com.facebook.drawee.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.recyclerview.widget.*;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;
import ph.STICKnoLOGIC.aerial.sutikero.ui.*;
import ph.STICKnoLOGIC.aerial.sutikero.util.*;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageformat.*;
import com.facebook.binaryresource.*;
import com.facebook.cache.common.*;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.core.*;
import com.facebook.imagepipeline.request.*;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import ph.STICKnoLOGIC.aerial.sutikero.provider.*;
import ph.STICKnoLOGIC.aerial.sutikero.adapter.* ;

public class StickersActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICKER = 101;
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private  Content content;
	private double position = 0;
	private boolean animated = false;
	
	private LinearLayout linear1;
	private ImageView imageview1;
	private TextView textview1;
	private TextView textview2;
	private RecycleView recyclerview1;
	
	private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog.Builder dial;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.stickers);
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
		linear1 = findViewById(R.id.linear1);
		imageview1 = findViewById(R.id.imageview1);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		recyclerview1 = findViewById(R.id.recyclerview1);
		picker.setType("image/*");
		picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		dial = new AlertDialog.Builder(this);
	}
	
	private void initializeLogic() {
		SketchwareUtil.showMessage(getApplicationContext(), "Tap the item to preview,\nLong press to remove");
		setTitle("Add Stickers");
		//com.facebook.drawee.backends.pipeline.Fresco.initialize(this);
		dial=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DarkDialog));
		position = getIntent().getIntExtra("pos",0);;
		if (!StickerContentProvider.getContent(this).exists()) {
			SketchwareUtil.showMessage(getApplicationContext(), "no Data found, exiting...");
			finish();
		}
		else {
			content = new Gson().fromJson(StickerContentProvider.readContent(this),Content.class);
		}
		animated = content.packs.get((int)position).animatedStickerPack;
		recyclerview1.setLayoutManager(new GridLayoutManager(this, 4));
		textview1.setText(content.packs.get((int)position).name);
		textview2.setText(content.packs.get((int)position).publisher);
		imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(this)+"/"+StickerContentProvider.STICKERS_ASSET+"/".concat(content.packs.get((int)position).identifier).concat("/").concat(content.packs.get((int)position).trayImageFile), 1024, 1024));
		textview1.setCompoundDrawablesWithIntrinsicBounds(animated?R.drawable.play:0, 0, 0, 0);
		_initializeSticker();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if (_requestCode == BuildConfig.SEND_TWA) {
				if (_data!=null) {
						final String validationError = _data.getStringExtra("validation_error");
						                    if (validationError != null&&!validationError.equals(""))
						FileContentProvider.pop(StickersActivity.this,validationError);
				}
		}
		if (_requestCode == BuildConfig.TRANSFER_PACK) {
				try{
						if (_data!=null) {
								final String url=_data.getStringExtra("url");
								ImageRequest downloadRequest = ImageRequest.fromUri(Uri.parse(url));
								CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(downloadRequest,null);
								if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey))
									{
												BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
												final File cacheFile = ((FileBinaryResource) resource).getFile(),
												dest=new File(FileUtil.getPackageDataDir(StickersActivity.this)+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get((int)position).identifier+"/");
												
												FileInputStream fis = new FileInputStream(cacheFile);
												com.facebook.imageformat.ImageFormat imageFormat = ImageFormatChecker.getImageFormat(fis);
												
										switch(imageFormat.toString()) {
												case "GIF":
												case "WEBP_ANIMATED": {
														if (!animated) {
																_pop2("You can't add animated Sticker in Static Sticker Pack...");
																return;
														}
														_save2(cacheFile,dest);
														break;
												}
												case "UNKNOWN": {
														_pop2("Unknown Format...");
														break;
												}
												default: {
														if (animated) {
																_pop2("You can't add Static Sticker in Animated Sticker Pack...");
																return;
														}
														_save2(cacheFile,dest);
														break;
												}
										}
										
									}
						}
				}catch(Exception e){
						FileContentProvider.pop(StickersActivity.this,e.getMessage());
				}
		}
		switch (_requestCode) {
			case REQ_CD_PICKER:
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
				String name = Uri.parse(_filePath.get(0)).getLastPathSegment().replace(" ", "_").replace(".", "_").concat(".webp");
				String urls = _filePath.get((int)(0));
				if (urls.endsWith(".gif")) {
					if (animated) {
						_save(urls, name);
					}
					else {
						_pop2("You can't add animated sticker in Static Sticker Pack...");
					}
				}
				else {
					if (animated) {
						_pop2("You can't add static sticker in animated Sticker Pack...");
					}
					else {
						_save(urls, name);
					}
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		FileUtil.writeFile(getFilesDir().getAbsolutePath().concat("/").concat("contents.json"), new Gson().toJson(content));
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int _id = item.getItemId();
		final String _title = (String) item.getTitle();
		switch((int)_id) {
			case ((int)R.id.addS): {
				if (content.packs.get((int)position).stickers.size()>=30) {
					FileContentProvider.pop(StickersActivity.this,"Sticker Packshould not exceed 30 Stickers");
					return true;
				}
				com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(StickersActivity.this,R.style.SheetDialog);
				
				//view
				View view=StickersActivity.this.getLayoutInflater().inflate(R.layout.bottom_modal,null,false);
				dialog.setContentView(view);
				
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				
				view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						startActivityForResult(picker, REQ_CD_PICKER);
						dialog.dismiss();
					}
				});
				view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						Intent i=new Intent(StickersActivity.this,UrlActivity.class);
						startActivityForResult(i,BuildConfig.TRANSFER_PACK);
						dialog.dismiss();
					}
				});
				
				dialog.show();
				break;
			}
			case ((int)R.id.addWA): {
				String result = content.packs.get((int)position).validate(this);
				if (result.equals("success")) {
					StickerContentProvider.addTWA(this,content.packs.get((int)position).identifier,content.packs.get((int)position).name);
				}
				else {
					_pop2(result);
				}
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.addWA,menu);
		return super.onCreateOptionsMenu(menu);
	}
	public void _initializeSticker() {
		recyclerview1.setAdapter(new grid(listener,content.packs.get((int)position).stickers,content.packs.get((int)position).identifier));
	}
	private onClickItem listener = new onClickItem()
				{
									@Override
									public void Delete(int pos)
									{
											_Delete(pos);
									}
									@Override
									public void onClick(View v, Uri uri)
									{
														_fade(true);
														View view=getLayoutInflater().inflate(R.layout.add_sticker,null);
														SimpleDraweeView sticker=view.findViewById(R.id.sticker);
														DraweeController controller = Fresco.newDraweeControllerBuilder()
												                    .setUri(uri)
												                    .setAutoPlayAnimations(true)
																	.setOldController(sticker.getController())
												                    .build();
												    	sticker.setController(controller);
							PopupWindow mypopupWindow = new PopupWindow(view,250, 250, true);
														mypopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() 
															{
																
																                @Override
																                public void onDismiss() {
																									_fade(false);
																				                }
																            });
														mypopupWindow.showAsDropDown(v,-75,-225);
												
									}
									//delete bridge
				};
								{
	}
	
	
	public void _fade(final boolean _popShow) {
		recyclerview1.setAlpha((float)(_popShow?0.2:1));
	}
	
	
	public void _pop2(final String _msg) {
		FileContentProvider.pop(this,_msg);
	}
	
	
	public void _save(final String _url, final String _name) {
		new save(_url,_name).execute();
	}
	class save extends AsyncTask<Void, Void, Void> { 
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(StickersActivity.this,R.style.Progress);
			dialog.setMessage("Converting Image...");
			dialog.setTitle("Please Wait!!!");
			dialog.setCancelable(false); dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); dialog.show();
			return ;
		}
		String urls = "";
		String name = "";
		ArrayList<String> emoji=new ArrayList<>();
		FFmpegSession session;
		Sticker sticker;
		ProgressDialog dialog;
		public save(String url,String name)
		{
			emoji.add("😃");
			emoji.add("👍");
			urls=url;
			this.name=name;
			sticker=new Sticker(name,emoji);
		}
		protected Void doInBackground(Void... arg0) {
			
			session=animated? StickerContentProvider.Gif2WebpAnimation(StickersActivity.this,urls,name,content.packs.get((int)position).identifier) : StickerContentProvider.Image2Webp(StickersActivity.this,urls,name,content.packs.get((int)position).identifier);
			
			return null;
			 }
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if (session==null) {
				return;
			}
			if (ReturnCode.isSuccess(session.getReturnCode())) {
				try{
					content.packs.get((int)position).stickers.add(sticker);
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					
					StickerPackValidator.validateSticker(StickersActivity.this,content.packs.get((int)position).identifier,sticker,animated);
					
					
					
					double d = Double.parseDouble(content.packs.get((int)position).imageDataVersion);
					d++;
					content.packs.get((int)position).imageDataVersion = String.valueOf((long)(d));
					
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					_initializeSticker();
					_pop2("Successfully Added...");
				}catch(Exception e){
					content.packs.get((int)position).stickers.remove(sticker);
					FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get((int)position).identifier+"/"+sticker.imageFileName);
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					_pop2("Sticker can't be added to Sticker Pack:\n".concat(e.getMessage()));
				}
			}
			else {
				if (ReturnCode.isCancel(session.getReturnCode())) {
					
				}
				else {
					_pop2("Error Converting Image...");
				}
			}
							return ;
							    }
	}
	{
	}
	
	
	public void _Delete(final int _pos) {
		dial.setTitle("Permanently Delete Item?");
		dial.setMessage("this action can't be undone, do you want to continue?");
		dial.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		dial.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get((int)position).identifier+"/"+content.packs.get((int)position).stickers.get(_pos).imageFileName);
				
				content.packs.get((int)position).stickers.remove((int)(_pos));
				double d = Double.parseDouble(content.packs.get((int)position).imageDataVersion);
				d++;
				content.packs.get((int)position).imageDataVersion = String.valueOf((long)(d));
				StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
				_initializeSticker();
			}
		});
		dial.create().show();
	}
	class save2 extends AsyncTask<Void, Void, Void> { 
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(StickersActivity.this,R.style.Progress);
			dialog.setMessage("Converting Image...");
			dialog.setTitle("Please Wait!!!");
			dialog.setCancelable(false); dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); dialog.show();
			return ;
		}
		protected Void doInBackground(Void... arg0) {
			
			session=animated? StickerContentProvider.Gif2WebpAnimation(StickersActivity.this,a,new File(b,name)) : StickerContentProvider.Image2Webp(StickersActivity.this,a,new File(b,name));
			
			return null;
			 }
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if (session==null) {
				return;
			}
			if (ReturnCode.isSuccess(session.getReturnCode())) {
				try{
					content.packs.get((int)position).stickers.add(sticker);
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					
					StickerPackValidator.validateSticker(StickersActivity.this,content.packs.get((int)position).identifier,sticker,animated);
					
					
					
					double d = Double.parseDouble(content.packs.get((int)position).imageDataVersion);
					d++;
					content.packs.get((int)position).imageDataVersion = String.valueOf((long)(d));
					
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					_initializeSticker();
					_pop2("Successfully Added...");
				}catch(Exception e){
					content.packs.get((int)position).stickers.remove(sticker);
					FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+content.packs.get((int)position).identifier+"/"+sticker.imageFileName);
					StickerContentProvider.saveContent(StickersActivity.this,new Gson().toJson(content));
					_pop2("Sticker can't be added to Sticker Pack:\n".concat(e.getMessage()));
				}
			}
			else {
				if (ReturnCode.isCancel(session.getReturnCode())) {
					
				}
				else {
					_pop2("Error Converting Image...");
				}
			}
							return ;
							    }
		public save2(File s,File d)
		{
			emoji.add("😃");
			emoji.add("👍");
			a=s;
			b=d;
			
			name = Uri.parse(a.getPath()).getLastPathSegment().replace(".", "_").replace(" ", "_").concat(".webp");
			sticker=new Sticker(name,emoji);
		}
		File a,b;
		Sticker sticker;
		String name="";
		FFmpegSession session;
		ArrayList<String> emoji=new ArrayList<>();
		ProgressDialog dialog;
	}
	
	
	private void _save2(final File src, final File dest){
		new save2(src,dest).execute();
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