package com.baeflower.hello.camera.intent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.baeflower.hello.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class IntentCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private String mCurrentPhotoPath;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_camera);

        findViewById(R.id.btn_camera_execute).setOnClickListener(this);

        mImageView = (ImageView) findViewById(R.id.iv_image_taken_from_Camera);
    }


    @Override
    public void onClick(View v) {

        /*
            예제 1.

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE); // 이미지 캡처를 할 수 있는 앱들이 뜰 것이여

            // 사진 폴더 가져와서 파일 저장
            // File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "demo.png");
            mUri = Uri.fromFile(file);

            // 파일로 부터 Uri를 가져와서
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            // 주고
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        */

        /*
            예제 2.
            http://ismydream.tistory.com/127

         */

        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( isIntentAvailable( getApplicationContext(), MediaStore.ACTION_IMAGE_CAPTURE)){
            try{
                File f = createImageFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                // 이미지가 저장될 파일은 카메라 앱이 구동되기 전에 세팅해서 넘겨준다.
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }catch( IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 받고
     * 카메라로 찍은 이미지 데이터 받기
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
            예제 1.
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && mUri != null) {
                try {
                    // Content Provider
                    // data.getData()를 하면 위에서 만들어진 Uri가 들어올 것 (파일경로?)
                    // 근데 이렇게 했을 때 null로 들어올 수 있음. 그래서 uri를 전역으로 만들어 놓고 사용...
                    Bitmap bitImage = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                    mImageView.setImageBitmap(bitImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        */



    }

    final static String JPEG_FILE_PREFIX = "IMG_";
    final static String JPEG_FILE_SUFFIX = ".jpg";

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,			// prefix
                JPEG_FILE_SUFFIX,		// suffix
                getAlbumDir()				// directory
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 저장할 위치를 얻는 방법
    // [API level 8 이상]
    public File getAlbumDir(){

        // getAlbumName()
        // sol_camera로 수정

        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                "sol_camera"
        );

        return storageDir;
    }

    /*
        카메라 앱으로 사용할 수 있는 앱이 있는지 체크
     */
    public static boolean isIntentAvailable( Context context, String action){
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent( action);
        List<ResolveInfo> list = packageManager.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /*
        카메라 앱을 통해 찍힌 사진 가져오기
        Activity 내 onActivityResult() 코드내에 아래의 코드를 추가한다.

    private void handleSmallCameraPhoto(Intent intent){
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap)extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
    }
*/


    /*
        저장된 사진을 사진 갤러리에 추가하기
    */
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File( mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile( f);
        mediaScanIntent.setData( contentUri);
        this.sendBroadcast( mediaScanIntent);
    }

    /*
        카메라로 찍은 사진의 용량이 크기 때문에 메모리를 많이 차지하게 된다.
        따라서 사용자에게 보여줄 때는 ImageView 의 사이즈에 맞도록 크기를 조절하여 보여준다.
     */
    private void setPic(){
        int targetW = mImageView.getWidth(); // ImageView 의 가로 사이즈 구하기
        int targetH = mImageView.getHeight(); // ImageView 의 세로 사이즈 구하기

        // Bitmap 정보를 가져온다.
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth; // 사진의 가로 사이즈 구하기
        int photoH = bmOptions.outHeight; // 사진의 세로 사이즈 구하기

        // 사진을 줄이기 위한 비율 구하기
        int scaleFactor = Math.min( photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile( mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }


}
