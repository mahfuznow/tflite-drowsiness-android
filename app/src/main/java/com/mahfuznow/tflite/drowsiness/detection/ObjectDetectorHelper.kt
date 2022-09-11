package com.mahfuznow.tflite.drowsiness.detection

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.examples.objectdetection.ml.YawnEyeDatasetNew
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.label.Category


class ObjectDetectorHelper(
    val context: Context,
    val objectDetectorListener: DetectorListener?
) {
    fun detect(bitmap: Bitmap, imageRotation: Int) {

        /*** If the .tflite model only accept a fixed shape then we need to convert the input matrix to that shape
         *
         *
        val imageProcessor: ImageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeMethod.NEAREST_NEIGHBOR))
            .add(Rot90Op(-imageRotation / 90))
            .build()
        val tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)
        val resizedTensorImage = imageProcessor.process(tensorImage)
        val resizedBitmap = resizedTensorImage.bitmap
        */

        val model = YawnEyeDatasetNew.newInstance(context)
        // Creates inputs for reference.
        val tensorImage = TensorImage.fromBitmap(bitmap)
        // Runs model inference and gets result.
        val outputs = model.process(tensorImage)
        val probability = outputs.probabilityAsCategoryList

        model.close()

        objectDetectorListener?.onResults(probability)
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(probability: MutableList<Category>)
    }
}
