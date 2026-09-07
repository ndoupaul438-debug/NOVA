package com.nova.app.school;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;

public final class CiaTeacherVoice {

    public interface Listener {
        void onTranscript(String text);
        void onListening(boolean listening);
        void onError(String message);
    }

    private final Activity activity;
    private final Listener listener;

    private SpeechRecognizer recognizer;
    private TextToSpeech tts;

    public CiaTeacherVoice(Activity activity, Listener listener) {
        this.activity = activity;
        this.listener = listener;

        tts = new TextToSpeech(
                activity,
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.getDefault());
                    }
                }
        );
    }

    public boolean hasMicrophonePermission() {
        return activity.checkSelfPermission(
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestMicrophonePermission(int requestCode) {
        activity.requestPermissions(
                new String[]{Manifest.permission.RECORD_AUDIO},
                requestCode
        );
    }

    public void startListening() {
        if (!hasMicrophonePermission()) {
            listener.onError("Microphone permission is required.");
            return;
        }

        if (!SpeechRecognizer.isRecognitionAvailable(activity)) {
            listener.onError(
                    "Speech recognition is not available on this Android device."
            );
            return;
        }

        stopListening();

        recognizer = SpeechRecognizer.createSpeechRecognizer(activity);

        recognizer.setRecognitionListener(
                new android.speech.RecognitionListener() {

                    @Override
                    public void onReadyForSpeech(android.os.Bundle params) {
                        listener.onListening(true);
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                    }

                    @Override
                    public void onEndOfSpeech() {
                        listener.onListening(false);
                    }

                    @Override
                    public void onError(int error) {
                        listener.onListening(false);
                        listener.onError(
                                "Speech recognition error: " + error
                        );
                    }

                    @Override
                    public void onResults(android.os.Bundle results) {
                        listener.onListening(false);

                        ArrayList<String> matches =
                                results.getStringArrayList(
                                        SpeechRecognizer.RESULTS_RECOGNITION
                                );

                        if (matches != null && !matches.isEmpty()) {
                            listener.onTranscript(matches.get(0));
                        }
                    }

                    @Override
                    public void onPartialResults(android.os.Bundle partialResults) {
                    }

                    @Override
                    public void onEvent(int eventType, android.os.Bundle params) {
                    }
                }
        );

        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
        );

        recognizer.startListening(intent);
    }

    public void stopListening() {
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer.destroy();
            recognizer = null;
        }

        listener.onListening(false);
    }

    public void speak(String text) {
        if (tts == null || text == null || text.trim().isEmpty()) {
            return;
        }

        tts.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "CIA_TEACHER"
        );
    }

    public void stopSpeaking() {
        if (tts != null) {
            tts.stop();
        }
    }

    public void destroy() {
        stopListening();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
