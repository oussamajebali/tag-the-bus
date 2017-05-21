package com.aquafadas.tagthebus.station.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.manager.RealmManager;
import com.aquafadas.tagthebus.station.model.Picture;
import com.aquafadas.tagthebus.station.model.Station;
import com.aquafadas.tagthebus.utils.DateUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

import static com.aquafadas.tagthebus.utils.AppStaticValues.PATH;
import static com.aquafadas.tagthebus.utils.AppStaticValues.STATION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPictureFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PictureFragment extends Fragment {

    @BindView(R.id.back_to_detail)
    ImageView backToDetail;
    @BindView(R.id.station_title)
    TextView stationTitle;
    @BindView(R.id.save_picture)
    ImageView savePicture;
    @BindView(R.id.station_picture)
    ImageView stationPicture;
    @BindView(R.id.picture_title)
    EditText pictureTitle;
    private OnPictureFragmentInteractionListener mListener;

    private Station station;
    private Picture picture;
    private Realm realm;
    private int stationId = -1;
    private String path;

    public PictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stationId = getArguments().getInt(STATION);
            path = getArguments().getString(PATH);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        realm = RealmManager.with(this).getRealm();
        station = RealmManager.with(this).getStation(stationId);
        if (station != null)
            stationTitle.setText(station.getStreet_name());
        stationTitle.setSelected(true);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                stationPicture.setImageBitmap(bitmap);
            }
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPictureFragmentInteractionListener) {
            mListener = (OnPictureFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPictureFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.back_to_detail)
    public void backToDetail() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.save_picture)
    public void savePicture() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                picture = realm.createObject(Picture.class);
                picture.setPath(path);
                picture.setTitle(pictureTitle.getText().toString());
                picture.setDateTime(DateUtils.getDateFromTimeStamp(System.currentTimeMillis()));
                station.getPictures().add(picture);
            }
        });
        getActivity().onBackPressed();
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
    public interface OnPictureFragmentInteractionListener {
    }
}
