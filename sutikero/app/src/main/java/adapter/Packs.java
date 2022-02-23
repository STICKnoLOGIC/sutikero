package ph.STICKnoLOGIC.aerial.sutikero.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.*;
import android.view.*;
import android.net.*;
import java.util.*;
import java.io.*;

import com.facebook.drawee.view.SimpleDraweeView;

import ph.STICKnoLOGIC.aerial.sutikero.provider.*;
import ph.STICKnoLOGIC.aerial.sutikero.model.*;
import ph.STICKnoLOGIC.aerial.sutikero.*;

public class Packs extends RecyclerView.Adapter<Packs.gViewHolder> {
    
	List<StickerPack> list;
	Bridge mBridge;
	
	public Packs(List<StickerPack> list,Bridge mBridge)
	{
		this.list=list;
		this.mBridge=mBridge;
	}
	
				
	@Override
    public void onBindViewHolder(gViewHolder v,final int p2) {
		v.tray.setImageURI(Uri.fromFile((new File(FileUtil.getPackageDataDir(v.name.getContext())+"/"+StickerContentProvider.STICKERS_ASSET+"/"+list.get(p2).identifier+"/"+list.get(p2).trayImageFile))));
		v.option.setImageURI(Uri.parse("res:/"+R.drawable.mire));
		v.name.setText(list.get(p2).name);
		
		
		v.main.setOnClickListener(
		new View.OnClickListener(){
			@Override
			public void onClick(View vs)
			{
				mBridge.Tapped(p2);
			}
		});
		v.name.setCompoundDrawablesWithIntrinsicBounds(0,0,list.get(p2).animatedStickerPack?R.drawable.play:0, 0);
		v.option.setOnClickListener(
		new View.OnClickListener(){
			@Override
			public void onClick(View vs)
			{
				mBridge.Options(p2,vs);
			}
		});
	}
	
	
	@Override
    public gViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        
        View view=LayoutInflater.from(p1.getContext()).inflate(R.layout.packs,p1,false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        return new gViewHolder(view);
    }
	@Override
    public int getItemCount() {
        return list.size();
    }
	
	public interface Bridge{
		public void Options(int pos,View v);
		public void Tapped(int pos);
	}
	public class gViewHolder extends RecyclerView.ViewHolder
	{
		SimpleDraweeView tray,option;
		TextView name;
		LinearLayout main;
		
		gViewHolder(View v)
		{
			super(v);
			tray=v.findViewById(R.id.imageview1);
			option=v.findViewById(R.id.imageview2);
			name=v.findViewById(R.id.textview1);
			main=v.findViewById(R.id.linear1);
		}
	}
}