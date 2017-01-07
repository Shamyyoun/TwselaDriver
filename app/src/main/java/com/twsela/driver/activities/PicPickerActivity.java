package com.twsela.driver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.twsela.driver.R;
import com.twsela.driver.utils.BitmapUtils;
import com.twsela.driver.utils.FileUtils;

import java.io.File;

public abstract class PicPickerActivity extends ParentActivity {
    private static int REQ_CAMERA_PERMISSIONS = 101;
    private static int REQ_STORAGE_PERMISSION = 102;
    private static int REQ_CAPTURE_CAMERA = 103;
    private static int REQ_PICK_GALLERY = 104;
    private static final String TITLE_TMP_IMAGE = "temp-image";
    private static final String TITLE_CROPPED_TMP_IMAGE = "cropped-temp-image";
    private int requestCode;
    private int aspectX;
    private int aspectY;
    private int maxImageDimen;
    private int maxImageWidth;
    private int maxImageHeight;
    private boolean crop;
    private File image;
    private File croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get fields from saved instance state
        if (savedInstanceState != null) {
            requestCode = savedInstanceState.getInt("requestCode");
            aspectX = savedInstanceState.getInt("aspectX");
            aspectY = savedInstanceState.getInt("aspectY");
            maxImageDimen = savedInstanceState.getInt("maxImageDimen");
            maxImageWidth = savedInstanceState.getInt("maxImageWidth");
            maxImageHeight = savedInstanceState.getInt("maxImageHeight");
            crop = savedInstanceState.getBoolean("crop");
            image = (File) savedInstanceState.getSerializable("image");
            croppedImage = (File) savedInstanceState.getSerializable("croppedImage");
        }
    }

    // -------------------- Setter Methods --------------------
    public void setPickerMaxDimen(int maxDimen) {
        maxImageDimen = maxDimen;
        maxImageWidth = 0;
        maxImageHeight = 0;
    }

    public void setPickerMaxDimens(int width, int height) {
        maxImageWidth = width;
        maxImageHeight = height;
        maxImageDimen = 0;
    }

    public void setPickerAspects(int aspectX, int aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }

    // -------------------- Gallery Methods --------------------
    public void pickFromGallery(int requestCode, boolean crop) {
        // set fields
        this.requestCode = requestCode;
        this.crop = crop;

        // check write permission
        boolean writePermGranted = checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if (writePermGranted) {
            // open the gallery intent
            openGalleryIntent();
        } else {
            // request write permission from user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_STORAGE_PERMISSION);
        }
    }

    private void openGalleryIntent() {
        // open gallery intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_PICK_GALLERY);
    }

    // -------------------- Camera Methods --------------------
    public void captureFromCamera(int requestCode, boolean crop) {
        // set fields
        this.requestCode = requestCode;
        this.crop = crop;

        // prepare permissions granting result
        boolean writePermGranted = checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean cameraPermGranted = checkCallingOrSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        // check the permissions
        if (writePermGranted && cameraPermGranted) {
            // open the camera intent
            openCameraIntent();
        } else {
            // request permissions from user
            String[] requiredPermissions = null;
            if (!(writePermGranted && cameraPermGranted)) {
                requiredPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            } else if (!writePermGranted) {
                requiredPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            } else if (!cameraPermGranted) {
                requiredPermissions = new String[]{Manifest.permission.CAMERA};
            }

            if (requiredPermissions != null) {
                ActivityCompat.requestPermissions(this, requiredPermissions, REQ_CAMERA_PERMISSIONS);
            }
        }
    }

    private void openCameraIntent() {
        // check if the device has a camera app
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile;
        if (intent.resolveActivity(getPackageManager()) != null) {
            // create the temp image
            image = BitmapUtils.createImagePath(this, TITLE_TMP_IMAGE);
            tempFile = image;

            // check the temp file creation
            if (tempFile != null) {
                // open camera intent
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQ_CAPTURE_CAMERA);
            } else {
                Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.no_camera_app_found_in_this_device, Toast.LENGTH_SHORT).show();
        }
    }

    // -------------------- Permission Method --------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_CAMERA_PERMISSIONS) {
            // prepare grant result
            boolean allGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            // check result
            if (allGranted) {
                openCameraIntent();
            } else {
                Toast.makeText(this, R.string.you_need_to_grant_required_permissions, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_STORAGE_PERMISSION) {
            // prepare grant result
            boolean granted = false;
            if (grantResults.length > 0 &&
                    (grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED)) {
                granted = true;
            }

            // check result
            if (granted) {
                openGalleryIntent();
            } else {
                Toast.makeText(this, R.string.you_need_to_grant_required_permissions, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // -------------------- Activity Result Method --------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_PICK_GALLERY) {
                // check image path
                String imagePath = FileUtils.getPath(this, data.getData());
                if (imagePath == null) {
                    Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
                    return;
                }

                // copy the image to app folders
                image = BitmapUtils.createImagePath(this, TITLE_TMP_IMAGE);
                boolean copied = FileUtils.copy(imagePath, image.getAbsolutePath());

                if (!copied || image == null) {
                    Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
                    return;
                }

                // resize the image if required
                image = resizeImage(image);

                // check if should crop the image
                if (crop) {
                    // crop the image
                    croppedImage = BitmapUtils.createImagePath(this, TITLE_CROPPED_TMP_IMAGE);
                    if (croppedImage != null) {
                        Crop crop = Crop.of(Uri.fromFile(image), Uri.fromFile(croppedImage));
                        if (!(aspectX == 0 && aspectY == 0)) {
                            crop.withAspect(aspectX, aspectY);
                        }
                        crop.start(this);
                    } else {
                        Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // return the image
                    returnImage(image);
                }
            } else if (requestCode == REQ_CAPTURE_CAMERA) {
                // check to resize the image
                if (image != null) {
                    image = resizeImage(image);
                } else {
                    Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (crop) {
                    // crop the image
                    croppedImage = BitmapUtils.createImagePath(this, TITLE_CROPPED_TMP_IMAGE);
                    if (croppedImage != null) {
                        Crop crop = Crop.of(Uri.fromFile(image), Uri.fromFile(croppedImage));
                        if (!(aspectX == 0 && aspectY == 0)) {
                            crop.withAspect(aspectX, aspectY);
                        }
                        crop.start(this);
                    } else {
                        Toast.makeText(this, R.string.error_fetching_this_image, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // return the original image
                    returnImage(image);
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                // return the cropped image
                returnImage(croppedImage);
            }
        }
    }

    private File resizeImage(File image) {
        if (image == null) {
            return image;
        }

        if (maxImageDimen == 0 && maxImageWidth == 0 && maxImageHeight == 0) {
            return image;
        } else {
            if (maxImageDimen == 0) {
                BitmapUtils.resizeBitmap(image.getAbsolutePath(), maxImageWidth, maxImageHeight);
            } else {
                BitmapUtils.resizeBitmap(image.getAbsolutePath(), maxImageDimen);
            }

            return image;
        }
    }

    private void returnImage(File image) {
        // reset fields
        aspectX = 0;
        aspectY = 0;
        maxImageDimen = 0;
        maxImageWidth = 0;
        maxImageHeight = 0;

        // fire the abstract method
        onImageReady(requestCode, image);
    }

    public abstract void onImageReady(int requestCode, File image);

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save fields
        outState.putInt("requestCode", requestCode);
        outState.putInt("aspectX", aspectX);
        outState.putInt("aspectY", aspectY);
        outState.putInt("maxImageDimen", maxImageDimen);
        outState.putInt("maxImageWidth", maxImageWidth);
        outState.putInt("maxImageHeight", maxImageHeight);
        outState.putBoolean("crop", crop);
        outState.putSerializable("image", image);
        outState.putSerializable("croppedImage", croppedImage);

        super.onSaveInstanceState(outState);
    }
}