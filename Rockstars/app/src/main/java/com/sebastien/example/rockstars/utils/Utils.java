package com.sebastien.example.rockstars.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sebastien.example.rockstars.R;
import com.sebastien.example.rockstars.fragment.BookmarksFragment;
import com.sebastien.example.rockstars.stars.Stars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * Created by Sebastien on 25/03/2016.
 */

public class Utils {
    private BookmarksFragment bookmarksfragment;
    private static FileOutputStream dataJsonStream;
    private static FileInputStream dataJsonStream2;


    private static final SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * SharedPreference
     *
     * @param context
     * @param key
     * @return
     */


    public static final int getIntValue(Context context, String key) {
        return getSharedPreference(context).getInt(key, 0);
    }

    /**
     * SharedPreference
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static final boolean putIntValue(Context context, String key,
                                            int value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }

    /**
     * SharedPreference
     *
     * @param context
     * @param key
     * @return
     */

    public static final Boolean getBooleanValue(Context context, String key) {
        return getSharedPreference(context).getBoolean(key, false);
    }

    /**
     * SharedPreference
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static final boolean putBooleanValue(Context context, String key,
                                            boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }
    /**
     * JSON OBJECT
     *
     * @param jobjparent
     * @param Stars_list_item
     * @return
     */

    public static Boolean generate_rockstars_from_jsonObject(JSONObject jobjparent, ArrayList<Stars> Stars_list_item) {
        Stars_list_item.clear();
        JSONArray jarray = null;
        //Here we verify that the jobjparent is not null, it means that if the data were been collected from the server
        if(jobjparent!=null) {
            try {
                jarray = jobjparent.getJSONArray("contacts");
                //We create a loop to parse the JSON ARRAY
                for (int i = 0; i < jarray.length(); i++) {

                    try {
                        //We get each Json Object on the Json Array
                        JSONObject jobj = jarray.getJSONObject(i);
                        // We use this function to create the different Stars Object and add them into the Stars ArrayList
                        Stars_list_item.add(new Stars(jobj.getInt("id"), jobj.getString("hisface"), jobj.getString("fullname"), jobj.getString("status"), jobj.getBoolean("bookmark")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * JSON ARRAY
     *  @param jobjparents
     * @param Bookmarks_list_item
     * @return
     */

    public static Boolean generate_bookmarks_from_jsonObject(JSONObject jobjparents, ArrayList<Stars> Bookmarks_list_item) {
        if(jobjparents != null) {
            Bookmarks_list_item.clear();
            JSONArray jarray = null;
            try {
                jarray = jobjparents.getJSONArray("contacts");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //We create a loop to parse the JSON ARRAY
            for (int i = 0; i < jarray.length(); i++) {

                try {
                    //We get each Json Object on the Json Array
                    JSONObject jobj = jarray.getJSONObject(i);
                    // We use this function to create the different Stars Object and add them into the Stars ArrayList
                    if (jobj.getBoolean("bookmark")) {
                        Bookmarks_list_item.add(new Stars(jobj.getInt("id"), jobj.getString("hisface"), jobj.getString("fullname"), jobj.getString("status"), jobj.getBoolean("bookmark")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;

    }
    /**
     * JSON ARRAY
     *@return
     */

    public static JSONObject create_json_file() throws JSONException {
        JSONObject jsonparents= new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for(int i =0 ;i<11;i++){
            jsonObject = new JSONObject();
            //jsonObject.put("user_id",Utils.getValue(ctx, "userId"));
            try {
                jsonObject.put("id", i);
                jsonObject.put("hisface", "sebastien.jpg");
                jsonObject.put("fullname", "name_"+i);
                jsonObject.put("status", "status_"+i);
                if(i%2!=0){
                    jsonObject.put("bookmark", true);
                }else{
                    jsonObject.put("bookmark", false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            jsonparents.put("contacts",jsonArray);
        }
        return jsonparents;

    }
    /**
     * JSON FILE
     *
     * @param ctx
     * @param jobjparent
     */

    public static void writeJSONFileToSD(Context ctx,JSONObject jobjparent,String name) {
        JSONArray jsonArray= null;
        try {
            jsonArray = jobjparent.getJSONArray("contacts");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File path = null;
        File file = null;
        try {
            String pathName = Environment.getExternalStorageDirectory() + File.separator + "Rockstars"+ File.separator;
            String fileName = name+".json";

            path = new File(pathName);
            file = new File(pathName + fileName);
            //store file path
            if( !path.exists()) {
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if( !file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }else{
                Log.d("TestFile", "Update the file:" + fileName);
                file.delete();
                file.createNewFile();
            }

            try {
                dataJsonStream = new FileOutputStream(file);
                dataJsonStream.write(jobjparent.toString().getBytes("utf-8"));
                //Toast.makeText(ctx, "Json File Created", Toast.LENGTH_SHORT).show();


            }catch (Exception e){
                //Toast.makeText(ctx, "Error Json File", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch(Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            //Toast.makeText(ctx, "Error on writeFilToSD.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    /**
     * Activity
     *
     * @param activity
     * @param cls
     */
    public static void startActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        //activity.overridePendingTransition(R.anim.enter_from_left,
        //        R.anim.exit_to_right);

    }

    /**
     * JSON FILE
     *
     * @param fileName
     */

    public static JSONObject readFileFromSD(String fileName) {
        JSONObject jobj =null;
        try {
            String pathName = Environment.getExternalStorageDirectory() + File.separator + "Rockstars" + File.separator;
            //String fileName = "data.json";
            File path = new File(pathName);
            File file = new File(pathName + fileName);


            try {
                dataJsonStream2 = new FileInputStream(file);
                String responseString = readInputStream(dataJsonStream2);
                jobj = new JSONObject(responseString);


            }catch (Exception e){
                //Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch(Exception e) {
            Log.e("TestFile", "Error on readFileFromSD.");
            e.printStackTrace();
        }
        return jobj;

    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream, "UTF-8"));
        String tmp;
        StringBuilder sb = new StringBuilder();
        while ((tmp = reader.readLine()) != null) {
            sb.append(tmp).append("\n");
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Image
     *
     * @param bitmap
     * @param imageFileName
     */
    public static void createImageFile(Bitmap bitmap,String imageFileName) throws IOException {
        // Create an image file name
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80, bytes);

        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Rockstars" + File.separator);

        if(storageDir.exists() && storageDir.listFiles()!=null) {
            ArrayList<File> files = new ArrayList<File>(Arrays.asList(storageDir.listFiles()));
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).getName().contains(imageFileName)) {
                    files.get(i).delete();
                }
            }
        }else{
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */

        );

        FileOutputStream stream = new FileOutputStream(image);
        stream.write(bytes.toByteArray());
        stream.close();
    }

    /**
     * Image
     *
     * @param image
     * @return
     */

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        int options = 30;
        while (baos.toByteArray().length / 1024 > 100) {
            options -= 10;
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * Image
     *
     * @param img
     * @param name
     */

    public static void set_picture(ImageView img,String name){
        Bitmap bitmap =null;
        File file =null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            String pathName = Environment.getExternalStorageDirectory() + File.separator + "Rockstars" + File.separator;
            File path = new File(pathName);
            ArrayList<File> files = new ArrayList<File>(Arrays.asList(path.listFiles()));
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).getName().contains(name)) {
                    file = files.get(i);
                }
            }
            if (file != null) {
                bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                img.setImageBitmap(bitmap);
            }

        }catch (Exception e){
                e.printStackTrace();
            }

    }

    /**
     * JSON FILE
     *
     * @param txt
     */
    public static void set_name(TextView txt){
        JSONObject jobject =null;
        File file =null;
        try {
            String pathName = Environment.getExternalStorageDirectory() + File.separator + "Rockstars" + File.separator;
            File path = new File(pathName);

            ArrayList <File> files = new ArrayList<File>(Arrays.asList(path.listFiles()));
            for(int i=0;i<files.size();i++){
                if( files.get(i).getName().contains("user.json")) {
                    file = files.get(i);
                }
            }
            try {
                if (file!=null) {
                    dataJsonStream2 = new FileInputStream(file);
                    String responseString = readInputStream(dataJsonStream2);
                    jobject = new JSONObject(responseString);
                    JSONArray jarray= jobject.getJSONArray("user");
                    String name = jarray.getJSONObject(0).get("username").toString();
                    txt.setText(name);
                }else{
                    txt.setText("username");
                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * JSON FILE
     *
     * @param edit_name
     * @return
     */

    public static JSONObject upate_json_user(EditText edit_name){
        JSONObject juser = new JSONObject();
        JSONArray jarray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        //jsonObject.put("user_id",Utils.getValue(ctx, "userId"));
        try {
            jsonObject.put("username", edit_name.getText().toString());
            jarray.put(jsonObject);
            juser.put("user", jarray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return juser;
    }
    /**
     * Image
     *
     * @param Url
     * @param name
     */

    public static void get_picture_from_server(String Url,String name){
        try {
            URL url=new URL(Url+name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(stream);
            Bitmap bitmap = compressImage(bmp);
            createImageFile(bitmap,name);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * JSON OBJECT
     *
     * @param Url_image
     * @param Url_json
     * @param LastJsonObectParent
     * @return
     */

    public static JSONObject get_json_from_server(String Url_image,String Url_json,JSONObject LastJsonObectParent){
        ArrayList<String> Last_bookmark_name = new ArrayList<String>();
        if(LastJsonObectParent!=null) {
            // Before to get new json array , we create an ArrayList to store the current bookmarks name

            try {
                JSONArray last_json_array = LastJsonObectParent.getJSONArray("contacts");
                for (int i = 0; i < last_json_array.length(); i++) {
                    JSONObject last_json_bookmark_object = last_json_array.getJSONObject(i);
                    if (last_json_bookmark_object.getBoolean("bookmark")) {

                        Last_bookmark_name.add(last_json_bookmark_object.getString("fullname"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Here we create the connection with the server by the URL
        URL url ;
        HttpURLConnection connection =null;
        BufferedReader reader = null;
        try {
            url = new URL(Url_json);
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line="";
            while((line=reader.readLine())!=null){
                buffer.append(line);
            }
            JSONObject jobjparent = new JSONObject(buffer.toString());
            JSONArray jarray = jobjparent.getJSONArray("contacts");
            //We parse the Json Array to add some objects, that we will use for match data in our Bookmarks list and RockStars list
            for(int i=0;i<jarray.length();i++){
                JSONObject jobj= jarray.getJSONObject(i);
                String fullname=jobj.getString("firstname")+" "+jobj.getString("lastname");
                jobj.put("id",i);
                //Here we parse the arraylist of last_bookmark_name to verify if the new rockstars is in the last bokkmarks list, and if its true we put the object bookmark with true value
                if(already_bookmarked(fullname,Last_bookmark_name)){
                    jobj.put("bookmark",true);
                }else{
                    jobj.put("bookmark",false);
                }
                jobj.put("fullname",fullname);
                jobj.remove("firstname");
                jobj.remove("lastname");
                get_picture_from_server(Url_image, jobj.getString("hisface"));
            }


            Log.d("bookmarks",""+jarray);
            return jobjparent;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection !=null){
                connection.disconnect();
            }
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Bookmark
     *
     * @param fullname
     * @param Last_bookmark_name
     * @return
     */

    private static Boolean already_bookmarked(String fullname,ArrayList<String> Last_bookmark_name){
        Boolean value =false;
        for (String string : Last_bookmark_name) {
            if(string.contains(fullname)) {
                value = true;
            }
        }
        return value;
    }

}
