package ir.iraddress.www.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPrefered {

    private String key;
    private Context context;
    public JSONArray data;

    public SharedPrefered(Context context, String Key) throws JSONException {
        this.context = context;
        this.key = Key;
        this.data = getData();
    }

    private SharedPreferences getPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public JSONArray getData() throws JSONException {

        String bookmarkedData = getPreference(context).getString(key, "");

        if(!bookmarkedData.isEmpty()){
            JSONArray collection = new JSONArray(bookmarkedData);
            return collection;
        }

        return new JSONArray();
    }

    public int getIndex(Integer id) throws JSONException {

        for(int n = 0; n < count(); n++ ){
            JSONObject object = (JSONObject) getData().get(n);
            if(object.getInt("id") == id){
                return n;
            }
        }

        return -1;
    }

    public JSONObject findByIndex(Integer index) throws JSONException {
        return (JSONObject) getData().get(index);
    }

    public Integer count() throws JSONException {
        return getData().length();
    }

    public void store(JSONObject object) throws JSONException {
        data.put(object);
        getPreference(context).edit().putString(key, data.toString()).apply();
    }

    public void setData(JSONArray jsonArray ){
        getPreference(context).edit().putString(key, jsonArray.toString()).apply();
    }

    public void destroy(Integer index) throws JSONException {
        JSONArray collection = getData();
        if(count() > 0 ){
            System.out.println("test arta remove");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                collection.remove(index);
                getPreference(context).edit().putString(key, collection.toString()).apply();
            }
        }
    }

    public void empty() throws JSONException {
        JSONArray collection = new JSONArray();
        this.data = new JSONArray();
        getPreference(context).edit().remove(key).apply();
    }

    public void merge(){

    }

    public void removeFirst(Integer count) throws JSONException {

        if(count <= count()){
            destroy(0);
        }
    }

    public JSONArray reverse() throws JSONException {

        JSONArray jsonArray = new JSONArray();

        int length = count()-1;

        for(int i = length; i >= 0;i--){
            jsonArray.put(getData().get(i));
        }

        return jsonArray;
    }

    public JSONArray latest(int number) throws JSONException {

        JSONArray jsonArray = new JSONArray();

        int n = count() - number;

        if(count() > number){
            System.out.println("ARTA LIST LATES");
            for(int i = n ; i < count() ;i++){
                jsonArray.put(getData().get(i));
            }

            System.out.println(jsonArray);

            setData(jsonArray);
        }

        return jsonArray;
    }

}
