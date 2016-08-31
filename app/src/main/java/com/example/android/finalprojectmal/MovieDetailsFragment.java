package com.example.android.finalprojectmal;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by michael on 17/08/16.
 */
public class MovieDetailsFragment extends Fragment {

    private Movie movie;
    private FavouriteDataSource favouriteDataSource;
    private Context mContext;
    private boolean isFromDB = false;

    public MovieDetailsFragment() {
        movie = new Movie();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_details_fragment, container, false);
        mContext = getActivity();
        Stetho.initializeWithDefaults(getActivity());
        Bundle b = getArguments();
        favouriteDataSource = new FavouriteDataSource(getActivity());
        favouriteDataSource.open();

        

        if (b != null && b.containsKey(Movie.MOVIE_ID)) {
            movie = new Movie(b);
        }

        if(movie.getPosterURL().contains(mContext.getPackageName())){
            isFromDB = true;
        }

        try {
            if (!isFromDB) {
                FetchMovieDetailsTask task = new FetchMovieDetailsTask(movie);
                movie = task.execute().get();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        final ImageView posterIV = (ImageView) rootView.findViewById(R.id.detailPoster);
        TextView titleTV = (TextView) rootView.findViewById(R.id.detailMovieTitle);
        TextView dateTV = (TextView) rootView.findViewById(R.id.detailMovieDate);
        TextView rateTV = (TextView) rootView.findViewById(R.id.detailMovieRate);
        TextView descriptionTV = (TextView) rootView.findViewById(R.id.detailMovieDescription);
        Button favourite = (Button) rootView.findViewById(R.id.addToFavourite);


        if(isFromDB){
            try {
                loadImageFromLocalStorge(movie.getPosterURL(),
                        movie.getTitle(),posterIV);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(mContext).load(movie.getPosterURL()).into(posterIV);
        }

        titleTV.setText(movie.getTitle());
        dateTV.setText(movie.getRelaseDate().substring(0, 4));
        rateTV.setText(movie.getRate() + "/10");
        descriptionTV.setText(movie.getDescription());

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sumOfRecords;
                Bitmap image = ((BitmapDrawable) posterIV.getDrawable()).getBitmap();
                String imageName = movie.getTitle();
                String path = "";
                try {
                    path = saveImageLocal(image, imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                movie.setPosterURL(path);
                sumOfRecords = favouriteDataSource.createMovie(movie, image);
                Toast toast = new Toast(getActivity());
                if (sumOfRecords == -1) {
                    Toast.makeText(getActivity(), R.string.movieInFavouriteToast, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), R.string.movieNotInFavourite, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        RelativeLayout rel = (RelativeLayout) rootView.findViewById(R.id.allDetails);
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (movie.getYoutubeURLs() != null) {
            addTrailersToView(rel, orientation);
        }

        if (movie.getReviewsAutors() != null && movie.getReviewsContents() != null) {
            addReviewsToView(rel, orientation);
        }


        return rootView;
    }





    //trailer image view 10
    //trailer text view 30
    //review author 50
    //review content 70

    private View addTrailersToView(RelativeLayout v, int orientation) {
        if (movie.getYoutubeURLs() != null) {
            int dpValue = 16; // margin in dips
            float d = getActivity().getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);
            for (int i = 0; i < movie.getYoutubeURLs().size(); i++) {
                TextView trailerTV = new TextView(getActivity());
                ImageView trailerIV = new ImageView(getActivity());

                trailerIV.setImageResource(R.drawable.play);
                trailerIV.setId(i + 10);
                trailerTV.setId(i + 30);

                RelativeLayout.LayoutParams llparms = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);


                if (i == 0) {
                    if (orientation == 2) {
                        llparms.addRule(RelativeLayout.RIGHT_OF, R.id.detailPoster);
                    }
                    llparms.addRule(RelativeLayout.BELOW, R.id.detailMovieDescription);

                } else {
                    if (orientation == 2) {
                        llparms.addRule(RelativeLayout.RIGHT_OF, R.id.detailPoster);
                    }
                    llparms.addRule(RelativeLayout.BELOW, (i - 1) + 10);
                }
                llparms.setMargins(margin, margin, 0, margin);
                trailerIV.setLayoutParams(llparms);
                v.addView(trailerIV);

                trailerTV.setText(mContext.getString(R.string.trailerValueTV) + (i + 1) + "     ");
                trailerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                llparms = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);


                if (i == 0) {
                    llparms.addRule(RelativeLayout.BELOW, R.id.detailMovieDescription);


                } else {
                    llparms.addRule(RelativeLayout.BELOW, (i - 1) + 10);
                }
                llparms.addRule(RelativeLayout.RIGHT_OF, trailerIV.getId());
                llparms.setMargins(margin, margin, margin, margin);
                trailerTV.setLayoutParams(llparms);
                v.addView(trailerTV);

            }

            for (int i = 0; i < movie.getYoutubeURLs().size(); i++) {
                TextView tv = (TextView) v.findViewById(i + 30);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(movie.getYoutubeURLs().get(v.getId() - 30)));
                        startActivity(intent);

                    }
                });
            }
        }
        return v;
    }


    private View addReviewsToView(RelativeLayout v, int orientation) {
        int dpValueAutor = 20;
        int dpValueContent = 16;
        float d = getActivity().getResources().getDisplayMetrics().density;
        int marginAutor = (int) (dpValueAutor * d);
        int marginContent = (int) (dpValueContent * d);


        if (movie.getReviewsAutors() != null && movie.getReviewsContents() != null) {
            for (int i = 0; i < movie.getReviewsAutors().size(); i++) {
                TextView authorTV = new TextView(getActivity());
                TextView contentTV = new TextView(getActivity());
                authorTV.setId(i + 50);
                contentTV.setId(i + 70);

                RelativeLayout.LayoutParams llparms = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (i == 0) {
                    if (orientation == 2) {
                        llparms.addRule(RelativeLayout.RIGHT_OF, R.id.detailPoster);
                    }
                    if (movie.getYoutubeURLs().size() > 0) {
                        llparms.addRule(RelativeLayout.BELOW, movie.getYoutubeURLs().size() + 29);
                    } else {
                        llparms.addRule(RelativeLayout.BELOW, R.id.detailMovieDescription);
                    }


                } else {
                    if (orientation == 2) {
                        llparms.addRule(RelativeLayout.RIGHT_OF, R.id.detailPoster);
                    }
                    llparms.addRule(RelativeLayout.BELOW, (i - 1) + 70);
                    Log.d("contentID", "" + (contentTV.getId()));
                }
                llparms.setMargins(marginAutor, marginAutor, marginAutor, marginAutor);
                authorTV.setLayoutParams(llparms);
                authorTV.setText(movie.getReviewsAutors().get(i));
                authorTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                v.addView(authorTV);

                llparms = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (orientation == 2) {
                    llparms.addRule(RelativeLayout.RIGHT_OF, R.id.detailPoster);
                }
                llparms.addRule(RelativeLayout.BELOW, authorTV.getId());
                llparms.setMargins(marginContent, 0, marginContent, 0);
                contentTV.setLayoutParams(llparms);
                contentTV.setText(movie.getReviewsContents().get(i));
                contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                v.addView(contentTV);
            }
        }

        return v;
    }

    private String saveImageLocal(Bitmap imageToSave, String fileName) throws IOException {

        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mContext.getFilesDir().getAbsolutePath();
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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favouriteDataSource.close();
    }


}
