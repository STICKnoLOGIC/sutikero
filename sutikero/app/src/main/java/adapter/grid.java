package ph.STICKnoLOGIC.aerial.sutikero.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.*;
import android.view.*;
import android.graphics.Bitmap;
import java.util.*;
import java.io.*;
import android.net.Uri;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.integration.webp.decoder.*;*/
import ph.STICKnoLOGIC.aerial.sutikero.*;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;
import ph.STICKnoLOGIC.aerial.sutikero.util.onClickItem;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;


public class grid extends RecyclerView.Adapter<grid.gViewHolder> {
    
	List<Sticker> list;
	String identifier="";
	onClickItem listener;
	
	public grid(onClickItem listener, List<Sticker> list,String identifier)
	{
		this.listener=listener;
		this.identifier=identifier;
		this.list=list;
	}
	
				
	@Override
    public void onBindViewHolder(gViewHolder v,final int p2) {

            final Uri stickerAssetUri = StickerPackLoader.getStickerAssetUri(identifier, list.get(p2).imageFileName);
            /*DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(stickerAssetUri)
     //               .setAutoPlayAnimations(true)
					.setOldController(v.sticker.getController())
                    .build();
           */v.sticker.setImageURI(stickerAssetUri);
           // v.sticker.setController(controller);
			v.sticker.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(listener!=null)
						listener.onClick(v,stickerAssetUri);
				}
			});
			v.sticker.setOnLongClickListener(new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View _view) {
					if(listener!=null)
						listener.Delete(p2);
					return true;
				}
			}
			
			);
		
		
		
		
		
		
		
		/*Transformation<Bitmap> center = new FitCenter();
		GlideApp.with(v.sticker.getContext())
		.load(new File(FileUtil.getPackageDataDir(v.sticker.getContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+identifier+"/"+list.get(p2).imageFileName))
		.optionalTransform(center)
		.set(WebpFrameLoader.FRAME_CACHE_STRATEGY, WebpFrameCacheStrategy.ALL)
        .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(center))
		.into(v.sticker);
		//v.name.setText(list.get(p2).imageFileName);*/
	}
	
	
	@Override
    public gViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        
        View view=LayoutInflater.from(p1.getContext()).inflate(R.layout.sticker,p1,false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new gViewHolder(view);
    }
	@Override
    public int getItemCount() {
        return list.size();
    }
	
	
	public class gViewHolder extends RecyclerView.ViewHolder
	{
	SimpleDraweeView sticker;
		gViewHolder(View v)
		{
			super(v);
			sticker=v.findViewById(R.id.imageview1);
		}
	}
}
