package lab.wasikrafal.psmaprojekt.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lab.wasikrafal.psmaprojekt.R;

/**
 * Created by Rafał on 19.04.2018.
 */

public class CarouselPagerAdapter extends PagerAdapter
{
    private int[] images = new int[4];
    private Context ctx;
    private LayoutInflater inflater;

    public CarouselPagerAdapter(Context c)
    {
        ctx = c;
        images[0] = R.drawable.screen_jeden;
        images[1] = R.drawable.screen_dwa;
        images[2] = R.drawable.screen_trzy;
        images[3] = R.drawable.screen_cztery;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.page_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView2);
        imageView.setImageResource(images[position]);

        TextView textView = itemView.findViewById(R.id.textView2);
        textView.setText("Obraz " + (position+1) + " z " + images.length);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
