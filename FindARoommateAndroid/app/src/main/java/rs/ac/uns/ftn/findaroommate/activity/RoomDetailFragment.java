package rs.ac.uns.ftn.findaroommate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rs.ac.uns.ftn.findaroommate.R;
import rs.ac.uns.ftn.findaroommate.model.Ad;

/**
 * A fragment representing a single Room detail screen.
 * This fragment is either contained in a {@link RoomListActivity}
 * in two-pane mode (on tablets) or a {@link RoomDetailActivity}
 * on handsets.
 */
public class RoomDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The Ad content this fragment is presenting.
     */
    private Ad mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RoomDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = RoomListActivity.adsMap.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.room_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.room_title_frag)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.room_detail_text)).setText(mItem.getDescription());
            /*((TextView) rootView.findViewById(R.id.room_ad_type)).setText(mItem.getAdType());*/
            ((TextView) rootView.findViewById(R.id.room_longitude)).setText(String.valueOf(mItem.getLongitude()));
            ((TextView) rootView.findViewById(R.id.room_latitude)).setText(String.valueOf(mItem.getLatitude()));
            ((TextView) rootView.findViewById(R.id.room_flat_m2)).setText(String.valueOf(mItem.getFlatM2()));
            ((TextView) rootView.findViewById(R.id.room_room_m2)).setText(String.valueOf(mItem.getRoomM2()));
            ((TextView) rootView.findViewById(R.id.room_price)).setText(String.valueOf(mItem.getPrice()));
            if(mItem.isCostsIncluded()) {
                ((TextView) rootView.findViewById(R.id.room_costs_included)).setText("Yes");
            } else {
                ((TextView) rootView.findViewById(R.id.room_costs_included)).setText("No");
            }
            ((TextView) rootView.findViewById(R.id.room_deposit)).setText(String.valueOf(mItem.getDeposit()));

            ((TextView) rootView.findViewById(R.id.room_available_from)).setText(mItem.getAvailableFrom().toString());
            ((TextView) rootView.findViewById(R.id.room_available_until)).setText(mItem.getAvailableUntil().toString());

            ((TextView) rootView.findViewById(R.id.room_min_days)).setText(String.valueOf(mItem.getMinDays()));
            ((TextView) rootView.findViewById(R.id.room_max_person)).setText(String.valueOf(mItem.getMaxPerson()));
            ((TextView) rootView.findViewById(R.id.room_ladies_num)).setText(String.valueOf(mItem.getLadiesNum()));
            ((TextView) rootView.findViewById(R.id.room_boys_num)).setText(String.valueOf(mItem.getBoysNum()));
        }

        return rootView;
    }
}
