package com.aquafadas.tagthebus.station.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.model.Picture;
import com.aquafadas.tagthebus.station.model.Station;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.aquafadas.tagthebus.utils.AppStaticValues.PICTURE;
import static com.aquafadas.tagthebus.utils.AppStaticValues.STATION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnShareFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ShareFragment extends Fragment {

    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.shared_picture)
    ImageView sharedPicture;

    private OnShareFragmentInteractionListener mListener;
    private Picture picture;
    private Station station;

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            picture = (Picture) getArguments().getSerializable(PICTURE);
            station = (Station) getArguments().getSerializable(STATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        if (picture != null) {
            File file = new File(picture.getPath());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                sharedPicture.setImageBitmap(bitmap);
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShareFragmentInteractionListener) {
            mListener = (OnShareFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShareFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick(R.id.close)
    public void backToList() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.share)
    public void sharePicture() {
        try {
            File myFile = new File(picture.getPath());
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType(getMimeType(myFile.getPath()));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, picture.getTitle() + " " + picture.getDateTime());
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
            startActivity(Intent.createChooser(sharingIntent, "Share picture"));
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getMimeType(String filePath) {
        String type = null;
        String extension = getFileExtensionFromUrl(filePath);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public String getFileExtensionFromUrl(String url) {
        int dotPos = url.lastIndexOf('.');
        if (0 <= dotPos) {
            return (url.substring(dotPos + 1)).toLowerCase();
        }
        return "";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnShareFragmentInteractionListener {

    }
}
