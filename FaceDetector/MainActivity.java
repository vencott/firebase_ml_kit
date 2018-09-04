package com.vencott.facedetector;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        findViewById(R.id.btn_camera).setOnClickListener(new SimpleOnClickListener() {
            @Override
            protected void handleOnClick(View view) {
                selectPhotoFromCamera(new SimplePhotoLoader(111, 111), getNewPhotoFilename(true, ".jpg"));
            }
        });
    }

    @Override
    protected void handleOnSelectPhotoResult(boolean isSucceeded, Uri[] photoUries) {
        super.handleOnSelectPhotoResult(isSucceeded, photoUries);
        
        Bitmap bitmap = ImageUtil.decodeBitmapFromUri(getApplicationContext(), photoUries[0], 1280, true);
        
        detectFace(bitmap, new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
                Toast.makeText(getApplicationContext(), "Detected faces : " + faces.size(), Toast.LENGTH_SHORT).show();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Firebase error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void detectFace(Bitmap bitmap, OnSuccessListener<List<FirebaseVisionFace>> onSuccessListener, OnFailureListener onFailureListener) {
        if (bitmap == null)
            return;

        FirebaseVision.getInstance().getVisionFaceDetector(new FirebaseVisionFaceDetectorOptions.Builder().build())
                .detectInImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
