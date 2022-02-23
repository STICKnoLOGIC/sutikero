package ph.STICKnoLOGIC.aerial.sutikero.model;
/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.*;

public class Sticker implements Parcelable {
	@SerializedName("image_file")
    public String imageFileName;
    public List<String> emojis=new ArrayList<>();
	@SerializedName("is_animated_sticker")
	public boolean isAnimated;   
	public long size;

    public Sticker(String imageFileName, List<String> emojis) {
        this.imageFileName = imageFileName;
        this.emojis = emojis;
    }

    private Sticker(Parcel in) {
        imageFileName = in.readString();
        emojis = in.createStringArrayList();
        size = in.readLong();
    }
	
	public void update(String old, String nw)
	{
		imageFileName=imageFileName.replace(old,nw);
	}

    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        dest.writeStringList(emojis);
        dest.writeLong(size);
    }
}
