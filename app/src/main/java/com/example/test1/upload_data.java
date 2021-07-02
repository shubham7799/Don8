package com.example.test1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;

public class upload_data extends AppCompatActivity {
    Button btnLocation;
    Button btnbrowse, btnupload;
    EditText txtdata;
    EditText txtdata2;
    EditText txtdata3;
    ImageView imgview;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    public static final int Image_Request_Code = 100;
    public static final int REQUEST_PERMISSION = 100;
    public static String LOCATION="";
    ProgressDialog progressDialog;
    private String imageFilePath = "";
    Uri photoUri;
    TextView address;
    @Override
    protected void onResume() {
        super.onResume();
        if(!LOCATION.equals("")){
            address.setText(LOCATION);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Food Donations");
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnupload= (Button)findViewById(R.id.btnupload);
        txtdata = (EditText)findViewById(R.id.txtdata);
        txtdata2 = (EditText)findViewById(R.id.txtdata2);
        txtdata3 = (EditText)findViewById(R.id.txtdata3);
        imgview = (ImageView)findViewById(R.id.image_view);
        btnLocation = (Button)findViewById(R.id.btnlocation);
        address=(TextView)findViewById(R.id.textView);

        progressDialog = new ProgressDialog(upload_data.this);
        // context name as per your project name

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(upload_data.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(upload_data.this,MapActivity.class));
                }
                else {
                    startActivity(new Intent(upload_data.this,PermissionActivity.class));
                }

            }
        });
        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
    }
    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            File photofile=null;
            try{
                photofile = createImageFile();
            }
            catch (IOException e){
                e.printStackTrace();
                return;
            }
            photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +".provider",photofile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(intent, Image_Request_Code);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code) {
            if (resultCode == RESULT_OK) {
                imgview.setImageURI(Uri.parse(imageFilePath));
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
    public void UploadImage() {
        if (photoUri != null) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(photoUri));
            storageReference2.putFile(photoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    String TempImageName = txtdata.getText().toString().trim();
                                    String TempImageName2 = txtdata2.getText().toString().trim();
                                    String TempImageName3 = txtdata3.getText().toString().trim();
                                    uploadinfo imageUploadInfo = new uploadinfo(TempImageName,TempImageName2,TempImageName3,MainActivity.user,LOCATION,url);
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(TempImageName+ImageUploadId).setValue(imageUploadInfo);
                                    LOCATION="";
                                    onBackPressed();
                                }
                            });

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=( (100.0)*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else
        {
            Toast.makeText(upload_data.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }
}