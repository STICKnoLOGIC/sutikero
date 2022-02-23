/********************************************
Author: STICKnoLOGIC(John Aerial J. Azcune)
Email:  jobeh.llame@gmail.com
		j.azer.yality@gmail.com
		johnaerial.azcune@bicol-u.edu.ph
this File is under the MIT Licensed, please read
the license at the root of this repository.
********************************************/ 
package ph.STICKnoLOGIC.aerial.sutikero.provider;

import android.content.*;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;
import android.app.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.arthenica.ffmpegkit.*;

import ph.STICKnoLOGIC.aerial.sutikero.model.StickerPack;
import ph.STICKnoLOGIC.aerial.sutikero.model.Content;
import ph.STICKnoLOGIC.aerial.sutikero.*;

import com.google.gson.Gson;

public class FileContentProvider extends ContentProvider{
    
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final String STICKERS_ASSET="sticker_asset",
	CONTENTS="contents.json",
	DATA="data", //the source file or the file path / the Json String for Contents,Sticker PAcks and Stickers
	TO="to", //how to convert it (0=gif to wwewbp Animation, 1= gif to webp, 2= image to webp Animation and 3=Image to webp)
	auth="ph.STICKnoLOGIC.aerial.sutikero.provider.thirdparty";
	
	private static File f;
	
	//private AlertDialog.Builder dial;
	private static final int CONTENT_CODE=0,
	STICKER_CODE=1;
	
	
	@Override
	public boolean onCreate()
	{
		
		f=new File(FileUtil.getPackageDataDir(getContext())+"/"+STICKERS_ASSET);
		MATCHER.addURI(auth,STICKERS_ASSET+"/"+CONTENTS,0);
		for(StickerPack pack:new Gson().fromJson(StickerContentProvider.readContent(getContext()),Content.class).packs)
			MATCHER.addURI(auth,pack.identifier+"/*",STICKER_CODE);
		return true;
	}
	
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException
	{
		int code=MATCHER.match(uri);
		if(code!=CONTENT_CODE&&code!=STICKER_CODE)
		throw new UnsupportedOperationException("this uri is not supported for file provider");

		if(!f.exists())
		f.mkdir();
		
		File bago=new File(f,uri.getEncodedPath());
		
		if(!bago.exists())
			{
				try{
					bago.createNewFile();
				}catch(Exception e)
				{
				//errror creating directory/files
				}
			}
		
		int mde=0;
		if(code==CONTENT_CODE)
		{
			mde|= ParcelFileDescriptor.MODE_READ_ONLY;
			return ParcelFileDescriptor.open(bago,mde);
		}
		if(mode.contains("w")) mde|=ParcelFileDescriptor.MODE_WRITE_ONLY;
		
		if(mode.contains("r")) mde|= ParcelFileDescriptor.MODE_READ_ONLY;
		
		if(mode.contains("+")) mde |= ParcelFileDescriptor.MODE_APPEND;
		
		return ParcelFileDescriptor.open(bago,mde);
	}
	
	@Override
    public String getType(@NonNull Uri uri) {
		int code=MATCHER.match(uri);
		if(code!=CONTENT_CODE&&code!=STICKER_CODE)
		throw new UnsupportedOperationException("this uri is  supported for file provider");

		return code==CONTENT_CODE?"application/json":"image/webp";
		}
	
	@Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) 
						{
							throw new UnsupportedOperationException("this method is not supported for file provider");
						}
						
						
						
						
	  @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("this method is not supported for file provider");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("this method is not supported for file provider");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
		int code=MATCHER.match(uri);
		
		if(code!=CONTENT_CODE&&code!=STICKER_CODE)
        throw new UnsupportedOperationException("this method is not supported for file provider");
    	
		if(code==CONTENT_CODE)
		{
			StickerContentProvider.saveContent(getContext(),values.getAsString(DATA));
			//save conents/update some miniscule
			return 0;
		
		}
		else {
			int to=values.getAsInteger(TO);
			File src=new File(values.getAsString(DATA));
			File dest =new File(f,uri.getEncodedPath());
			
			if(dest.exists())
				{
					showReplace(src,dest,to);
					return 0;
				}
				
				return getResult(src,dest,to);
				//replace
		}
			
			
	}
		
	
	
	
	private synchronized int getResult(File src,File dest,int to)
	{
		FFmpegSession session= to==0? StickerContentProvider.Gif2WebpAnimation(getContext(),src,dest) : StickerContentProvider.Image2Webp(getContext(),src,dest);//= StickerContentProvider.Convert(src,dest);
	if(session==null)
	{
		pop(getContext(),"Filename already exist...");
		return 1;
	}
	
	if (ReturnCode.isSuccess(session.getReturnCode())) {
		pop(getContext(),"Successfullly Added...");
		return 0;}
	else if (ReturnCode.isCancel(session.getReturnCode())) {
        return 2;
    } else {
		//Failed to convert
        String s=( String.format("Command failed with state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));
		pop(getContext(),s);
		return 1;
   
    }
	}
	
	public static void pop(@NonNull Context c,final String message)
	{
		
		((Activity)c).runOnUiThread(new Runnable() {
    public void run() {
		AlertDialog.Builder dial= new AlertDialog.Builder(new android.view.ContextThemeWrapper(c, ph.STICKnoLOGIC.aerial.sutikero.R.style.DarkDialog));
		dial.setTitle("Sutikero Result...");
		dial.setMessage(message);
		dial.setPositiveButton("Okay",null);
		dial.create().show();
		}
});
	}
	
	
	public synchronized void showReplace(final File src, final File dest,final int to)
	{
		AlertDialog.Builder dial= new AlertDialog.Builder(new android.view.ContextThemeWrapper(getContext(), ph.STICKnoLOGIC.aerial.sutikero.R.style.DarkDialog));
		dial.setTitle("The File Name Already Exist...");
		dial.setMessage("do you want to overwrite the file?(this action can't be undone)");
		dial.setPositiveButton("OverWrite", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				dest.delete();
				getResult(src,dest,to);
				
			}
		});
		dial.setNegativeButton("Cancel",null);
		dial.create().show();
	}
}
