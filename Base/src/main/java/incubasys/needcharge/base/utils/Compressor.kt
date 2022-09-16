package incubasys.needcharge.base.utils

import android.content.Context
import android.graphics.Bitmap

import java.io.File
import java.io.IOException

/**
 * Created on : June 18, 2016
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
class Compressor(context: Context) {
    //max width and height values of the compressed image is taken as 612x816
    private var maxWidth = 612
    private var maxHeight = 816
    private var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    private var quality = 80
    private var destinationDirectoryPath: String? = null

    init {
        destinationDirectoryPath = (if (context.externalCacheDir == null)
            context.cacheDir.path
        else
            context.externalCacheDir!!.path) + File.separator + "images"
    }

    fun setMaxWidth(maxWidth: Int): Compressor {
        this.maxWidth = maxWidth
        return this
    }

    fun setMaxHeight(maxHeight: Int): Compressor {
        this.maxHeight = maxHeight
        return this
    }

    fun setCompressFormat(compressFormat: Bitmap.CompressFormat): Compressor {
        this.compressFormat = compressFormat
        return this
    }

    fun setQuality(quality: Int): Compressor {
        this.quality = quality
        return this
    }

    fun setDestinationDirectoryPath(destinationDirectoryPath: String): Compressor {
        this.destinationDirectoryPath = destinationDirectoryPath
        return this
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun compressToFile(imageFile: File, compressedFileName: String = imageFile.name): File {
        return ImageUtil.compressImage(
            imageFile, maxWidth, maxHeight, compressFormat, quality,
            destinationDirectoryPath + File.separator + compressedFileName
        )
    }

    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File): Bitmap {
        return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight)
    }

    /* public Flowable<File> compressToFileAsFlowable(final File imageFile) {
        return compressToFileAsFlowable(imageFile, imageFile.getName());
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile, final String compressedFileName) {
        return Flowable.defer(new Callable<Flowable<File>>() {
            @Override
            public Flowable<File> call() {
                try {
                    return Flowable.just(compressToFile(imageFile, compressedFileName));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }

    public Flowable<Bitmap> compressToBitmapAsFlowable(final File imageFile) {
        return Flowable.defer(new Callable<Flowable<Bitmap>>() {
            @Override
            public Flowable<Bitmap> call() {
                try {
                    return Flowable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }*/
}