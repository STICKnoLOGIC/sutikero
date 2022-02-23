package ph.STICKnoLOGIC.aerial.sutikero.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.app.Activity;
import android.database.MatrixCursor;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import com.arthenica.ffmpegkit.*;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;



import ph.STICKnoLOGIC.aerial.sutikero.model.*;
import ph.STICKnoLOGIC.aerial.sutikero.*;
import ph.STICKnoLOGIC.aerial.sutikero.util.*;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class StickerContentProvider extends ContentProvider {


    /**
     * Do not change the strings listed below, as these are used by WhatsApp. And changing these will break the interface between sticker app and WhatsApp.
     */
    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
	public static final String IMAGE_DATA_VERSION = "image_data_version";
    public static final String AVOID_CACHE = "whatsapp_will_not_cache_stickers";
	public static final String STICKER_FILE_ANIMATED = "is_animated_sticker";
    public static final String ANIMATED_STICKER_PACK = "animated_sticker_pack";

    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    public static final String CONTENT_FILE_NAME = "contents.json";

    public static final String CONTENT_SCHEME = "content";
    private static final String TAG = StickerContentProvider.class.getSimpleName();
    public static Uri AUTHORITY_URI = new Uri.Builder().scheme(StickerContentProvider.CONTENT_SCHEME).authority(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.METADATA).build();

    /**
     * Do not change the values in the UriMatcher because otherwise, WhatsApp will not be able to fetch the stickers from the ContentProvider.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static final String METADATA = "metadata";
    private static final int METADATA_CODE = 1;

    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;

    public static final String STICKERS = "stickers";
    public static final int STICKERS_CODE = 3;

    public static final String STICKERS_ASSET =ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.path;// "stickers_asset";
    private static final int STICKERS_ASSET_CODE = 4;

    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;

    private List<StickerPack> stickerPackList = new ArrayList<>();
	
	private SharedPreferences memory;



    @Override
    public boolean onCreate() {
        //Hawk.init(getContext()).build();
        final String authority = ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith(Objects.requireNonNull(getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }
		memory=getContext().getSharedPreferences("memory",0);
        //the call to get the metadata for the sticker packs.
        MATCHER.addURI(authority, METADATA, METADATA_CODE);

        //the call to get the metadata for single sticker pack. * represent the identifier
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);

        //gets the list of stickers for a sticker pack, * respresent the identifier.
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);

        for (StickerPack stickerPack : getStickerPackList()) {
            Log.e(TAG, "onCreate: " + stickerPack.identifier);
            MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile, STICKER_PACK_TRAY_ICON_CODE);
            if (stickerPack.getStickers() != null) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName, STICKERS_ASSET_CODE);
                }

            }
					//not yet added file path
		MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/*", STICKERS_ASSET_CODE);
        }
		

        return true;
    }
	
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        Log.d(TAG, "query: " + code + uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        MATCHER.match(uri);
        final int matchCode = MATCHER.match(uri);
        final List<String> pathSegments = uri.getPathSegments();
        Log.d(TAG, "openFile: " + matchCode + uri + "\n" + uri.getAuthority()
                + "\n" + pathSegments.get(pathSegments.size() - 3) + "/"
                + "\n" + pathSegments.get(pathSegments.size() - 2) + "/"
                + "\n" + pathSegments.get(pathSegments.size() - 1));

        return getImageAsset(uri);
    }


    private AssetFileDescriptor getImageAsset(Uri uri) throws IllegalArgumentException {
        AssetManager am = Objects.requireNonNull(getContext()).getAssets();
        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                if (fileName.equals(stickerPack.trayImageFile)) {
                    return fetchFile(uri, am, fileName, identifier);
                } else {
                    for (Sticker sticker : stickerPack.getStickers()) {
                        if (fileName.equals(sticker.imageFileName)) {
                            return fetchFile(uri, am, fileName, identifier);
                        }
                    }
                }
            }
        }
        return null;
    }

    private AssetFileDescriptor fetchFile(@NonNull Uri uri, @NonNull AssetManager am, @NonNull String fileName, @NonNull String identifier) {
        try {
            File file;
            //if(fileName.endsWith(".png")){
                file=new File(FileUtil.getPackageDataDir(getContext())+"/"+STICKERS_ASSET+"/" + identifier + "/", fileName);
             //   file = new File(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.path + "/" + identifier + "/", fileName);
            
            if (!file.exists()) {
                Log.d("fetFile", "StickerPack dir not found");
            }
            Log.d("fetchFile", "StickerPack " + file.getPath());
            return new AssetFileDescriptor(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY), 0L, -1L);
        } catch (IOException e) {
            Log.e(Objects.requireNonNull(getContext()).getPackageName(), "IOException when getting asset file, uri:" + uri, e);
            return null;
        }
    }


    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private synchronized void readContentFile(@NonNull Context context) {
        /*if (Hawk.get("sticker_pack", new ArrayList<StickerPack>()) != null) {
            stickerPackList.addAll(Hawk.get("sticker_pack", new ArrayList<StickerPack>()));
        }*/
		if(!readContent(getContext()).equals(""))
		stickerPackList=new Gson().fromJson(readContent(getContext()),Content.class).packs;
		else
		stickerPackList=new ArrayList<>();
		//stickerPackList.addAll(MainActivity.getPacks(FileUtil.getPackageDataDir(getContext())));
		
    }

    public List<StickerPack> getStickerPackList() {
     
            readContentFile(Objects.requireNonNull(getContext()));
		
		return stickerPackList;
       // return (List)Hawk.get("sticker_packs",new ArrayList<StickerPack>());
		
		//return MainActivity.getPacks();
    }


	public static File getContent(Context c)
		{
			return new File(FileUtil.getPackageDataDir(c)+"/"+STICKERS_ASSET,"contents.json");
		}

	public static FFmpegSession Gif2WebpAnimation(Context c, String src,String name,String Identfier)
		{
			File dest=new File(FileUtil.getPackageDataDir(c)+"/"+STICKERS_ASSET+"/"+Identfier+"/"+name);
			return Gif2WebpAnimation(c,new File(src),dest);
		}

	public static FFmpegSession Gif2WebpAnimation(Context c,File src,File dest)
		{
			if(dest.exists())
		{
			FileContentProvider.pop(c,"File Name Already Exist...");
			return null;
		}
			FFmpegSession session = FFmpegKit.execute("-y -i \""+src.getAbsolutePath()+
			"\" -vf \"fps=24,scale=512:512:force_original_aspect_ratio=decrease,format=rgba,pad=512:512:(ow-iw)/2:(oh-ih)/2:color=#00000000\" -loop 0 -q 20 -vcodec libwebp_anim -t 00:00:09 -c:a copy \""
			//" -filter_complex \"[0:v] scale=320:-1:flags=lanczos,split [a][b]; [a] palettegen=reserve_transparent=on:transparency_color=ffffff [p]; [b][p] paletteuse\" "
			+dest.getAbsolutePath()+"\"");
	
			return session;
		}
		
			
	public static FFmpegSession Image2Webp(Context c,String src, String name, String Identfier)
	{
		
		File dest=new File(FileUtil.getPackageDataDir(c)+"/"+STICKERS_ASSET+"/"+Identfier+"/"+name+"");
		
		return Image2Webp(c,new File(src),dest);
	}

	public static FFmpegSession Image2Webp(Context c,File src, File dest)
	{
		if(dest.exists())
			{
				FileContentProvider.pop(c,"File Name Already Exist...");
				return null;
			}
		//convert it to webp using FFmpeg toolkit session
		FFmpegSession session=FFmpegKit.execute(
		"-y -i \""
		+src.getAbsolutePath()
		+ "\" -vf \"scale=512:512:force_original_aspect_ratio=decrease,format=rgba,pad=512:512:(ow-iw)/2:(oh-ih)/2:color=#00000000\" -q 20 -vcodec libwebp -c:a copy \""
		+dest.getAbsolutePath()+"\""
		);
		return session;//for now
	}


	public static void saveContent(Context c,String gson)
		{
			File f=getContent(c);
			FileWriter fileWriter = null;
			try{
				if(!f.exists())
					{
						//f.mkdir();
						(new File(FileUtil.getPackageDataDir(c)+"/"+STICKERS_ASSET)).mkdir();
						f.createNewFile();
					}
				fileWriter = new FileWriter(f, false);
				fileWriter.write(gson);
				fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileWriter != null)
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	public static String readContent(Context c)
		{
			File f=getContent(c);
			if(!f.exists())
				{
					Content content=new  Content(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.ps,ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.ios,new ArrayList<StickerPack>());
					saveContent(c,new Gson().toJson(content));
				}
			StringBuilder sb = new StringBuilder();
			FileReader fr = null;
			try {
				fr = new FileReader(f);
				char[] buff = new char[1024];
				int length = 0;

				while ((length = fr.read(buff)) > 0)
					sb.append(new String(buff, 0, length));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fr != null) {
					try {
						fr.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

        	return sb.toString();
		}

	public static void addTWA(Activity c,String id,String name)
		{
			Intent intent = new Intent();
    		intent.setAction(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.wa);
			intent.putExtra(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.EXTRA_STICKER_PACK_ID, id); //identifier is the pack's identifier in contents.json file
			intent.putExtra(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.EXTRA_STICKER_PACK_AUTHORITY, ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY); //authority is the ContentProvider's authority. In the case of the sample app it is ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.CONTENT_PROVIDER_AUTHORITY.
			intent.putExtra(ph.STICKnoLOGIC.aerial.sutikero.BuildConfig.EXTRA_STICKER_PACK_NAME, name); //stickerPackName is the name of the sticker pack.
			try {
				c.startActivityForResult(intent, 200);
			} catch (Exception e) {
				Toast.makeText(c, "error adding stickers", Toast.LENGTH_LONG).show();
			}
		}

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                return getStickerPackInfo(uri, Collections.singletonList(stickerPack));
            }
        }

        return getStickerPackInfo(uri, new ArrayList<StickerPack>());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<StickerPack> stickerPackList) {
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE,
						IMAGE_DATA_VERSION,
						AVOID_CACHE,
						ANIMATED_STICKER_PACK
                });
        for (StickerPack stickerPack : stickerPackList) {
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.identifier);
            builder.add(stickerPack.name);
            builder.add(stickerPack.publisher);
            builder.add(stickerPack.trayImageFile);
            builder.add(stickerPack.androidPlayStoreLink);
            builder.add(stickerPack.iosAppStoreLink);
            builder.add(stickerPack.publisherEmail);
            builder.add(stickerPack.publisherWebsite);
            builder.add(stickerPack.privacyPolicyWebsite);
            builder.add(stickerPack.licenseAgreementWebsite);
			builder.add(stickerPack.imageDataVersion);
			builder.add(stickerPack.avoidCache?1:0);
			builder.add(stickerPack.animatedStickerPack?1:0);
        }
        Log.d(TAG, "getStickerPackInfo: " + stickerPackList.size());
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY,STICKER_FILE_ANIMATED});
        for (StickerPack stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.identifier)) {
                for (Sticker sticker : stickerPack.getStickers()) {
                    cursor.addRow(new Object[]{sticker.imageFileName, TextUtils.join(",", sticker.emojis),sticker.isAnimated});
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }
}
