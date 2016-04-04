package com.sebastien.example.rockstars.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.adapter.CustomListViewAdapter;
import com.sebastien.example.rockstars.stars.Stars;
import com.sebastien.example.rockstars.utils.Utils;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Sebastien on 25/03/2016.
 */
public class BookmarksFragment extends Fragment {
    private ListView bookmarks_list;
    private ArrayList<Stars> Bookmarks_list_item;
    private Context ctx;
    private Activity activity;
    private LinearLayout layout_status;
    public BookmarksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the view of this Fragment
        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ctx = getContext();
        activity = getActivity();
        bookmarks_list = (ListView)rootView.findViewById(R.id.listView_bookmarks);
        layout_status =(LinearLayout)rootView.findViewById(R.id.layout_status_bookmarks);

        Bookmarks_list_item = new ArrayList<Stars>();
        //We use this function to read in our json file on the internal memory
        JSONObject jobject =Utils.readFileFromSD("data.json");
        //This Function create Stars Object From an JSON ARRAY, and add them into the Bookmarks ArrayList
        Utils.generate_bookmarks_from_jsonObject(jobject, Bookmarks_list_item);

        //We Instanciate the CustomListViewAdapter with our ArrayList of Stars Object, with the type 1 , which means bookmark list
        CustomListViewAdapter adapter = new CustomListViewAdapter(activity, Bookmarks_list_item, 1, getFragmentManager());

        //We set the adapter to our ListView
        bookmarks_list.setAdapter(adapter);
        if(Bookmarks_list_item.size()>0) {
            layout_status.setVisibility(View.GONE);
        }

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
