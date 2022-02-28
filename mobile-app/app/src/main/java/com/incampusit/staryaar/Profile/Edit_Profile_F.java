package com.incampusit.staryaar.Profile;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.API_CallBack;
import com.incampusit.staryaar.SimpleClasses.ApiRequest;
import com.incampusit.staryaar.SimpleClasses.Callback;
import com.incampusit.staryaar.SimpleClasses.Fragment_Callback;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.incampusit.staryaar.Main_Menu.MainMenuFragment.hasPermissions;

//import com.squareup.picasso.Picasso;


/*
 * A simple {@link Fragment} subclass.
 */
public class Edit_Profile_F extends RootFragment implements View.OnClickListener {

    final Calendar myCalendar = Calendar.getInstance();
    View view;
    Context context;
    Fragment_Callback fragment_callback;
    ImageView profile_image;
    EditText firstname_edit, lastname_edit, user_bio_edit, user_handle_edit, user_dob;
    EditText etfacebook, etYoutube, etTwitter, etInstagram;
    EditText etEmail, etPhone, etDob;
    RadioButton male_btn, female_btn, others_btn;
    String imageFilePath;
    byte[] image_byte_array;

    public Edit_Profile_F() {

    }

    public Edit_Profile_F(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context = getContext();


        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.save_btn).setOnClickListener(this);
        view.findViewById(R.id.upload_pic_btn).setOnClickListener(this);

        profile_image = view.findViewById(R.id.profile_image);
        firstname_edit = view.findViewById(R.id.firstname_edit);
        lastname_edit = view.findViewById(R.id.lastname_edit);
        user_bio_edit = view.findViewById(R.id.user_bio_edit);
        user_handle_edit = view.findViewById(R.id.userhandle_edit);

        // Social Media
        etfacebook = view.findViewById(R.id.user_facebook_link);
        etYoutube = view.findViewById(R.id.user_youtube_link);
        etTwitter = view.findViewById(R.id.user_twitter_link);
        etInstagram = view.findViewById(R.id.user_instagram_link);
        // Social Media

        //Personal Details
        etEmail = view.findViewById(R.id.user_email_address);
        etPhone = view.findViewById(R.id.user_phone_number);
        etDob = view.findViewById(R.id.user_date_of_birth);
        //Personal Details

        setDateofBirthPicker();

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_handle_edit.setText(bundle.getString("userhandle"));
            user_bio_edit.setText(bundle.getString("bio"));

            // Social Media
            etfacebook.setText(bundle.getString("facebook"));
            etYoutube.setText(bundle.getString("youtube"));
            etTwitter.setText(bundle.getString("twitter"));
            etInstagram.setText(bundle.getString("instagram"));
            // Social Media

            //Personal Details
            etEmail.setText(bundle.getString("email"));
            etPhone.setText(bundle.getString("phone"));
            etDob.setTag(bundle.getString("dob"));

            String myFormat = "MMMM dd, yyyy"; //In which you need put here
            SimpleDateFormat sdf;
            if (bundle.getString("dob") != null && !bundle.getString("dob").isEmpty()) {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date dbDate = null;
                try {
                    dbDate = sdf.parse(bundle.getString("dob"));
                    sdf = new SimpleDateFormat(myFormat, Locale.US);
                    user_dob.setText(sdf.format(dbDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //Personal Details

            bundle.getString("gender");
        }

        firstname_edit.setText(Variables.sharedPreferences.getString(Variables.f_name, ""));
        lastname_edit.setText(Variables.sharedPreferences.getString(Variables.l_name, ""));

        /* replace picasso with glide
        Picasso.with(context)
                .load(Variables.sharedPreferences.getString(Variables.u_pic, ""))
                .placeholder(R.drawable.profile_image_placeholder)
                .resize(200, 200)
                .centerCrop()
                .into(profile_image);
         */
        Glide.with(context)
                .load(Variables.sharedPreferences.getString(Variables.u_pic, ""))
                .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                .centerCrop()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .into(profile_image);


        male_btn = view.findViewById(R.id.male_btn);
        female_btn = view.findViewById(R.id.female_btn);
        others_btn = view.findViewById(R.id.others_btn);

        Call_Api_For_User_Details();

        return view;
    }

    private void setDateofBirthPicker() {
        view.findViewById(R.id.user_date_of_birth).setOnClickListener(this);
        user_dob = view.findViewById(R.id.user_date_of_birth);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                user_dob.setText(sdf.format(myCalendar.getTime()));

                // Put db format
                myFormat = "yyyy-MM-dd";
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                user_dob.setTag(sdf.format(myCalendar.getTime()));
            }

        };

        user_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), R.style.DatePickerDialog, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Goback:

                getActivity().onBackPressed();
                break;

            case R.id.save_btn:
                if (Check_Validation()) {

                    Call_Api_For_Edit_profile();
                }
                break;

            case R.id.upload_pic_btn:
                selectImage();
                break;
            case R.id.user_date_of_birth:
                break;
        }
    }

    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (check_permissions())
                        openCameraIntent();

                } else if (options[item].equals("Choose from Gallery")) {

                    if (check_permissions()) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2);
        } else {

            return true;
        }

        return false;
    }

    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));

                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

                Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, (int) (rotatedBitmap.getWidth() * 0.7), (int) (rotatedBitmap.getHeight() * 0.7), true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                image_byte_array = baos.toByteArray();

                Save_Image();

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);


                Bitmap resized = Bitmap.createScaledBitmap(rotatedBitmap, (int) (rotatedBitmap.getWidth() * 0.5), (int) (rotatedBitmap.getHeight() * 0.5), true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                image_byte_array = baos.toByteArray();

                Save_Image();

            }

        }

    }

    // this will check the validations like none of the field can be the empty
    public boolean Check_Validation() {
        String firstname = firstname_edit.getText().toString();
        String lastname = lastname_edit.getText().toString();
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String MOBILE_STRING = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

        if (TextUtils.isEmpty(firstname)) {
            return false;
        } //else return !TextUtils.isEmpty(lastname);
        if (!TextUtils.isEmpty(etEmail.getText().toString())) {
            if (!Pattern.compile(EMAIL_STRING).matcher(etEmail.getText().toString()).matches()) {
                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (!TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            if (!Pattern.compile(MOBILE_STRING).matcher(etPhone.getText().toString()).matches()) {
                Toast.makeText(context, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (!TextUtils.isEmpty(etDob.getText().toString().trim())) {

        }

        return true;
    }

    public void Save_Image() {

        Functions.Show_loader(context, false, false);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.push().getKey();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference filelocation = storageReference.child("User_image")
                .child(key + ".jpg");

        filelocation.putBytes(image_byte_array).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    filelocation.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Call_Api_For_image(uri.toString());
                        }
                    });
                } else {
                    Functions.cancel_loader();
                }
            }
        });


    }


    public void Call_Api_For_image(final String image_link) {


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("image_link", image_link);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.uploadImage, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {

                        Variables.sharedPreferences.edit().putString(Variables.u_pic, image_link).commit();
                        Profile_F.pic_url = image_link;
                        Variables.user_pic = image_link;

                        /* Replace picasso with glide
                        Picasso.with(context)
                                .load(Profile_F.pic_url)
                                .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                .resize(200, 200).centerCrop().into(profile_image);
                         */
                        Glide.with(context)
                                .load(Profile_F.pic_url)
                                .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                                .centerCrop()
                                .into(profile_image);

                        Toast.makeText(context, "Image Update Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    // this will update the latest info of user in database
    public void Call_Api_For_Edit_profile() {

        Functions.Show_loader(context, false, false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("first_name", firstname_edit.getText().toString());
            parameters.put("last_name", lastname_edit.getText().toString());

            if (male_btn.isChecked()) {
                parameters.put("gender", "Male");
            } else if (female_btn.isChecked()) {
                parameters.put("gender", "Female");
            } else if (others_btn.isChecked()) {
                parameters.put("gender", "Others");
            }

            parameters.put("bio", user_bio_edit.getText().toString());
            //Social Media
            parameters.put("facebook", etfacebook.getText().toString());
            parameters.put("youtube", etYoutube.getText().toString());
            parameters.put("instagram", etInstagram.getText().toString());
            parameters.put("twitter", etTwitter.getText().toString());
            //Social Media

            //Personal Details
            parameters.put("email", etEmail.getText().toString());
            parameters.put("phone", etPhone.getText().toString());
            parameters.put("dob", user_dob.getTag().toString());
            //Personal Details

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.edit_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {

                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();

                        editor.putString(Variables.f_name, firstname_edit.getText().toString());
                        editor.putString(Variables.l_name, lastname_edit.getText().toString());
                        editor.commit();

                        Variables.user_name = firstname_edit.getText().toString() + " " + lastname_edit.getText().toString();

                        getActivity().onBackPressed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    // this will get the user data and parse the data and show the data into views
    public void Call_Api_For_User_Details() {
        Functions.Show_loader(getActivity(), false, false);
        Functions.Call_Api_For_Get_User_data(getActivity(),
                Variables.sharedPreferences.getString(Variables.u_id, ""),
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {

                    }

                    @Override
                    public void OnSuccess(String responce) {
                        Functions.cancel_loader();
                        Parse_user_data(responce);
                    }

                    @Override
                    public void OnFail(String responce) {

                    }
                });
    }

    public void Parse_user_data(String responce) {
        try {
            JSONObject jsonObject = new JSONObject(responce);

            String code = jsonObject.optString("code");

            if (code.equals("200")) {
                JSONArray msg = jsonObject.optJSONArray("msg");
                JSONObject data = msg.getJSONObject(0);

                firstname_edit.setText(data.optString("first_name"));
                lastname_edit.setText(data.optString("last_name"));

                String picture = data.optString("profile_pic");

                /* replace picasso with glide
                Picasso.with(context)
                        .load(picture)
                        .placeholder(R.drawable.profile_image_placeholder)
                        .into(profile_image);
                 */
                Glide.with(context)
                        .load(picture)
                        .placeholder(context.getResources().getDrawable(R.drawable.star_home))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                        .centerCrop()
                        .into(profile_image);

                String gender = data.optString("gender");
                if (gender.equals("Male")) {
                    male_btn.setChecked(true);
                } else {
                    female_btn.setChecked(true);
                }

                user_bio_edit.setText(data.optString("bio"));
            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (fragment_callback != null)
            fragment_callback.Responce(new Bundle());
    }
}
