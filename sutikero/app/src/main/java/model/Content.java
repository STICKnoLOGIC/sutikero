package ph.STICKnoLOGIC.aerial.sutikero.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.*;


public class Content implements Parcelable {
	
	@SerializedName("ios_appstore_link")
	public String as;
	@SerializedName("sticker_packs")
	public List<StickerPack> packs;
	@SerializedName("android_play_store_link")
    public String ps;
	
	public Content(String ps,String as, List<StickerPack> packs)
	{
		
		this.ps=ps;
		this.as=as;
		this.packs=packs=new ArrayList<>();
	}
	
	
	
	private Content(Parcel in) {
		
		ps=in.readString();
		as=in.readString();
		packs=in.createTypedArrayList(StickerPack.CREATOR);

    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ps);
		dest.writeString(as);
		dest.writeTypedList(packs);

    }
}
