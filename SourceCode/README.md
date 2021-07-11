#Documentation

## HAR Module
- Open folder "HARModule" with Android Studio.
- After Android Studio indexes the source, push the source to your Github repository.
- Create a Tag that is corresponding to the branch you want to build.
- Login to jitpack.io through your Github account.
- Find the Tag that you have created at the repository in Jitpack.
- Implement this module in your project and the module will be built. It may takes a little time for building.
- If you want to update the module with the model that has higher accuracy, copy the model file and replace in path "HARModule\android_har\src\main\assets". After that, repeat the above steps.
## Healthcare App
- Open folder "HealthcareApp" with Android Studio and build APK file.
## HAR model
- Open file "model/HAR_LSTM_Model.ipynb" with Google Colab.
- Run all the code that is not in comment.
- After running, open "Files" tab on the navigation of Google Colab, find and download file "har_tflite.tflite".
- Now, we had the LSTM model in format of TensorFlow Lite.