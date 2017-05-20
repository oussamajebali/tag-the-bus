package com.aquafadas.tagthebus.station.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquafadas.tagthebus.R;
import com.aquafadas.tagthebus.station.model.Station;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aquafadas.tagthebus.utils.AppStaticValues.STATION;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StationDetailFragment.OnStationDetailFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StationDetailFragment extends Fragment {

    @BindView(R.id.station_title)
    TextView stationTitle;

    @BindView(R.id.back_to_list)
    ImageView backToList;

    private OnStationDetailFragmentInteractionListener mListener;
    private Station station;

    public StationDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            station = (Station) getArguments().getSerializable(STATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_station_detail, container, false);
        ButterKnife.bind(this, view);
        if (station != null)
            stationTitle.setText(station.getStreet_name());
        stationTitle.setSelected(true);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStationDetailFragmentInteractionListener) {
            mListener = (OnStationDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStationDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.back_to_list)
    public void backToList() {
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
    public interface OnStationDetailFragmentInteractionListener {

    }
}
