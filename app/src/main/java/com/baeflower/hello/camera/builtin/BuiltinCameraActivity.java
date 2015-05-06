package com.baeflower.hello.camera.builtin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.camera.adapter.PictureFolderGridAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BuiltinCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {


    private static final String TAG = BuiltinCameraActivity.class.getSimpleName();
    private SurfaceView mPreview;
    private Button mBtnShutter;
    private Button mBtnFolderAdd;
    private GridView mGridViewFolder;

    private Camera mCamera;
    private String mRoot;
    private String mSaveRoot;

    private HashMap<String, String> mPathMap;

    // 카메라 찍은 후 저장될 파일 경로
    private String mFilePath;
    // private String mFolderName;// 폴더명
    // private String mFileName; // 파일명


    private String[] mExistFolderNameArr;
    private List<String> mPossibleFolderNameList; // 저장 가능한 폴더 리스트
    private PictureFolderGridAdapter mPictureFolderGridAdapter;


    private void init() {
        mPreview = (SurfaceView) findViewById(R.id.sv_camera);
        mBtnShutter = (Button) findViewById(R.id.btn_camera_shutter);
        mBtnFolderAdd = (Button) findViewById(R.id.btn_add_folder);
        mGridViewFolder = (GridView) findViewById(R.id.gl_folderName_list);

        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // 사진 찍는 버튼
        mBtnShutter.setOnClickListener(this);
        // 폴더 추가 버튼
        mBtnFolderAdd.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // action bar(title bar)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // status bar(맨위에...)
        setContentView(R.layout.activity_builtin_camera);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();


        // SD 카드가 장착되어 있지 않다면 앱 종료
//        if( isSdCard() == false )
//            finish();

        // SD 카드 루트 폴더의 경로를 구한다 (/storage/emulated/0)
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

        String pictureFolderPath = mRoot + "/Pictures";
        setFolderList(pictureFolderPath);

        if (mPossibleFolderNameList != null) {
            mPictureFolderGridAdapter = new PictureFolderGridAdapter(BuiltinCameraActivity.this, mPossibleFolderNameList);
            mGridViewFolder.setAdapter(mPictureFolderGridAdapter);
        }
    }

    // SD 카드 장착 여부를 반환
    private boolean isSdCard() {
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED) == false) {
            Toast.makeText(this, "SD Card does not exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*
        특정 폴더의 파일 목록을 구해서 반환
     */
    private void setFolderList(String folderPath) {
        File fileRoot = new File(folderPath); // 폴더 경로를 지정해서 File 객체 생성

        if(fileRoot.isDirectory() == true){ // 해당 경로가 폴더가 아니라면 함수 탈출
            mExistFolderNameArr = fileRoot.list(); // 파일 목록을 구한다
        }
        mPossibleFolderNameList = addDefaultFolder();
    }

    private List<String> addDefaultFolder() {
        mPathMap = new HashMap<>();
        String tmpFolderName;

        String defaultFolderRoot = mRoot + "/DCIM";
        File defaultFileRoot = new File(defaultFolderRoot);

        List<String> tempFolderList = new ArrayList<>();
        int defaultFolderListSize;
        int pictureFolderListSize = 0;

        if (defaultFileRoot.isDirectory() == true) { // 기본 사진 저장 폴더에 있는 폴더들 추가하기
            defaultFolderListSize = defaultFileRoot.list().length;

            for (int i = 0; i < defaultFolderListSize; i++) {
                tmpFolderName = defaultFileRoot.list()[i];

                if (!".thumbnails".equals(tmpFolderName) && !"100ANDRO".equals(tmpFolderName)) {
                    tempFolderList.add(tmpFolderName);
                    mPathMap.put(tmpFolderName, defaultFolderRoot + "/" + tmpFolderName);
                }
            }
        }

        if (mExistFolderNameArr != null) {
            pictureFolderListSize = mExistFolderNameArr.length;

            for (int i = 0; i < pictureFolderListSize; i++) {
                tmpFolderName =  mExistFolderNameArr[i];

                if (!"cache".equals(tmpFolderName) && !".tempLu".equals(tmpFolderName)) {
                    tempFolderList.add(tmpFolderName);
                    mPathMap.put(tmpFolderName , mRoot + "/Pictures/" + tmpFolderName);
                }
            }
        }

        return tempFolderList;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_camera_shutter:
                savePicture();
                break;
            case R.id.btn_add_folder:
                showDialogToAddFolder();
                break;
        }
    }

    private void savePicture() {
        /*
            예제1. 기본 경로에 저장됨

            // 포커싱 성공하면 촬영 허가
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if(success){
                        mBtnShutter.setEnabled(success);
                        mCamera.takePicture(null, null, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                // 2차원인데 data는 1차원으로 들어오네? 1차원 데이터를 2차원으로 변환해서 bitmap으로 뽑아냄
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, null);

                                // Content provider를 이용해서 사진폴더에 저장
                                // String savedImageUri = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "", null);
                                // Content Resolver, path, name, description
                                String path = mPictureFolderGridAdapter.getmDirToSave();
                                if (path == null) {
                                    path = mRoot + "/DCIM/Camera";
                                }
                                // content://media/external/images/media/2096
                                String savedImageUri = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, createFileName(), null);

                                // 사진을 찍은 다음에
                                Uri uri = Uri.parse(savedImageUri);

                                // Media Scan
                                // 사진 앱 들의 DB를 갱신하기 위해서(사진 앱을 실행했을 때 이 파일이 바로 검색 되도록?!)
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                                Toast.makeText(BuiltinCameraActivity.this, "사진이 저장됐습니다.", Toast.LENGTH_SHORT).show();

                                // 다시 프리뷰를 작동
                                camera.startPreview();

                            }
                        });
                    }
                }
            });
        */

        /*
            예제2. 기본 경로 말고 다른 곳에 저장시키기!
         */
        mCamera.autoFocus(new Camera.AutoFocusCallback(){
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                            File path;
                            String dirToSave = mPictureFolderGridAdapter.getmDirToSave();
                            String saveFolder;
                            if (!TextUtils.isEmpty(dirToSave)) {
                                path = new File(mPathMap.get(dirToSave));
                                saveFolder = dirToSave;
                            } else {
                                path = new File(mPathMap.get("Camera")); // 기본 path
                                saveFolder = "Camera";
                            }

                            if (path.isDirectory()) {
                                File targetFile = new File(path, createFileName());

                                try {
                                    FileOutputStream fos = new FileOutputStream(targetFile, false);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    fos.close();

                                    // /storage/emulated/0/DCIM/Camera/파일이름 (absolute path)
                                    // String savedFilePath = targetFile.getPath();
                                    // Uri uri = Uri.parse(savedFilePath);

                                    Uri uri = Uri.fromFile(targetFile);

                                    // Media Scan
                                    // 사진 앱 들의 DB를 갱신하기 위해서(사진 앱을 실행했을 때 이 파일이 바로 검색 되도록?!)
                                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                                    Toast.makeText(BuiltinCameraActivity.this, "* " + saveFolder + "에 사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                                    // 다시 프리뷰를 작동
                                    camera.startPreview();

                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "FileNotFoundException");
                                } catch (IOException e) {
                                    Log.d(TAG, "IOException");
                                }
                            }
                        }
                    }); // takePicture
                }
            }
        }); // autoFocus
    }


    final static String JPEG_FILE_PREFIX = "IMG_";
    final static String JPEG_FILE_SUFFIX = ".jpg";

    private String createFileName() {
        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + JPEG_FILE_SUFFIX;
        return imageFileName;
    }

    private EditText mDialogEtFolderName;

    /*

     */
    private void showDialogToAddFolder() {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(BuiltinCameraActivity.this);

        final View folderAddDialog = inflater.inflate(R.layout.dialog_camera_add_folder, null);

        builder.setTitle("폴더 추가");
        builder.setView(folderAddDialog);

        mDialogEtFolderName = (EditText) folderAddDialog.findViewById(R.id.dialog_et_add_folder_name);

        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String folderName = mDialogEtFolderName.getText().toString();

                if (TextUtils.isEmpty(folderName)) {
                    Toast.makeText(getApplicationContext(), "* 폴더명을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isAdded = addPictureFolder(folderName);

                    if (isAdded) {
                        Toast.makeText(getApplicationContext(), "* " + folderName + " 이 생성되었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "* 중복된 폴더명 입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("닫기", null);

        builder.show();
    }


    /*
        폴더 경로 추가하기
     */
    private boolean addPictureFolder(String folderName) {
        // 저장하려는 경로에 중복된 폴더명이 있을 경우 만들어지면 안됨
        // 해당하는 폴더명(key)으로 path 있으면 무조건 안만들어지도록 함
        // 카메라 앱들의 경로는 DCIM 밑에랑 Pictures 두군데로 저장되는데 여기서 만들어지는 것은 전부 Pictures 밑으로 저장
        String existPath = mPathMap.get(folderName);

        if (!TextUtils.isEmpty(existPath)) {
            return false;
        } else {
            // 만든다
            String addPath = mRoot + "/Pictures/" + folderName;
            File addFolder = new File(addPath);
            addFolder.mkdir();

            mPathMap.put(folderName, addPath);

            mPossibleFolderNameList.add(folderName);
            mPictureFolderGridAdapter.notifyDataSetChanged();

            return true;
        }
    }


    /**
     * 폴더 삭제 다이얼로그(어댑터에서 호출)
     */
    public void showFolderDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuiltinCameraActivity.this);

        final String folderName = mPossibleFolderNameList.get(position);
        final int finalPosition = position;

        builder.setTitle("폴더 삭제");
        builder.setMessage(folderName + " 폴더를 삭제하시겠습니까?");

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(folderName)) {
                    Toast.makeText(getApplicationContext(), "* 기본 폴더는 삭제할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {

                    String path = mPathMap.get(folderName);
                    File targetFolder = new File(path);

                    if (targetFolder.exists()) {
                        targetFolder.delete();

                        mPossibleFolderNameList.remove(finalPosition);
                        mPathMap.remove(folderName);
                        mPictureFolderGridAdapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(), "* " + folderName + " 폴더가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "* 존재하지 않는 폴더입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).setNegativeButton("닫기", null);

        builder.create();
        builder.show();
    }
































    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_builtin_camera, menu);
        return true;//false를 해줘어야함. 메뉴 사용 못함
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();

        try {
            //set camera to continually auto-focus
            Camera.Parameters params = mCamera.getParameters();
            if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else {
                //Choose another supported mode
            }

            List<Camera.Size> sizeList = params.getSupportedPictureSizes();
            // 카메라 SupportedPictureSize목록 출력 로그
            for(int i=0; i<sizeList.size(); i++){
                Camera.Size size = sizeList.get(i);
                Log.d(TAG, "Width : " + size.width + ", Height : " + size.height);
            }

            Camera.Size size = getOptimalPictureSize(params.getSupportedPictureSizes(), 1280, 720);
            Log.d(TAG, "Selected Optimal Size : (" + size.width + ", " + size.height + ")");
            params.setPreviewSize(size.width,  size.height);
            params.setPictureSize(size.width,  size.height);


            mCamera.setParameters(params);

            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.startPreview(); // 실행
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.release(); // 메모리에서 해제
        mCamera = null;
    }


    private void setDirecctoryForSaving() {
        /*
            // 외부 저장소 경로
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d(TAG, "외부 저장소 경로 : " + path);

            // 폴더명 및 파일 명
            String folderPath = path + File.separator + mFolderName;
            mFilePath = path + File.separator + mFolderName + File.separator + mFileName + ".jpg";

            // 저장 폴더 지정 및 폴더 생성
            File fileFolderPath = new File(folderPath);
            fileFolderPath.mkdir();

            // 파일 이름 지정
            File file = new File(mFilePath);
            Uri outputFileUri = Uri.fromFile(file);
        */
    }

    // 지정한 해상도에 가장 최적화 된 카메라 캡쳐 사이즈 구해주는 함수
    private Camera.Size getOptimalPictureSize(List<Camera.Size> sizeList, int width, int height){
        Log.d(TAG, "getOptimalPictureSize, 기준 width,height : (" + width + ", " + height + ")");
        Camera.Size prevSize = sizeList.get(0);
        Camera.Size optSize = sizeList.get(1);
        for(Camera.Size size : sizeList){
            // 현재 사이즈와 원하는 사이즈의 차이
            int diffWidth = Math.abs((size.width - width));
            int diffHeight = Math.abs((size.height - height));

            // 이전 사이즈와 원하는 사이즈의 차이
            int diffWidthPrev = Math.abs((prevSize.width - width));
            int diffHeightPrev = Math.abs((prevSize.height - height));

            // 현재까지 최적화 사이즈와 원하는 사이즈의 차이
            int diffWidthOpt = Math.abs((optSize.width - width));
            int diffHeightOpt = Math.abs((optSize.height - height));

            // 이전 사이즈보다 현재 사이즈의 가로사이즈 차이가 적을 경우 && 현재까지 최적화 된 세로높이 차이보다 현재 세로높이 차이가 적거나 같을 경우에만 적용
            if(diffWidth < diffWidthPrev && diffHeight <= diffHeightOpt){
                optSize = size;
                Log.d(TAG, "가로사이즈 변경 / 기존 가로사이즈 : " + prevSize.width + ", 새 가로사이즈 : " + optSize.width);
            }
            // 이전 사이즈보다 현재 사이즈의 세로사이즈 차이가 적을 경우 && 현재까지 최적화 된 가로길이 차이보다 현재 가로길이 차이가 적거나 같을 경우에만 적용
            if(diffHeight < diffHeightPrev && diffWidth <= diffWidthOpt){
                optSize = size;
                Log.d(TAG, "세로사이즈 변경 / 기존 세로사이즈 : " + prevSize.height + ", 새 세로사이즈 : " + optSize.height);
            }

            // 현재까지 사용한 사이즈를 이전 사이즈로 지정
            prevSize = size;
        }
        Log.d(TAG, "결과 OptimalPictureSize : " + optSize.width + ", " + optSize.height);
        return optSize;
    }


    // 이미지, 동영상 기본 저장 경로 구하기
    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

























    // 참고
    // 1. SurfaceView 상속
    // 2. callback implementation
    // view 위에 buffer가 있음. buffer 조작을 하려면 Holder 객체를 가져와야된다
    // Holder가 없어지고 어쩌고 하는 거에 따라서 Callback에 들어옴
    // 3. 생성자에서 getHolder()
    public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private Camera mCamera; // 롤리팝 부터는 코드가 바뀜. 분기타서 따로 구현해줘야한다

        // view 단은 건드릴게 없고 Holder 쪽 작업이 필요함
        public CameraSurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this); // 뒷 단의 buffer에서 무슨 일이 일어나면 여기의 callback으로 연결됨
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = Camera.open();

            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mCamera.startPreview(); // 실행
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.release(); // 메모리에서 해제
            mCamera = null;
        }
    }
}
