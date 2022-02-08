package com.movocash.movo.utilities.extensions

import android.app.Activity
import android.content.*
import android.database.CursorIndexOutOfBoundsException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.CalendarContract.Attendees.query
import android.provider.CalendarContract.EventDays.query
import android.provider.CalendarContract.Instances.query
import android.provider.CalendarContract.Reminders.query
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.query
import android.provider.MediaStore.Images.Thumbnails.query
import android.provider.MediaStore.VOLUME_EXTERNAL
import android.provider.MediaStore.Video.query
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.remote.callback.ICallBackUri
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.utils.RealPathUtil
import com.movocash.movo.view.ui.auth.AuthActivity
import com.movocash.movo.view.ui.base.ActivityBase
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


var fileUri: Uri? = null

val Context.glide: RequestManager?
    get() = Glide.with(this)

fun ImageView.load(path: String) {
    try {
        context.glide!!.load(path).thumbnail(0.1f).into(this)
    } catch (ex: IllegalArgumentException) {

    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showToastMessage(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showLongToastMessage(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun View.errorAnim(context: Context) {
//    if (this is EditText) {
//        this.setHintTextColor(ContextCompat.getColor(context, R.color.white))
//    }
    val anim = AnimationUtils.loadAnimation(context, R.anim.viberation_anim)
    anim.repeatCount = 0
    anim.fillAfter = false
    this.startAnimation(anim)
}

fun Context.isEmailValid(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun Context.convertBitmapToUri(bitmap: Bitmap): Uri? {
    val file = File(this.cacheDir, System.currentTimeMillis().toString() + ".png")
    try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
    val bitmapdata = bos.toByteArray()
    try {
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
    } catch (e: IOException) {

        e.printStackTrace()
    }

    return Uri.fromFile(file)
}


fun getBitmap(v: View): Bitmap? {
    try {
        v.clearFocus()
        v.isPressed = false
        val willNotCache = v.willNotCacheDrawing()
        v.setWillNotCacheDrawing(false)
        val color = v.drawingCacheBackgroundColor
        v.drawingCacheBackgroundColor = 0
        if (color != 0) {
            v.destroyDrawingCache()
        }
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(v.drawingCache)
        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
        v.setWillNotCacheDrawing(willNotCache)
        v.drawingCacheBackgroundColor = color
        return bitmap
    } catch (ex: NullPointerException) {
        ex.printStackTrace()
        return null
    }
}

fun getMIMEType(url: String?): String? {
    var mType: String? = null
    val mExtension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (mExtension != null) {
        mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension)
    }
    return mType
}


fun Activity.startCamera(requestCode: Int) {
    val values = ContentValues(1)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    fileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    startActivityForResult(intent, requestCode)


    if (intent.resolveActivity(packageManager) != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, requestCode)
    }
}
fun Activity.startFrontCamera(requestCode: Int) {
    val values = ContentValues(1)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    fileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra(
            "android.intent.extras.CAMERA_FACING",
            android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
    )
    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
    if (intent.resolveActivity(packageManager) != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, requestCode)
    }
}

fun Activity.showGallery(requestCode: Int) {
    val galleryIntent: Intent?
    galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    galleryIntent.type = "image/*"
   /* startActivityForResult(galleryIntent, requestCode)
    if (galleryIntent.resolveActivity(packageManager) == null) {
        showToastMessage("This Application do not have Gallery Application")
        return
    }*/
    startActivityForResult(galleryIntent, requestCode)
}

fun Activity.showGallery2(requestCode: Int) {


    data class Image(val uri: Uri,
                     val name: String,
                     val duration: Int,
                     val size: Int
    )
    val imgList = mutableListOf<Image>()

    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,

        MediaStore.Images.Media.SIZE
    )



// Display videos in alphabetical order based on their display name.
  /*  val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

    val query = ContentResolver.query(
        collection,
        projection,
        null,
        null,
        sortOrder
    )
    query?.use { cursor ->
        // Cache column indices.
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)

            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )

            // Stores column values and the contentUri in a local object
            // that represents the media file.
            imgList += Image(contentUri, name, size)
        }
    }*/
}



fun Activity.processGalleryPhoto(data: Intent, callback: ICallBackUri) {
    val realPath = RealPathUtil.getRealPath(this, data.data!!)
    fileUri = try {
        Uri.fromFile(File(realPath))
    } catch (ex: Exception) {
        if (realPath != null)
            Uri.parse(realPath)
        else
            Uri.fromFile(File(data.data!!.path))
    }
    callback.imageUri(fileUri)
}


fun Activity.processCapturedPhoto(callback: ICallBackUri) {
    try {
        val cursor = contentResolver.query(
                Uri.parse(fileUri.toString()), Array(1) { MediaStore.Images.ImageColumns.DATA },
                null,
                null,
                null
        )
        cursor!!.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()
        val file = File(photoPath)
        callback.imageUri(Uri.fromFile(file))
    } catch (ex: CursorIndexOutOfBoundsException) {
        ex.printStackTrace()

        val columns: Array<String> = arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DATE_ADDED
        )
        val cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )

        val coloumnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val photoPath = cursor.getString(coloumnIndex)
        cursor.close()
        val file = File(photoPath)
        callback.imageUri(Uri.fromFile(file))
    }

}


fun getFileName(wholePath: String): String {
    var name: String? = null
    val start: Int = wholePath.lastIndexOf('/')
    val end: Int = wholePath.length
    //lastIndexOf('.');
    name = wholePath.substring((start + 1), end)
    return name
}

@Throws(IOException::class)
private fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
    )
}


fun Context.compressFile(fileUri: Uri): Uri {

    val image = this.compressBitmap(this.handleSamplingAndRotationBitmap(fileUri)!!)
    val file = File(this.cacheDir, getFileName(fileUri.path!!))
    try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val bitmap = Bitmap.createScaledBitmap(image, image.width, image.height, true)
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
    val bitmapdata = bos.toByteArray()
    try {
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
    } catch (e: IOException) {

        e.printStackTrace()
    }

    return Uri.fromFile(file)
}

fun Context.compressBitmap(image: Bitmap): Bitmap {

    var finalImage: Bitmap? = null
    if (image.width > Constants.CONST_DEFAULT_IMAGE_WIDTH || image.height > Constants.CONST_DEFAULT_IMAGE_WIDTH) {
        if (image.width > image.height) {
            val ratio = (Constants.CONST_DEFAULT_IMAGE_WIDTH.toFloat() / image.width.toFloat())
            finalImage = Bitmap.createScaledBitmap(
                    image,
                    Constants.CONST_DEFAULT_IMAGE_WIDTH,
                    (image.height * ratio).toInt(),
                    true
            )
        } else if (image.height > image.width) {

            val ratio = (Constants.CONST_DEFAULT_IMAGE_WIDTH.toFloat() / image.height.toFloat())
            finalImage = Bitmap.createScaledBitmap(
                    image,
                    (image.width * ratio).toInt(),
                    Constants.CONST_DEFAULT_IMAGE_WIDTH,
                    true
            )

        } else {
            if (image.width > Constants.CONST_DEFAULT_IMAGE_WIDTH) {
                finalImage = Bitmap.createScaledBitmap(
                        image,
                        Constants.CONST_DEFAULT_IMAGE_WIDTH,
                        Constants.CONST_DEFAULT_IMAGE_WIDTH,
                        true
                )
            } else {
                finalImage = image
            }
        }
    } else {
        finalImage = if (image.width > image.height) {
            val ratio = ((image.width * 0.7) / image.width.toFloat())
            Bitmap.createScaledBitmap(
                    image,
                    (image.width * 0.7).toInt(),
                    (image.height * ratio).toInt(),
                    true
            )
        } else {
            val ratio = ((image.height * 0.7) / image.height.toFloat())
            Bitmap.createScaledBitmap(
                    image,
                    (image.width * ratio).toInt(),
                    (image.height * 0.7).toInt(),
                    true
            )
        }
    }
    return finalImage!!
}

@Throws(IOException::class)
fun Context.handleSamplingAndRotationBitmap(selectedImage: Uri): Bitmap? {
    val MAX_HEIGHT = 1024
    val MAX_WIDTH = 1024

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var imageStream = contentResolver.openInputStream(selectedImage)
    BitmapFactory.decodeStream(imageStream, null, options)
    imageStream!!.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    imageStream = contentResolver.openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)
    if (img != null)
        img = rotateImageIfRequired(img, selectedImage)
    else {
        showToastMessage("Image not available")
        return null
    }

    return img
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
        val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    }
    return inSampleSize
}

private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap {
    val ei = ExifInterface(selectedImage.path!!)
    val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
        else -> { img }
    }
}


private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

fun Context.getRootParentFragment(fragment: Fragment): Fragment {
    val parent = fragment.parentFragment
    return if (parent == null)
        fragment
    else
        getRootParentFragment(parent)
}

private fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun Context.uriToBase64(imageUri: Uri):String{
    val imageStream: InputStream? = this.contentResolver.openInputStream(imageUri)
    val selectedImage = BitmapFactory.decodeStream(imageStream)
    val encodedImage = encodeImage(selectedImage)
    return encodedImage!!
}

fun convertCountToMonth(month: Int): String {
    return when (month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
}

fun Context.copyText(text: String) {
    val clipboard: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

fun gotoHome() {
    val fm = ActivityBase.activity.supportFragmentManager
    for (i in 0 until fm.backStackEntryCount) {
        fm.popBackStack()
    }
}


fun Context.openFacebookIntent() {
    try {
        this.packageManager.getPackageInfo("com.facebook.katana", 0)
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/579659758816833")))
    } catch (e: Exception) {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/movocash")))
    }
}


fun watchYoutubeVideo(id: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
    try {
        ActivityBase.activity.startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        ActivityBase.activity.startActivity(webIntent)
    }
}

fun currentOS(): String {
    val builder = StringBuilder()
    val fields = Build.VERSION_CODES::class.java.fields
    for (field in fields) {
        val fieldName = field.name
        var fieldValue = -1

        try {
            fieldValue = field.getInt(Any())
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        if (fieldValue == Build.VERSION.SDK_INT) {
            builder.append(fieldName)
        }
    }
    return builder.toString()
}

fun onTokenExpiredLogout(){
    val lastUserName = MovoApp.db.getString(Constants.USER_NAME)
    val lastUserPass = MovoApp.db.getString(Constants.USER_PASS)
    val rememberUserPass = MovoApp.db.getString(Constants.REMEMBERED_USER_NAME)
    MovoApp.db.clear()
    MovoApp.db.putString(Constants.USER_NAME, lastUserName!!)
    MovoApp.db.putString(Constants.USER_PASS, lastUserPass!!)
    MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, rememberUserPass!!)
    val intent = Intent(ActivityBase.activity, AuthActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    ActivityBase.activity.startActivity(intent)
    ActivityBase.activity.finish()
}