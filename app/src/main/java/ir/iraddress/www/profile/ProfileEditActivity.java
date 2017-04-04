package ir.iraddress.www.profile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.iraddress.www.R;

public class ProfileEditActivity extends ProfileMainActivity {

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText mobile;
    TextInputEditText email;
    CircleImageView profileImage;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        firstName = (TextInputEditText) findViewById(R.id.firstName);
        lastName = (TextInputEditText) findViewById(R.id.lastName);
        mobile = (TextInputEditText) findViewById(R.id.mobile);
        email = (TextInputEditText) findViewById(R.id.email);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        try {
            firstName.setText(user.getString("firstName"));
            lastName.setText(user.getString("lastName"));
            mobile.setText(user.getString("mobile"));
            email.setText(user.getString("email"));
            Picasso.with(context).load(user.getString("avatar")).centerCrop().fit().into(profileImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void saveProfile(View view) throws JSONException {

        RequestParams profileEdit = new RequestParams();

        profileEdit.put("firstName", firstName.getText().toString().trim());
        profileEdit.put("lastName", firstName.getText().toString().trim());
        profileEdit.put("mobile", firstName.getText().toString().trim());
        profileEdit.put("token", user.getString("token"));

        putRequest("users/"+user.getInt("id")+"/profile", params);

    }

    public void selectProfileImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                System.out.println("Uri: " + uri.toString());
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println(picturePath);

                RequestParams params = new RequestParams();
                try {

                    params.put("avatar", new File(picturePath));
                    try {
                        upload("users/"+user.getInt("id")+"/avatar", params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void callback(JSONObject response, int statusCode, String method){

    }
}
