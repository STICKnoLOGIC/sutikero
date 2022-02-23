package ph.STICKnoLOGIC.aerial.sutikero.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleView extends RecyclerView {
    public RecycleView(Context context) {
        super(context);
    }

    public RecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 0.50f;
    }
}
