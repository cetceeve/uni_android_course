package android.mi.ur.de.imagecropper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

   CropImageView mCropImageView;
   ImageView mCroppedImageView;
   private String mCurrentPhotoPath;
   private static final int REQUEST_IMAGE_CAPTURE = 0;
   private static final int REQUEST_IMAGE_FROM_GALLERY = 1;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      // Hide button for taking an image when no camera is present
      if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
         Toast.makeText(this, "Your device doesn't have a camera!", Toast.LENGTH_LONG).show();
         Button takeImageButton = (Button) findViewById(R.id.button_takeImage);
         takeImageButton.setVisibility(View.GONE);
      }
       mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
      // Get the intent that started this activity
      Intent intent = getIntent();
      if (intent != null) {
         // TODO Check intent type, set variables and load image to mCropImageView
         Uri data = intent.getData();

         try {
             if (intent.getType().contains("image/*")) {
                 if (data != null) {
                     mCurrentPhotoPath = data.toString();
                     Bitmap bitmap = loadBitmap(data);
                     if (bitmap != null) {
                         mCropImageView.setImageBitmap(bitmap);
                     }
                 }
             }
         } catch (NullPointerException e) {
             e.printStackTrace();
         }
      }
   }

   private Bitmap loadBitmap(Uri uri) {
       InputStream inputStream = null;
       try {
           inputStream = getContentResolver().openInputStream(uri);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       return BitmapFactory.decodeStream(inputStream);
   }


   /**
    * Crops an image
    *
    * @param view
    */
   public void cropImage(View view) {
      mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
      mCroppedImageView = (ImageView) findViewById(R.id.croppedImageView);
      mCroppedImageView.setImageBitmap(mCropImageView.getCroppedBitmap());
   }

   /**
    * @param view
    */
   public void takeImage(View view) {
      // TODO Use the devices camera app to take a photo
       Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
           File photoFile = null;
           try {
               photoFile = createImageFile();
           } catch (IOException e) {
               e.printStackTrace();
           }
           if (photoFile != null) {
               Uri uri = null;
               try {
                   uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, createImageFile());
               } catch (IOException e) {
                   e.printStackTrace();
               }
               takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
               startActivityForResult(takeImageIntent, REQUEST_IMAGE_CAPTURE);
           }
       }

   }

   private File createImageFile() throws IOException {
      // Create an image file name
      String timeStamp =
            new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      String imageFileName = "JPEG_" + timeStamp + ".jpg";
      File storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      if (!storageDir.exists()) {
         Log.i(MainActivity.class.getName(),
               "Directory creation was needed and was successfull: " + storageDir.mkdirs());
      }
      File imageFile = new File(storageDir, imageFileName);
      // Save a file: path for use with ACTION_VIEW intents
      mCurrentPhotoPath = imageFile.getAbsolutePath();
      return imageFile;
   }

   /**
    * Adds the taken photo to the gallery.
    */
   private void galleryAddPic() {
      // Skip if no file (only thumbnail) is there
      if (mCurrentPhotoPath == null) {
         return;
      }
      Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      File file = new File(mCurrentPhotoPath);
      Uri contentUri = Uri.fromFile(file);
      mediaScanIntent.setData(contentUri);
      this.sendBroadcast(mediaScanIntent);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO check requests (either image from camera or gallery), load the image to
       if (resultCode == RESULT_OK) {
           if (requestCode == REQUEST_IMAGE_CAPTURE) {
               galleryAddPic();
               Bitmap bm = loadBitmap(Uri.parse(mCurrentPhotoPath));
               if (bm != null) {
                   mCropImageView.setImageBitmap(bm);
               }
               if (data != null) {
                   Bundle bitmapData = data.getBundleExtra("data");
                   Bitmap bitmap = (Bitmap) bitmapData.get("data");
                   if (bitmap != null) {
                       mCropImageView.setImageBitmap(bitmap);
                   }
               }
           }
           if (requestCode == REQUEST_IMAGE_FROM_GALLERY) {
               if (data != null) {
                   Uri uri = data.getData();
                   Bitmap bitmap = loadBitmap(uri);
                   if (bitmap != null) {
                       mCropImageView.setImageBitmap(bitmap);
                   }
               }
           }
       }
   }

   /**
    * @param view
    */
   public void chooseImageFromGallery(View view) {
      // TODO let the user choose an image from the gallery
       Intent chooseImageFromGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
       chooseImageFromGalleryIntent.setType("image/*");
       startActivityForResult(chooseImageFromGalleryIntent, REQUEST_IMAGE_FROM_GALLERY);
   }

   /**
    * @param view
    */
   public void saveCroppedImage(View view) {
      mCroppedImageView = (ImageView) findViewById(R.id.croppedImageView);
      Drawable drawable = mCroppedImageView.getDrawable();
      if ((mCurrentPhotoPath == null) || (drawable == null)) {
         Toast.makeText(this, "No image loaded or not cropped yet!", Toast.LENGTH_SHORT).show();
         return;
      }
      Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
      int nameSeparatorIndex = mCurrentPhotoPath.lastIndexOf("/");
      String fileNameAndExtension =
            mCurrentPhotoPath.substring(nameSeparatorIndex + 1, mCurrentPhotoPath.length());
      int extSeparatorIndex = fileNameAndExtension.lastIndexOf(".");
      // If the original file doesn't have an extension
      if (extSeparatorIndex == -1) {
         // Using the length as index means no splitting of the String
         extSeparatorIndex = fileNameAndExtension.length();
      }
      String fileName = fileNameAndExtension.substring(0, extSeparatorIndex);
      String fileExtension =
            fileNameAndExtension.substring(extSeparatorIndex, fileNameAndExtension.length());
      // Assume JPG if no extension is provided
      if (fileExtension.isEmpty()) {
         fileExtension = ".jpg";
      }
      String imageFileName = fileName + "_cropped" + fileExtension;
      File storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      if (!storageDir.exists()) {
         Log.i(MainActivity.class.getName(),
               "Directory creation was needed and was successfull: " + storageDir.mkdirs());
      }
      // http://stackoverflow.com/a/673014/3992979
      File imageFile = new File(storageDir, imageFileName);
      FileOutputStream outFile = null;
      if (!overwriteFile(imageFile)) {
         return;
      }
      try {
         outFile = new FileOutputStream(imageFile);
         // http://stackoverflow.com/a/8306683/3992979
         // Use JPG encoding as default
         Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
         if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg")) {
            compressFormat = Bitmap.CompressFormat.JPEG;
         } else if (fileExtension.equalsIgnoreCase(".png")) {
            compressFormat = Bitmap.CompressFormat.PNG;
         } else if (fileExtension.equalsIgnoreCase(".webp")) {
            compressFormat = Bitmap.CompressFormat.WEBP;
         }
         bmp.compress(compressFormat, 100, outFile);
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (outFile != null) {
               outFile.flush();
               outFile.close();
               Toast.makeText(this, imageFile.getAbsolutePath() + " written", Toast.LENGTH_SHORT)
                     .show();
            }
         } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error writting file!", Toast.LENGTH_SHORT).show();
         }
      }

   }

   /**
    * Checks if the file is already present and asks the user if he wants to overwrite the file in
    * that case.
    *
    * @param imageFile the file to check if it should be overwritten (if already present)
    * @return <code>true</code> if the file doesn't exist already or the user wants to overwrite it
    * if it is already present or <code>false</code> if it is present and it should NOT be
    * overwritten
    */
   private boolean overwriteFile(File imageFile) {
      final boolean[] overwriteFile = {false};
      if (imageFile.exists() && !imageFile.isDirectory()) {
         // http://stackoverflow.com/a/2478662/3992979
         DialogInterface.OnClickListener dialogClickListener =
               new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                     switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                           //Yes button clicked
                           overwriteFile[0] = true;
                           break;
                        case DialogInterface.BUTTON_NEGATIVE:
                           //No button clicked
                           overwriteFile[0] = false;
                           break;
                        default:
                           break;
                     }
                  }
               };
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage(getString(R.string.dialog_overwrite))
               .setPositiveButton("Yes", dialogClickListener)
               .setNegativeButton("No", dialogClickListener).show();
         // File is not present
      } else {
         overwriteFile[0] = true;
      }
      return overwriteFile[0];
   }

}
