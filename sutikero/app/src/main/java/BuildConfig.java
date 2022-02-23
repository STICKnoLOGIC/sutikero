package ph.STICKnoLOGIC.aerial.sutikero;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import ph.STICKnoLOGIC.aerial.sutikero.provider.StickerContentProvider.*;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;

public final class BuildConfig {
	public static final String path="sticker_asset",
	email="jobeth.llame@gmail.com",
	website="https://www.github.com/STICKnoLOGIC/",
	policy="https://sites.google.com/view/my-wa-sticker-packs-lt/privacy-policy",
	term="",
	ps="https://play.google.com/store/apps/details?id=ph.STICKnoLOGIC.aerial.sutikero",
	ios="https://apps.apple.com/app/id300136119",
	wa="com.whatsapp.intent.action.ENABLE_STICKER_PACK";
    public static final String APPLICATION_ID = "ph.STICKnoLOGIC.aerial.sutikero";
    public static final String CONTENT_PROVIDER_AUTHORITY = "ph.STICKnoLOGIC.aerial.sutikero.provider.stickercontentprovider";;
	
	public static final int TRANSFER_PACK=2030,
	CATCH_PACK=1020,
	SEND_TWA=200,
	CATCH_TWA=300;
	
	
	
	    /**
     * Do not change below values of below 3 lines as this is also used by WhatsApp
     */
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";


}

