package com.sebastien.example.rockstars.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.utils.Utils;
import java.io.IOException;

/**
 * Created by Sebastien on 25/03/2016.
 */
public class ProfileFragment extends Fragment {
    private Toolbar toolbar;
    private Context ctx ;
    private Bitmap bmp=null;
    private ImageView profile_pic,profile_pic2 ;
    private TextView profile_name ;
    private EditText edit_name;
    private Button edit_profile,close_profile,check_profile,btn_profile_name;
    private String current_name,new_name;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the view of this Fragment
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        ctx = getContext();
        profile_pic =(ImageView)rootView.findViewById(R.id.IMG_picture_profile);
        profile_pic2 =(ImageView)rootView.findViewById(R.id.IMG_btn_picture_profile);
        profile_name = (TextView)rootView.findViewById(R.id.TXT_name_profile);
        edit_name = (EditText)rootView.findViewById(R.id.EDT_name_profile);
        btn_profile_name = (Button)rootView.findViewById(R.id.BTN_name_profile);
        edit_profile = (Button)rootView.findViewById(R.id.button_toolbar);
        close_profile = (Button)rootView.findViewById(R.id.button_toolbar2);
        check_profile = (Button)rootView.findViewById(R.id.button_toolbar3);
        toolbar=(Toolbar)rootView.findViewById(R.id.toolbar_profile);

        current_name=profile_name.getText().toString();


        //profile_pic.setImageBitmap(Utils.get_picture());
        Utils.set_picture(profile_pic,"picture");
        Utils.set_name(profile_name);
        init_listener();



        return rootView;
    }

    private void init_listener() {
        btn_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_profile_name.setVisibility(View.GONE);
                edit_name.setVisibility(View.VISIBLE);
                edit_name.setText("");
                edit_name.setHint(current_name);
                profile_name.setVisibility(View.GONE);

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_profile_name.setText(current_name);
                profile_pic2.setVisibility(View.VISIBLE);
                profile_name.setVisibility(View.GONE);
                edit_profile.setVisibility(View.GONE);
                close_profile.setVisibility(View.VISIBLE);
                check_profile.setVisibility(View.VISIBLE);
                btn_profile_name.setVisibility(View.VISIBLE);
                profile_pic.setAlpha((float)0.5);
            }
        });
        check_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name=edit_name.getText().toString();
                try {
                    if (bmp != null && new_name.length()> 0 && !new_name.trim().equals("nom_du_profile") ) {
                        Utils.createImageFile(bmp,"picture");
                        Utils.writeJSONFileToSD(ctx, Utils.upate_json_user(edit_name), "user");
                        Utils.set_picture(profile_pic, "picture");
                        Utils.set_name(profile_name);
                        edit_profile.setVisibility(View.VISIBLE);
                        profile_name.setVisibility(View.VISIBLE);
                        edit_name.setVisibility(View.GONE);
                        close_profile.setVisibility(View.GONE);
                        check_profile.setVisibility(View.GONE);
                        btn_profile_name.setVisibility(View.GONE);
                        profile_pic2.setVisibility(View.GONE);
                        profile_pic.setAlpha((float) 1);
                    } else {
                        Toast.makeText(ctx, "you must enter an username and take a picture", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        close_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setVisibility(View.GONE);
                profile_name.setVisibility(View.VISIBLE);
                close_profile.setVisibility(View.GONE);
                check_profile.setVisibility(View.GONE);
                edit_profile.setVisibility(View.VISIBLE);
                btn_profile_name.setVisibility(View.GONE);
                profile_pic2.setVisibility(View.GONE);
                Utils.set_picture(profile_pic,"picture");
                profile_pic.setAlpha((float) 1);
            }
        });
        profile_pic2.setOnClickListener(new OnClickCamera());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OnClickCamera.CAM_REQUEST && data!=null){
            bmp = (Bitmap)data.getExtras().get("data");
            profile_pic.setImageBitmap(bmp);
        }
    }

    class OnClickCamera implements View.OnClickListener {
        public static final int CAM_REQUEST=1313;

        @Override
        public void onClick(View v) {
            Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(CameraIntent,CAM_REQUEST);

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
