
package com.example.android.finalprojectmal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Movie> movies = new ArrayList<>();
    private LayoutInflater inflater;


    public MovieAdapter(Context c,ArrayList<Movie> movies) {
        mContext = c;
        inflater = LayoutInflater.from(mContext);
        this.movies = movies;
    }






    public View getView(int position, View convertView,ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.single_grid_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(movies.get(position).getPosterURL().contains(mContext.getPackageName())){
            try {
                loadImageFromLocalStorge(movies.get(position).getPosterURL(),
                        movies.get(position).getTitle(),viewHolder.posterIV);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(mContext).load(movies.get(position).getPosterURL()).into(viewHolder.posterIV);
        }

        viewHolder.title.setText(movies.get(position).getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void loadImageFromLocalStorge(String path,String fileName,ImageView posterIV) throws IOException {

        try {
            File f=new File(path,fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            posterIV.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class ViewHolder{
    ImageView posterIV;
    TextView title;
    ViewHolder(View view){
        posterIV = (ImageView) view.findViewById(R.id.poster);
        title = (TextView) view.findViewById(R.id.movieTitle);
    }
}