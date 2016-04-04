package com.sebastien.example.rockstars.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.adapter.CustomListViewAdapter;
import com.sebastien.example.rockstars.stars.Stars;
import com.sebastien.example.rockstars.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Sebastien on 25/03/2016.
 */
public class RockstarsFragment extends Fragment {
    private ListView rockstars_list;
    private ArrayList<Stars> Stars_list_item;
    private SearchView search;
    private JSONObject jobject;
    private CustomListViewAdapter adapter ;
    private SwipeRefreshLayout swipe,swipe2;
    private Context ctx;
    private String Web_Server_Url ="http://54.72.181.8/yolo/";
    private String Web_Server_Json ="contacts.json";
    private ImageView img_status,swipe_status;
    private TextView txt_status;
    private LinearLayout layout_status;
    //private String Web_Server_Url ="http://192.168.1.8/Rockstars/";
    //private String Web_Server_Json ="getjson.php";

    public RockstarsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the view of this Fragment
        View rootView = inflater.inflate(R.layout.fragment_rockstars,container,false);
        swipe =(SwipeRefreshLayout)rootView.findViewById(R.id.swipe);
        //swipe2 =(SwipeRefreshLayout)rootView.findViewById(R.id.swipe2);
        rockstars_list = (ListView)rootView.findViewById(R.id.listView_stars);
        search =(SearchView)rootView.findViewById(R.id.search);
        img_status = (ImageView)rootView.findViewById(R.id.img_status);
        txt_status = (TextView)rootView.findViewById(R.id.txt_status);
        swipe_status = (ImageView)rootView.findViewById(R.id.swipe_status);
        layout_status = (LinearLayout)rootView.findViewById(R.id.layout_status);

        Stars_list_item = new ArrayList<Stars>();
        ctx = getContext();
        if(Utils.getBooleanValue(ctx,"first_launch")) {
            new get_jsonTask().execute();
        }else{
            new get_default_json().execute();
        }



        //We Instanciate the CustomListViewAdapter with our ArrayList of Stars Object,with the type 0 , which means Rockstars list
        adapter = new CustomListViewAdapter(getActivity(),Stars_list_item,0,getFragmentManager());

        //We set the adapter to our ListView
        rockstars_list.setAdapter(adapter);
        init_listener();



        return rootView;
    }

    private void init_listener() {
        rockstars_list.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                /*
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                        new get_jsonTask().execute();

                /*
                    }
                },3000);*/
            }
        });
    }

    class get_jsonTask extends AsyncTask<String,String,ArrayList<JSONObject>>{

        @Override
        protected ArrayList<JSONObject> doInBackground(String... params) {
            ArrayList<JSONObject> jobj_array=new ArrayList<JSONObject>();
            JSONObject current_jobject = Utils.readFileFromSD("data.json");
            JSONObject jobj=null;
            JSONObject obj_status= new JSONObject();
            jobj = Utils.get_json_from_server(Web_Server_Url, Web_Server_Url + Web_Server_Json, current_jobject);
            if(jobj!=null){
                try {
                    obj_status.put("from_server",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                jobj =current_jobject;
                try {
                    obj_status.put("from_server",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jobj_array.add(0,obj_status);
            jobj_array.add(1, jobj);
            return jobj_array;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jobj_array) {
            super.onPostExecute(jobj_array);
            Boolean from_server = false;
            try {
                from_server = jobj_array.get(0).getBoolean("from_server");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jobj =jobj_array.get(1);
            if(jobj!=null){
                swipe.setRefreshing(false);
                Stars_list_item.clear();
                if(from_server) {
                    //We create a Json File from the Json Array got from server
                    Utils.writeJSONFileToSD(ctx, jobj, "data");
                }
                //This Function create Stars Object From an JSON ARRAY, and add them into the Stars ArrayList
                Utils.generate_rockstars_from_jsonObject(jobj, Stars_list_item);
                layout_status.setVisibility(View.GONE);
                Utils.putBooleanValue(ctx, "first_launch", false);
                adapter.notifyDataSetChanged();

            }else {
                swipe.setRefreshing(false);
                layout_status.setVisibility(View.VISIBLE);
            }

        }
    }
    class get_default_json extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject current_jobject = Utils.readFileFromSD("data.json");

            return current_jobject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if(jsonObject!=null){
                swipe.setRefreshing(false);
                Stars_list_item.clear();
                Utils.generate_rockstars_from_jsonObject(jsonObject, Stars_list_item);
                layout_status.setVisibility(View.GONE);
                Utils.putBooleanValue(ctx, "first_launch", false);
                adapter.notifyDataSetChanged();
            }else {
                swipe.setRefreshing(false);
                layout_status.setVisibility(View.VISIBLE);
            }

        }
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
