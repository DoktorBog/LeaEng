package doktor.bog.leaeng.base_adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class TTSManager {

    private TextToSpeech mTts = null;
    private boolean isLoaded = false;


    public void init(Context context) {
        try {

            mTts = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {

                isLoaded = true;

            }
        }
    };

    public void shutDown() {
        mTts.shutdown();
    }

    public void addQueue(String text,Locale local) {
        if (isLoaded){
            mTts.setLanguage(local);
            mTts.speak(text, TextToSpeech.QUEUE_ADD, null);}

        else
            Log.e("error", "TTS Not Initialized");
    }
}