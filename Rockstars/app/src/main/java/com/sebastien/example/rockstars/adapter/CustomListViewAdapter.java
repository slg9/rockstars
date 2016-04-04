package com.sebastien.example.rockstars.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.fragment.BookmarksFragment;
import com.sebastien.example.rockstars.stars.Stars;
import com.sebastien.example.rockstars.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Sebastien on 26/03/2016.
 */
public class CustomListViewAdapter extends BaseAdapter implements Filterable {
    private FragmentManager fm;
    private Stars item;
    private RelativeLayout item_raw ;
    private ImageView picture ;
    private TextView name ;
    private TextView status ;
    private CheckBox bookmark ;
    private Button delete;
    private LayoutInflater inflater;
    private ArrayList<Stars> items ;
    private ArrayList<Stars> filtered_data ;
    private int type_of_list;
    private Context ctx;

    public CustomListViewAdapter(Context context, ArrayList<Stars> items,int type_of_list,FragmentManager fm) {
        this.fm=fm;// here i get the fragment manager to refresh my fragment, when i click on the delete button
        this.ctx = context;
        this.inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        filtered_data = items;
        this.type_of_list = type_of_list;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item =items.get(position);
        View view = convertView;
        if(convertView == null)
            view=inflater.inflate(R.layout.stars_item,null);
        item_raw =(RelativeLayout)view.findViewById(R.id.item_raw);
        picture = (ImageView)view.findViewById(R.id.IMG_picture);
        name = (TextView)view.findViewById(R.id.TXT_rockstar_name);
        status = (TextView)view.findViewById(R.id.TXT_rockstar_status);
        bookmark = (CheckBox)view.findViewById(R.id.CHK_bookmark);
        delete = (Button)view.findViewById(R.id.BTN_delete);

        //Here we put the all item data in the different ObjectView in the ListView
        Utils.set_picture(picture,item.getPicture());
        name.setText(item.getFullname());
        status.setText(item.getStatus());
        if(item.getBookmark()){
            bookmark.setChecked(true);
        }else{
            bookmark.setChecked(false);
        }

        //If it's a rockstart list we add the checkbox bookmark to the view
        if(type_of_list==0){
            delete.setVisibility(View.GONE);
            bookmark.setVisibility(View.VISIBLE);
        }
        //If it's a Bookmark list we add the Button delete to the view
        else{
            bookmark.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }

        checkbox_listener(bookmark, ctx, item);
        Button_delete_listner(delete, ctx, item, item_raw);




        return view;
    }



    private void checkbox_listener(final CheckBox bookmark, final Context ctx, final Stars item) {
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We use this function to read in our json file on the internal memory
                JSONObject jobjparent = Utils.readFileFromSD("data.json");
                try {
                    JSONArray jarray = jobjparent.getJSONArray("contacts");
                    //Here we get the id in the JsonArray of the Stars item in the ArrayList Stars
                    int pos = item.getId();
                    jarray.getJSONObject(pos).remove("bookmark");
                    jarray.getJSONObject(pos).put("bookmark", bookmark.isChecked());
                    Utils.writeJSONFileToSD(ctx, jobjparent,"data");
                    //Utils.startActivity(activity, MainActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void Button_delete_listner(Button delete, final Context ctx, final Stars item, final RelativeLayout item_raw) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We use this function to read in our json file on the internal memory
                JSONObject jobjparents =Utils.readFileFromSD("data.json");

                try {
                    JSONArray jarray =jobjparents.getJSONArray("contacts");
                    //Here we get the id in the JsonArray of the Stars item in the ArrayList Stars
                    int pos =item.getId();
                    //We remove the bookmark with the good position in JSON Array
                    jarray.getJSONObject(pos).remove("bookmark");
                    jarray.getJSONObject(pos).put("bookmark", false);
                    //we update the jsonFile
                    Utils.writeJSONFileToSD(ctx, jobjparents, "data");
                    Utils.putIntValue(ctx, "current_fragment_position", 1);
                    Utils.putBooleanValue(ctx, "first_launch", false);
                    //Utils.startActivity(activity, MainActivity.class);
                    //Here i reload my fragment when i press this button

                    fm.beginTransaction()
                            .replace(R.id.frame, new BookmarksFragment(), "bookmark")
                            .addToBackStack(null)
                            .commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });
    }
    public void delete(int item){

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();
                if (constraint == null || constraint.length()==0 ) {
                    result.values = filtered_data;
                    result.count = filtered_data.size();
                }else{
                    ArrayList<Stars> filterdata = new ArrayList<Stars>();
                    for(int i=0;i<filtered_data.size();i++){
                        String filter_data=filtered_data.get(i).getFullname();
                        if(filter_data.toLowerCase().contains(constraint.toString())){
                            filterdata.add(filtered_data.get(i));
                        }
                    }
                    result.values = filterdata;
                    result.count = filterdata.size();
                }
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<Stars>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
