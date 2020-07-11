package com.fbu.instagrom.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fbu.instagrom.databinding.ActivitySetProfilePicBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SetProfilePicActivity extends AppCompatActivity {
    private static final String TAG = "SetProfilePic";
    private final static int CAMERA_REQUEST_CODE = 45;
    private final static int GALLERY_REQUEST_CODE = 46;
    private Context context;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    private ActivitySetProfilePicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetProfilePicBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        binding.galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto(view);
            }
        });

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if photofile empty
                if (photoFile == null || binding.profileImageView.getDrawable() == null) {
                    Toast.makeText(SetProfilePicActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SetProfilePicActivity.this, "Change profile picture successful!", Toast.LENGTH_SHORT).show();
                uploadImage(photoFile);
                finish();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                binding.profileImageView.setImageBitmap(takenImage);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if ((data != null) && requestCode == GALLERY_REQUEST_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            // Load the selected image into a preview
            binding.profileImageView.setImageBitmap(selectedImage);
            photoFile = bitmapToFile(selectedImage);
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            Toast.makeText(this, "Picture wasn't selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void uploadImage(File photoFile) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profilePic", new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(SetProfilePicActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was success!!");
            }
        });
    }

    public File bitmapToFile(Bitmap bitmap) {
        //create a file to write bitmap data
        File f = new File(this.getCacheDir(), photoFileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }
}